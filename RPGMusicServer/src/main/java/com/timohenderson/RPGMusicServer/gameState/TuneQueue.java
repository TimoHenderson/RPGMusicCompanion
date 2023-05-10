package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.models.Tune;

import java.util.ArrayList;

public class TuneQueue {
    private ArrayList<Tune> tunes = new ArrayList();

    public boolean isEmpty() {
        return tunes.isEmpty();
    }
}
