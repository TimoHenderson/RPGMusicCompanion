package com.timohenderson.RPGMusicServer.models;

import java.util.ArrayList;
import java.util.List;

public class Part {
    private List<Museme> musemes = new ArrayList<>();

    public List<Museme> getMusemes() {
        return musemes;
    }

    public void setMusemes(List<Museme> musemes) {
        this.musemes = musemes;
    }
}
