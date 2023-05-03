package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdaptiveSection extends Section {
    private List<Part> melodyParts = new ArrayList<>();
    private List<Part> padParts = new ArrayList<>();
    private List<Part> bassParts = new ArrayList<>();
    private List<Part> highRhythmParts = new ArrayList<>();
    private List<Part> midRhythmParts = new ArrayList<>();
    private List<Part> lowRhythmParts = new ArrayList<>();
    private List<Part> cymbalRhythmParts = new ArrayList<>();

    public AdaptiveSection() {
    }

    public AdaptiveSection(String name, SectionData sectionData) {
        super(name, sectionData);
    }

    public List<Part> getMelodyParts() {
        return melodyParts;
    }

    public void setMelodyParts(List<Part> melodyParts) {
        this.melodyParts = melodyParts;
    }

    public List<Part> getPadParts() {
        return padParts;
    }

    public void setPadParts(List<Part> padParts) {
        this.padParts = padParts;
    }

    public List<Part> getBassParts() {
        return bassParts;
    }

    public void setBassParts(List<Part> bassParts) {
        this.bassParts = bassParts;
    }

    public List<Part> getHighRhythmParts() {
        return highRhythmParts;
    }

    public void setHighRhythmParts(List<Part> highRhythmParts) {
        this.highRhythmParts = highRhythmParts;
    }

    public List<Part> getMidRhythmParts() {
        return midRhythmParts;
    }

    public void setMidRhythmParts(List<Part> midRhythmParts) {
        this.midRhythmParts = midRhythmParts;
    }

    public List<Part> getLowRhythmParts() {
        return lowRhythmParts;
    }

    public void setLowRhythmParts(List<Part> lowRhythmParts) {
        this.lowRhythmParts = lowRhythmParts;
    }

    public List<Part> getCymbalRhythmParts() {
        return cymbalRhythmParts;
    }

    public void setCymbalRhythmParts(List<Part> cymbalRhythmParts) {
        this.cymbalRhythmParts = cymbalRhythmParts;
    }

    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }
}
