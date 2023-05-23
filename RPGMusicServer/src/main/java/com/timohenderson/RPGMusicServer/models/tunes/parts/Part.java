package com.timohenderson.RPGMusicServer.models.tunes.parts;

import org.javatuples.Pair;

import java.net.URL;

public abstract class Part {
    PartData partData;

    public Part(PartData partData) {
        this.partData = partData;
    }

    public PartData getPartData() {
        return partData;
    }

    public abstract Pair<PartData, URL> getURL(int bar);
}
