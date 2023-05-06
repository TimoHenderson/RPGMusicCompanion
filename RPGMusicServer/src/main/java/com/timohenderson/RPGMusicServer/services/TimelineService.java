package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class TimelineService {
    private final ConcurrentLinkedQueue<Section> sectionQueue = new ConcurrentLinkedQueue<>();
    private final BarEvent[] barEvents = new BarEvent[17];
    Clock clock = Clock.systemUTC();
    private volatile Section currentSection;
    private volatile boolean end = false;
    private volatile boolean runTimer;
    private int currentBar = 0;
    private long barLength = 0;
    private long previousTime = 0;
    private long overTime = 0;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public TimelineService() {
        for (int i = 0; i < 17; i++) {
            barEvents[i] = new BarEvent(this, i);
        }
    }

    @Async
    public void play() throws InterruptedException {
        if (!runTimer) {
            runTimer = true;
            while (runTimer) {
                sendBarEvent();
                long currentTime = clock.millis();
                if (previousTime != 0) {
                    overTime = currentTime - previousTime - barLength;
                }
                previousTime = currentTime;
                Thread.sleep(barLength - overTime);
                if (shouldTimeLineEnd()) {
                    loadNextSection();
                    end = false;
                }
                nextBar();
            }
        }
        previousTime = 0;
    }

    private void sendBarEvent() {
        applicationEventPublisher.publishEvent(barEvents[currentBar]);
    }
    

    private boolean shouldTimeLineEnd() {
        if (currentSection == null) return false;
        if (end || !currentSection.getSectionData().loop()) {
            switch (currentSection.getSectionData().transitionType()) {
                case END -> {
                    return currentBar == currentSection.getSectionData().numBars();
                }
                case NEXT_BAR -> {
                    currentBar = 0;
                    return true;
                }
            }
        }
        return false;
    }


    public void stop() {
        runTimer = false;
    }

    public void end() {
        end = true;
    }

    public void triggerNextSection() {
        end = true;
    }

    private void nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentSection != null && currentBar > currentSection.getSectionData().numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
    }

    public void addToSectionQueue(Section section) throws InterruptedException {
        sectionQueue.offer(section);
        if (currentSection == null) {
            loadNextSection();
        }
    }

    public void loadNextSection() throws InterruptedException {
        currentSection = sectionQueue.poll();
        if (currentSection == null) {
            stopAndCleanUp();
            return;
        }
        this.barLength = (long) (60000.0 / currentSection.getSectionData().bpm()) * currentSection.getSectionData().numBeats();
        previousTime = 0;
        overTime = 0;
        applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    }

    public void stopAndCleanUp() {
        stop();
        sectionQueue.clear();
        currentSection = null;
        currentBar = 0;
        previousTime = 0;
        overTime = 0;
        end = false;
        runTimer = false;
        barLength = 0;

    }

    public Section getCurrentSection() {
        return currentSection;
    }
}

