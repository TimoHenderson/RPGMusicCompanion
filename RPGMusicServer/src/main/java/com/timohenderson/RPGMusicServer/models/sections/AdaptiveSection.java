package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.HashMap;
import java.util.List;

public class AdaptiveSection extends Section {
    private PartLists partLists;

    public AdaptiveSection() {
    }

    public AdaptiveSection(String name, SectionData sectionData) {
        super(name, sectionData);
        
    }


    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }

    public PartLists getPartLists() {
        return partLists;
    }

    public void setPartLists(PartLists partLists) {
        this.partLists = partLists;
    }
}
