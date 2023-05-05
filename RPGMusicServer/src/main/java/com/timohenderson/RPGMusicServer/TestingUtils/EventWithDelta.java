package com.timohenderson.RPGMusicServer.TestingUtils;

import com.timohenderson.RPGMusicServer.events.BarEvent;

public record EventWithDelta(int eventNum, BarEvent event, long delta) {
    public String toString() {
        return eventNum + ":- BarEvent: " + event.getBar() + " Delta: " + delta;
    }
}
