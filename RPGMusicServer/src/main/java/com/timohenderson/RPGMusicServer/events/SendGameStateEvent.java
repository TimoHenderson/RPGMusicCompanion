package com.timohenderson.RPGMusicServer.events;

public class SendGameStateEvent extends Event {

    public SendGameStateEvent(Object source, Object payload) {
        super(source, payload);
    }

    public String getState() {
        return (String) getPayload();
    }
}
