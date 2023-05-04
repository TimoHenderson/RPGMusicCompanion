package com.timohenderson.RPGMusicServer.models.parts;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;

public class LinearPart extends Part {
    private Museme museme;

    public Museme getMuseme() {
        return museme;
    }

    public void setMuseme(Museme museme) {
        this.museme = museme;
    }
}
