package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.enums.ParamType;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GameState {
    @Getter
    private double darkness = 2.5;
    @Getter
    private double intensity = 2.5;
    private boolean isPlaying = false;
    private Tune currentTune = null;
    private Tune nextTune = null;
    private Movement currentMovement = null;
    private Movement nextMovement = null;
    @Getter
    private MovementQueue movements = new MovementQueue();
    @Getter
    private NextMovementSectionsQueue movementSections = new NextMovementSectionsQueue();
    @Getter
    private SectionQueue sectionQueue = new SectionQueue();

    public void setGameParams(HashMap<ParamType, Double> params) {
        if (params.containsKey(ParamType.DARKNESS)) {
            this.darkness = params.get(ParamType.DARKNESS);
        }
        if (params.containsKey(ParamType.INTENSITY)) {
            this.intensity = params.get(ParamType.INTENSITY);
        }
    }


}
