package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class TimelineService {
    private final LinkedBlockingQueue<SectionData> sectionQueue = new LinkedBlockingQueue<>();
    private final BarEvent[] barEvents = new BarEvent[17];
    Clock clock = Clock.systemUTC();
    private SectionData currentSectionData;
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
                applicationEventPublisher.publishEvent(barEvents[currentBar]);
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

    private boolean shouldTimeLineEnd() {
        if (currentSectionData == null) return false;
        if (end || !currentSectionData.loop()) {
            switch (currentSectionData.transitionType()) {
                case END -> {
                    return currentBar == currentSectionData.numBars();
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
        boolean lastBarPlayed = currentSectionData != null && currentBar > currentSectionData.numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
    }

    public void addToSectionQueue(SectionData sectionData) throws InterruptedException {
        sectionQueue.put(sectionData);
        if (currentSectionData == null) {
            loadNextSection();
        }
    }

    public void loadNextSection() throws InterruptedException {
        currentSectionData = sectionQueue.take();
        this.barLength = (long) (60000.0 / currentSectionData.bpm()) * currentSectionData.numBeats();
        previousTime = 0;
        overTime = 0;
    }

    public void stopAndCleanUp() {
        stop();
        sectionQueue.clear();
        currentSectionData = null;
        currentBar = 0;
        previousTime = 0;
        overTime = 0;
        end = false;
        runTimer = false;
        barLength = 0;

    }

    public SectionData getCurrentSectionData() {
        return currentSectionData;
    }
}

