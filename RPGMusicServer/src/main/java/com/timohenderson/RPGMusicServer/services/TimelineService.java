package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class TimelineService {
    private TimelineParams currentParams;
    private TimelineParams nextParams;

    private volatile boolean goToNextParams;
    private volatile boolean runTimer;
    private int currentBar = 0;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Async
    public void timeLine() {
        if (!runTimer) {
            runTimer = true;
            while (runTimer) {
                //System.out.println(currentBar);
                BarEvent barEvent = new BarEvent(this, currentBar);
                applicationEventPublisher.publishEvent(barEvent);
                try {
                    Thread.sleep((long) currentParams.getBarLength());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                nextBar();
            }
        }
    }

    public void stopTimer() {
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
            currentParams = nextParams;
        }
    }

}

