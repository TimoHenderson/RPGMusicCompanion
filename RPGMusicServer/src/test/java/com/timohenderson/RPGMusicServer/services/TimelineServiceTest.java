package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.TestingUtils.EventCatcher;
import com.timohenderson.RPGMusicServer.TestingUtils.EventWithDelta;
import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import com.timohenderson.RPGMusicServer.services.timeline.TimelineService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class TimelineServiceTest {
    @Autowired
    TimelineService timelineService;
    @Autowired
    EventCatcher eventCatcher;
    Section section;
    Section loopingSection;
    Section slowSection;

    @BeforeEach
    void setUp() {
        section = new RenderedSection("section",
                new SectionData(
                        1,
                        4,
                        2,
                        4,
                        150,
                        false,
                        false,
                        TransitionType.END),
                null);
        loopingSection = new RenderedSection(
                "loopingSection",
                new SectionData(1,
                        4,
                        2,
                        4,
                        120,
                        true,
                        true,
                        TransitionType.NEXT_BAR),
                null);
        SectionData slowSectionData = new SectionData(1, 8, 4, 4, 110, false, false, TransitionType.END);
        slowSection = new AdaptiveSection("slowSection",
                new SectionData(
                        1,
                        8,
                        2,
                        4,
                        110,
                        false,
                        false,
                        TransitionType.END),
                null);
    }

    @AfterEach
    void tearDown() throws LineUnavailableException {
        timelineService.stopAndCleanUp();
        eventCatcher.clearLog();
    }

    @Test
    void canPlay() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(section);
        timelineService.play();
        Thread.sleep(3200);
        timelineService.stop();
        eventCatcher.printLog();
        assertTrue(eventCatcher.getLog().size() > 0);
    }

    @Test
    void canStopTimer() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(section);
        timelineService.play();
        Thread.sleep(2000l);
        timelineService.stop();
        int numEvents = eventCatcher.getLog().size();
        Thread.sleep(5000l);
        assertTrue(eventCatcher.getLog().size() == numEvents);
    }

    @Test
    void canResume() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(loopingSection);
        timelineService.play();
        Thread.sleep(5000l);
        timelineService.stop();
        System.out.println("Stop");
        Thread.sleep(2000l);
        int numEvents = eventCatcher.getLog().size();
        timelineService.play();
        Thread.sleep(5000l);
        timelineService.stop();
        assertTrue(eventCatcher.getLog().size() > numEvents);
    }

    @Test
    void goToNextSectionWhenNonLoopingSectionReachesEnd() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(section);
        timelineService.addToSectionQueue(loopingSection);
        timelineService.play();
        Thread.sleep(10000l);
        timelineService.stop();
//        eventCatcher.printLog();
        assertEquals(loopingSection, timelineService.getCurrentSection());
    }

    @Test
    void goToNextSectionWhenLoopingSectionWithNextBarIsTriggered() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(loopingSection);
        timelineService.addToSectionQueue(section);
        timelineService.play();
        Thread.sleep(2000l);
        timelineService.triggerNextSection();
        EventWithDelta eventWithDelta = eventCatcher.getLastEvent();
//        System.out.println(eventWithDelta);
        Thread.sleep(1600l);
        timelineService.stop();
//        eventCatcher.printLog();
        assertEquals(section, timelineService.getCurrentSection());
        assertTrue(eventWithDelta.event().getBar() != 4);
//        int changeIndex = eventWithDelta.eventNum() + 1;
//        assertTrue(eventCatcher.getLog().get(changeIndex).event().getBar() == 1);
    }

    @Test
    void loopingSectionWillRunMultipleTimes() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(loopingSection);
        timelineService.play();
        Thread.sleep(8000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertTrue(eventCatcher.getLog().size() > 4);
    }

    @Test
    void testTimer() throws InterruptedException {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer task started at:" + System.currentTimeMillis());
                //do a task that takes time but doesn't use thread sleep
                int i = 0;
                while (i < 100000) {
                    i++;
                }
                System.out.println("Timer task finished at:" + System.currentTimeMillis());
            }
        };
        new Timer().scheduleAtFixedRate(timerTask, 0, 1000);
        Thread.sleep(4000l);
    }

    @Test
    void setCurrentParams() {
    }
}