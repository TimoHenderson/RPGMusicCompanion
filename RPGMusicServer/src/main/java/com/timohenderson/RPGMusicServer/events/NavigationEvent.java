package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.NavigationType;

public class NavigationEvent extends Event {

    public NavigationEvent(Object source, Object payload) {
        super(source, payload);
    }

    public NavigationType getAction() {
        return (NavigationType) super.getPayload();
    }
}
