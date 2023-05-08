package com.timohenderson.RPGMusicServer.models.parts;

public abstract class Part {
    PartData partData;

    public Part(PartData partData) {
        this.partData = partData;
    }

    public PartData getPartData() {
        return partData;
    }
}
