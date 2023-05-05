package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.TransportAction;
import org.springframework.context.ApplicationEvent;

public class TransportEvent extends ApplicationEvent {
    private TransportAction action;

    public TransportEvent(Object source, TransportAction action) {
        super(source);
        this.action = action;
    }

    public TransportAction getAction() {
        return action;
    }
}
