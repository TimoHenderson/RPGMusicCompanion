package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.enums.ParamType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class GameParameters {
    @Getter
    @Setter
    private double darkness = 3.0;
    @Getter
    @Setter
    private double intensity = 3.0;

    public void setParams(HashMap<ParamType, Double> params) {
        if (params.containsKey(ParamType.DARKNESS)) {
            this.darkness = params.get(ParamType.DARKNESS);
        }
        if (params.containsKey(ParamType.INTENSITY)) {
            this.intensity = params.get(ParamType.INTENSITY);
        }
    }
}
