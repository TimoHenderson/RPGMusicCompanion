package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.TransportAction;

public class TransportEvent extends Event {

    public TransportEvent(Object source, TransportAction action) {
        super(source, action);

    }

    public TransportAction getAction() {
        return (TransportAction) super.getPayload();
    }
}
