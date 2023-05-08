package com.timohenderson.RPGMusicServer.models.parts;

import java.net.URL;

public abstract class Part {
    PartData partData;

    public Part(PartData partData) {
        this.partData = partData;
    }

    public PartData getPartData() {
        return partData;
    }

    public abstract URL getURL(int bar);
}
