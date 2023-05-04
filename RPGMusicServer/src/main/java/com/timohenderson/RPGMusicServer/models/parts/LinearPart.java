package com.timohenderson.RPGMusicServer.models.parts;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;

public class LinearPart extends Part {
    private Museme museme;

    public LinearPart(PartData partData, Museme museme) {
        super(partData);
        this.museme = museme;
    }

    public Museme getMuseme() {
        return museme;
    }

    public void setMuseme(Museme museme) {
        this.museme = museme;
    }
}
