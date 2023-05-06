package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class TimelineService {
    private final LinkedBlockingQueue<Section> sectionQueue = new LinkedBlockingQueue<Section>();
    private final BarEvent[] barEvents = new BarEvent[17];
    //Clock clock = Clock.systemUTC();

    private Section currentSection;
    private volatile boolean end = false;
    private volatile boolean runTimer;
    private int currentBar = 1;
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
    public void play() throws InterruptedException, LineUnavailableException {
        if (!runTimer) {
            runTimer = true;
            while (runTimer) {
//                audioPlayerService.play();
                applicationEventPublisher.publishEvent(barEvents[currentBar]);

//                long currentTime = clock.millis();
//                if (previousTime != 0) {
//                    overTime = currentTime - previousTime - barLength;
//                }
//                previousTime = currentTime;
                System.out.println("Bar: " + currentBar + " OverTime: " + overTime + " BarLength: " + barLength);
//                Thread.sleep(barLength - overTime);
                Thread.sleep(barLength - 4);
                nextBar();
                if (shouldTimeLineEnd()) {
                    loadNextSection();
                    end = false;
                }


            }
        }
        previousTime = 0;
    }

    private void sendBarEvent() {

    }


    private boolean shouldTimeLineEnd() {
        //System.out.println("shouldTimeLineEnd: " + currentBar + " " + currentSection.getSectionData().numBars());
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
            System.out.println("No more sections to play");
            stopAndCleanUp();
            return;
        }
        reset();
        this.barLength = (long) (60000.0 / currentSection.getSectionData().bpm()) * currentSection.getSectionData().numBeats();
        System.out.println("Loading section: " + currentSection.getName());

        applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    }

    private void reset() {
        currentBar = 1;
        previousTime = 0;
        overTime = 0;
        end = false;
    }

    public void stopAndCleanUp() {
        stop();
        sectionQueue.clear();
        currentSection = null;
        currentBar = 1;
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

