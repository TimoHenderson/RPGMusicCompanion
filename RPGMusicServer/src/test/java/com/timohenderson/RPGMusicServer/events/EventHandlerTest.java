package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.services.timeline.TimelineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventHandlerTest {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    EventHandler eventHandler;
    @Autowired
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        eventHandler.clearLog();
    }

    @Test
    void canLogEvents() {
        applicationEventPublisher.publishEvent(new BarEvent(this, 1));
        applicationEventPublisher.publishEvent(new BarEvent(this, 2));
        applicationEventPublisher.publishEvent(new BarEvent(this, 3));
        ArrayList<String> log = eventHandler.getLog();
        eventHandler.printLog();
        assertEquals(3, log.size());
    }

//    @Test
//    void canPlayAndStopTimeline() throws InterruptedException, LineUnavailableException {
//        timelineService.addToSectionQueue(
//                new RenderedSection(
//                        "Hello",
//                        new SectionData(1,
//                                4,
//                                2,
//                                4,
//                                150,
//                                false,
//                                true,
//                                TransitionType.END),
//                        null));
//        applicationEventPublisher.publishEvent(new TransportEvent(this, TransportActionType.PLAY));
//        Thread.sleep(2000);
//        applicationEventPublisher.publishEvent(new TransportEvent(this, TransportActionType.STOP));
//        int numEvents = eventHandler.getLog().size();
//        eventHandler.printLog();
//        Thread.sleep(2000);
//        int numEventsAfter = eventHandler.getLog().size();
//        eventHandler.printLog();
//        assertTrue(numEvents > 1);
//        assertTrue(numEvents == numEventsAfter);
//
//
//    }

}