package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.enums.ParamType;

import java.util.HashMap;
import java.util.Map;

public class GameParamsEvent extends Event {
    public GameParamsEvent(Object source, Object payload) {
        super(source, payload);
    }

    public HashMap<ParamType, Integer> getParams() {
        Map map = (Map) super.getPayload();
        HashMap<ParamType, Integer> params = new HashMap<>();
        params.put(ParamType.DARKNESS, (Integer) map.get("darkness"));
        params.put(ParamType.INTENSITY, (Integer) map.get("intensity"));
        return params;
    }
}
