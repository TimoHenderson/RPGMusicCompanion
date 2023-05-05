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
    Clock clock = Clock.systemUTC();
    private SectionData currentSectionData;
    private LinkedBlockingQueue<SectionData> sectionQueue = new LinkedBlockingQueue<>();
    private volatile boolean end;
    private volatile boolean runTimer;
    private int currentBar = 1;
    private int stopAtBar = 0;
    private BarEvent[] barEvents = new BarEvent[16];
    private long barLength = 0;
    private long sleepTime = 0;
    private long previousTime = 0;
    private long currentTime = 0;
    private long overTime = 0;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public TimelineService() {
        for (int i = 0; i < 16; i++) {
            barEvents[i] = new BarEvent(this, i + 1);
        }

    }

    @Async
    public void play() throws InterruptedException {
        if (!runTimer) {
            runTimer = true;
            while (runTimer) {
                applicationEventPublisher.publishEvent(barEvents[currentBar - 1]);
                if (shouldTimeLineEnd()) { //todo: fix ending so it actually stops
                    loadNextSection();
                    end = false;
                } else {
                    nextBar();
                }

                currentTime = clock.millis();
                if (previousTime != 0) {
                    overTime = currentTime - previousTime - barLength;
                }
                previousTime = currentTime;
                Thread.sleep(barLength - overTime);
            }
        }
    }

    //Todo: fix this
    private boolean shouldTimeLineEnd() {
        if (end || !currentSectionData.loop()) {
            switch (currentSectionData.transitionType()) {
                case END:
                    return currentBar == currentSectionData.numBars();
                case NOW:
                    return true;
                case NEXT_BAR:
                    if (stopAtBar != 0) {
                        if (currentBar == stopAtBar) {
                            stopAtBar = 0;
                            return true;
                        }
                    } else {
                        stopAtBar = currentBar + 1;
                        return false;
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

    private int nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentBar > currentSectionData.numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
        return currentBar;
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

}

