package com.timohenderson.RPGMusicServer.models.parts;

public abstract class Part {
    private int darkness = -1;
    private int intensity = -1;

    private String name = "Part";


    public int getDarkness() {
        return darkness;
    }

    public void setDarkness(int darkness) {
        this.darkness = darkness;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
