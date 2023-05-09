package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.ParamType;

import java.util.HashMap;

public class GameParamsEvent extends Event {
    public GameParamsEvent(Object source, Object payload) {
        super(source, payload);
    }

    public HashMap<ParamType, Integer> getParams() {
        return (HashMap<ParamType, Integer>) super.getPayload();
    }
}
