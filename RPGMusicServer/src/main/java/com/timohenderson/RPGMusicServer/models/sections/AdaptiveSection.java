package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptiveSection extends Section {
    HashMap<MusicalType, List<AdaptivePart>> partListsMap;


    public AdaptiveSection(String name, SectionData sectionData, HashMap<MusicalType, List<AdaptivePart>> partListsMap) {
        super(name, sectionData);
        this.partListsMap = partListsMap;
    }

    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }

    public Map<MusicalType, List<AdaptivePart>> getPartLists() {
        return Collections.unmodifiableMap(partListsMap);
    }
}
