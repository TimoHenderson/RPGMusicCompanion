package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;

@SpringBootTest
class TimelineServiceTest {
    @Autowired
    TimelineService timelineService;
    @Autowired
    EventCatcher eventCatcher;
    SectionData sectionData;

    @BeforeEach
    void setUp() {
        sectionData = new SectionData(1, 4, 2, 4, 150, false, false, TransitionType.END);
    }

    @Test
    void canPlay() throws InterruptedException {
        timelineService.addToSectionQueue(sectionData);
        timelineService.play();
        Thread.sleep(20000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertTrue(eventCatcher.getLog().size() > 0);
    }

    @Test
    void canStopTimer() throws InterruptedException {
        timelineService.addToSectionQueue(sectionData);
        timelineService.play();
        Thread.sleep(5000l);
        timelineService.stop();
        int numEvents = eventCatcher.getLog().size();
        Thread.sleep(5000l);
        assertTrue(eventCatcher.getLog().size() == numEvents);
    }

    @Test
    void canResume() throws InterruptedException {
        timelineService.addToSectionQueue(sectionData);
        timelineService.play();
        Thread.sleep(5000l);
        timelineService.stop();
        int numEvents = eventCatcher.getLog().size();
        timelineService.play();
        Thread.sleep(5000l);
        assertTrue(eventCatcher.getLog().size() > numEvents);
    }

    @Test
    void goToNextParams() throws InterruptedException {
        timelineService.addToSectionQueue(sectionData);
        SectionData slowSectionData = new SectionData(1, 8, 2, 4, 110, false, false, TransitionType.END);
        timelineService.addToSectionQueue(slowSectionData);
        timelineService.play();
        Thread.sleep(10000l);
        //timelineService.triggerNextSection();
        Thread.sleep(5000l);
        timelineService.addToSectionQueue(new SectionData(1, 4, 2, 4, 150, false, true, TransitionType.NEXT_BAR));
        Thread.sleep(10000l);
        timelineService.end();
        Thread.sleep(5000l);
        eventCatcher.printLog();
    }

    @Test
    void end() {
    }


    @Test
    void setCurrentParams() {
    }
}