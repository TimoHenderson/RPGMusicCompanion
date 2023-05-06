package com.timohenderson.RPGMusicServer.TestingUtils;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.TransportEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.ArrayList;

@Component
public class EventCatcher {
    ArrayList<BarEvent> barEvents = new ArrayList<>();
    volatile ArrayList<EventWithDelta> log = new ArrayList<>();
    EventWithDelta lastEvent;
    Clock clock = Clock.systemUTC();
    int eventNum = 0;
    long previousTime = 0;

    @EventListener
    public void handleTransportEvent(TransportEvent event) {
        //System.out.println("TransportEvent: " + event.getAction());
    }

    @Async
    @EventListener
    public void handleBarEvent(BarEvent event) {
        long now = clock.millis();
        lastEvent = new EventWithDelta(eventNum, event, getTimeDelta(now));
        System.out.println(lastEvent);
        log.add(lastEvent);
        eventNum++;
        //barEvents.add(event);
    }

    private long getTimeDelta(long now) {
        if (previousTime == 0) {
            previousTime = clock.millis();
            return 0;
        } else {
            long delta = now - previousTime;
            previousTime = now;
            return delta;
        }
    }

    public ArrayList<BarEvent> getBarEvents() {
        return barEvents;
    }

    public ArrayList<EventWithDelta> getLog() {
        return log;
    }

    public void printLog() {
        for (EventWithDelta s : log) {
            System.out.println(s);
        }
        //ignore first in log then get the average of the rest
//        long sum = 0;
//        for (int i = 1; i < log.size(); i++) {
//            sum += log.get(i);
//        }
//        System.out.println("Average: " + sum / (log.size() - 1));
    }

    public EventWithDelta getLastEvent() {
        return lastEvent;
    }

    public void clearLog() {
        log.clear();
        eventNum = 0;
        lastEvent = null;
        previousTime = 0;
    }


}
