package com.timohenderson.RPGMusicServer.events;

import org.springframework.context.ApplicationEvent;

public class BarEvent extends ApplicationEvent {
    private int bar;

    public BarEvent(Object source, int bar) {
        super(source);

        this.bar = bar;
    }

    public int getBar() {
        return bar;
    }
}
