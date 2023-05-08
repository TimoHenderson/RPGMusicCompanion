package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.enums.TransportAction;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import com.timohenderson.RPGMusicServer.services.TimelineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EventsHandlerTest {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    EventsHandler eventsHandler;
    @Autowired
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        eventsHandler.clearLog();
    }

    @Test
    void canLogEvents() {
        applicationEventPublisher.publishEvent(new BarEvent(this, 1));
        applicationEventPublisher.publishEvent(new BarEvent(this, 2));
        applicationEventPublisher.publishEvent(new BarEvent(this, 3));
        ArrayList<String> log = eventsHandler.getLog();
        eventsHandler.printLog();
        assertEquals(3, log.size());
    }

    @Test
    void canPlayAndStopTimeline() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(
                new RenderedSection(
                        "Hello",
                        new SectionData(1,
                                4,
                                2,
                                4,
                                150,
                                false,
                                true,
                                TransitionType.END),
                        null));
        applicationEventPublisher.publishEvent(new TransportEvent(this, TransportAction.PLAY));
        Thread.sleep(2000);
        applicationEventPublisher.publishEvent(new TransportEvent(this, TransportAction.STOP));
        int numEvents = eventsHandler.getLog().size();
        eventsHandler.printLog();
        Thread.sleep(2000);
        int numEventsAfter = eventsHandler.getLog().size();
        eventsHandler.printLog();
        assertTrue(numEvents > 1);
        assertTrue(numEvents == numEventsAfter);


    }

}