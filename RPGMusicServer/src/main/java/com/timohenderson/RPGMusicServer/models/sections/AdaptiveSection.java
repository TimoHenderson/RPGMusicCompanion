package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.Part;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import org.javatuples.Pair;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptiveSection extends Section {
    HashMap<MusicalType, List<AdaptivePart>> partListsMap;
//Todo: Add functionality to select parts based on darkness and intensity

    public AdaptiveSection(String name, SectionData sectionData, HashMap<MusicalType, List<AdaptivePart>> partListsMap) {
        super(name, sectionData);
        this.partListsMap = partListsMap;
    }

    @Override
    public List<Pair<PartData, URL>> getNextMusemeURLs(int currentBar) {
        nextMusemeURLs.clear();
        for (MusicalType musicalType : partListsMap.keySet()) {
            for (AdaptivePart adaptivePart : partListsMap.get(musicalType)) {
                Pair<PartData, URL> nextURL = adaptivePart.getURL(nextBarNum(currentBar));
                if (nextURL != null) {
                    nextMusemeURLs.add(nextURL);
                }
            }
        }
        return nextMusemeURLs;
    }


    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }

    public Map<MusicalType, List<AdaptivePart>> getPartLists() {
        return Collections.unmodifiableMap(partListsMap);
    }

    public void reset() {
        for (List<AdaptivePart> adaptiveParts : partListsMap.values()) {
            for (AdaptivePart adaptivePart : adaptiveParts) {
                adaptivePart.reset();
            }
        }
    }

}
