package com.timohenderson.RPGMusicServer.events;

import org.springframework.context.ApplicationEvent;

public abstract class Event extends ApplicationEvent {
    private Object payload;

    public Event(Object source, Object payload) {
        super(source);
        this.payload = payload;
    }

    public String toString() {
        return "Event: " + this.getClass().getSimpleName() + " Payload: " + payload.toString();
    }

    protected Object getPayload() {
        return payload;
    }
}
