package com.timohenderson.RPGMusicServer.models.parts;

public abstract class Part {
    private PartData partData;
    
    public Part(PartData partData) {
        this.partData = partData;
    }

    public PartData getPartData() {
        return partData;
    }

    public void setPartData(PartData partData) {
        this.partData = partData;
    }
}
