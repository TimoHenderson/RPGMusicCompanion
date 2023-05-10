package com.timohenderson.RPGMusicServer;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import org.springframework.stereotype.Service;

@Service
public class GameState {
    private double darkness = 2.5;
    private double intensity = 2.5;
    private boolean isPlaying = false;
    private Tune currentTune = null;
    private Tune nextTune = null;
    private Movement currentMovement = null;
    private Movement nextMovement = null;


}
