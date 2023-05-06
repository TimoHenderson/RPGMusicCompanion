package com.timohenderson.RPGMusicServer.events;

public class LoadTuneEvent extends Event {
    public LoadTuneEvent(Object source, Object payload) {
        super(source, payload);
    }

    public String getTuneName() {
        return (String) super.getPayload();
    }
}
