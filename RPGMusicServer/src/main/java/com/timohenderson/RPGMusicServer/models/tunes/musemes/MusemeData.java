package com.timohenderson.RPGMusicServer.models.tunes.musemes;

import java.util.List;

public record MusemeData(String name, int length, List<Integer> startBars) {
    @Override
    public List<Integer> startBars() {
        return List.copyOf(startBars);
    }
}
