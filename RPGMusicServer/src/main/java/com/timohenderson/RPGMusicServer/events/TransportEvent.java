package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.TransportActionType;

public class TransportEvent extends Event {

    public TransportEvent(Object source, TransportActionType action) {
        super(source, action);

    }

    public TransportActionType getAction() {
        return (TransportActionType) super.getPayload();
    }
}
