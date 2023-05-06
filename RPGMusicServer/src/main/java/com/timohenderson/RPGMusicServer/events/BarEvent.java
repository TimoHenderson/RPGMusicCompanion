package com.timohenderson.RPGMusicServer.events;

public class BarEvent extends Event {
    //private Integer bar;


    public BarEvent(Object source, Integer bar) {
        super(source, bar);
    }

    public int getBar() {
        return (Integer) super.getPayload();
    }
}
