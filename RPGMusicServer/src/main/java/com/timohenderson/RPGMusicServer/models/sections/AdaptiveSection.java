package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.HashMap;
import java.util.List;

public class AdaptiveSection extends Section {
    private HashMap<MusicalType, List<Part>> partLists;


    public AdaptiveSection(String name, SectionData sectionData, HashMap<MusicalType, List<Part>> partLists) {
        super(name, sectionData);

        this.partLists = partLists;
    }


    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }

    public HashMap<MusicalType, List<Part>> getPartLists() {
        return partLists;
    }
}
