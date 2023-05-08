package com.timohenderson.RPGMusicServer.models.parts;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;

import java.net.URL;

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

    @Override
    public URL getURL(int bar) {
        if (bar == 1) return museme.getURL();
        else return null;
    }
}
