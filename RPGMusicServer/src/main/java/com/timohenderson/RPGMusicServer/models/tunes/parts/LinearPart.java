package com.timohenderson.RPGMusicServer.models.tunes.parts;


import com.timohenderson.RPGMusicServer.models.tunes.musemes.Museme;
import org.javatuples.Pair;

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
    public Pair<PartData, URL> getURL(int bar) {
        if (bar == 1) {
            return new Pair<PartData, URL>(this.partData, museme.getURL());

        } else return null;
    }
}
