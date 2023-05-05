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
        timelineService.createNextParams(sectionData);
        timelineService.play();
        Thread.sleep(20000l);
        timelineService.stop();
        eventCatcher.printLog();
        assertTrue(eventCatcher.getLog().size() > 0);
    }

    @Test
    void stopTimer() {
    }

    @Test
    void createNextParams() {
    }
}