package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;


@Service
public class TimelineService {
    Clock clock = Clock.systemUTC();
    private TimelineParams currentParams;
    private TimelineParams nextParams;
    private volatile boolean end;
    private volatile boolean goToNextParams;
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
                if (shouldTimeLineStop()) {
                    runTimer = false;
                    end = false;
                } else {
                    nextBar();
                }
                applicationEventPublisher.publishEvent(barEvents[currentBar - 1]);
                currentTime = clock.millis();

                if (previousTime != 0) {
                    overTime = currentTime - previousTime - barLength;
                }
                previousTime = currentTime;


                Thread.sleep(barLength - overTime);

            }
        }
    }

    private boolean shouldTimeLineStop() {
        if (end) {
            switch (currentParams.getTransitionType()) {
                case END:
                    return currentBar == currentParams.getNumBars();
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

    private int nextBar() {
        currentBar++;
        if (currentBar > currentParams.getNumBars()) {
            currentBar = 1;
        }
        return currentBar;
    }

    public void createNextParams(SectionData sectionData) {
        nextParams = new TimelineParams(sectionData);
        if (currentParams == null) {
            setCurrentParams(nextParams);
        }
    }

    public void setCurrentParams(TimelineParams currentParams) {
        this.currentParams = currentParams;
        this.barLength = (long) currentParams.getBarLength();
    }

}

