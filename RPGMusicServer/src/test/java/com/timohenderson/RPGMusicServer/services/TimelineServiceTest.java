package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.TestingUtils.EventCatcher;
import com.timohenderson.RPGMusicServer.TestingUtils.EventWithDelta;
import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class TimelineServiceTest {
    @Autowired
    TimelineService timelineService;
    @Autowired
    EventCatcher eventCatcher;
    SectionData sectionData;
    SectionData loopingSectionData;

    @BeforeEach
    void setUp() {
        sectionData = new SectionData(1, 4, 2, 4, 150, false, false, TransitionType.END);
        loopingSectionData = new SectionData(1, 4, 2, 4, 120, true, true, TransitionType.NEXT_BAR);
    }

    @AfterEach
    void tearDown() {
        timelineService.stopAndCleanUp();
        eventCatcher.clearLog();
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
        timelineService.stop();
        assertTrue(eventCatcher.getLog().size() > numEvents);
    }

    @Test
    void goToNextSectionWhenNonLoopingSectionReachesEnd() throws InterruptedException {
        timelineService.addToSectionQueue(sectionData);
        SectionData slowSectionData = new SectionData(1, 8, 2, 4, 110, false, false, TransitionType.END);
        timelineService.addToSectionQueue(slowSectionData);
        timelineService.play();
        Thread.sleep(10000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertEquals(slowSectionData, timelineService.getCurrentSectionData());
    }

    @Test
    void goToNextSectionWhenLoopingSectionWithNextBarIsTriggered() throws InterruptedException {
        timelineService.addToSectionQueue(loopingSectionData);
        timelineService.addToSectionQueue(sectionData);
        timelineService.play();
        Thread.sleep(4500l);
        timelineService.triggerNextSection();
        EventWithDelta eventWithDelta = eventCatcher.getLastEvent();
        Thread.sleep(6000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertEquals(sectionData, timelineService.getCurrentSectionData());
        assertTrue(eventWithDelta.event().getBar() != 4);
        int changeIndex = eventWithDelta.eventNum() + 1;
        assertTrue(eventCatcher.getLog().get(changeIndex).event().getBar() == 1);
    }

    @Test
    void loopingSectionWillRunMultipleTimes() throws InterruptedException {
        timelineService.addToSectionQueue(loopingSectionData);
        timelineService.play();
        Thread.sleep(8000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertTrue(eventCatcher.getLog().size() > 4);
    }

    @Test
    void setCurrentParams() {
    }
}