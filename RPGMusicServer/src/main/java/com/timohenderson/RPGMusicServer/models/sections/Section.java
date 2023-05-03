package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.HashMap;
import java.util.List;

public abstract class Section {

    private String name;
    private SectionData sectionData;

    public Section() {
    }

    public Section(String name, SectionData sectionData) {
        this.name = name;
        this.sectionData = sectionData;
    }

    public SectionData getSectionData() {
        return sectionData;
    }

    public abstract List<Part> getNextParts(HashMap<String, Integer> partsNeeded);


}
