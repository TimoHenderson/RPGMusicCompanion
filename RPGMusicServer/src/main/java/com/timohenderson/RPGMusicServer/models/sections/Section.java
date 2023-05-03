package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.HashMap;
import java.util.List;

public abstract class Section {


    private int numBars;
    private int numBeats;
    private int bpm;
    private int beatValue;
    private boolean isLoop;
    private String name;

    public int getNumBars() {
        return numBars;
    }

    public void setNumBars(int numBars) {
        this.numBars = numBars;
    }

    public int getNumBeats() {
        return numBeats;
    }

    public void setNumBeats(int numBeats) {
        this.numBeats = numBeats;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getBeatValue() {
        return beatValue;
    }

    public void setBeatValue(int beatValue) {
        this.beatValue = beatValue;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract List<Part> getNextParts(HashMap<String, Integer> partsNeeded);


}
