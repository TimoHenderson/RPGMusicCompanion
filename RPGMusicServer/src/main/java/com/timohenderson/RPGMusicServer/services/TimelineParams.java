package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;

public class TimelineParams {
    private int numBars;
    private double barLength;
    private TransitionType transitionType;

    public TimelineParams(SectionData sectionData) {
        this.numBars = sectionData.numBars();
        //calculate length of bar in ms from bpm and numBeats
        this.barLength = (60000.0 / sectionData.bpm()) * sectionData.numBeats();
        this.transitionType = sectionData.transitionType();
    }

    public int getNumBars() {
        return numBars;
    }

    public double getBarLength() {
        return barLength;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

}
