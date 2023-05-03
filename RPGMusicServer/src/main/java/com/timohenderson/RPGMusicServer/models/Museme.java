package com.timohenderson.RPGMusicServer.models;

import java.util.ArrayList;

public class Museme {
    private String fileName;
    private int darkness = -1;
    private int intensity = -1;

    private ArrayList<Integer> startBars = new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public ArrayList<Integer> getStartBars() {
        return startBars;
    }

    public void setStartBars(ArrayList<Integer> startBars) {
        this.startBars = startBars;
    }
}
