package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.Part;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import org.javatuples.Pair;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdaptiveSection extends Section {
    HashMap<MusicalType, List<AdaptivePart>> partListsMap;
//Todo: Add functionality to select parts based on darkness and intensity

    public AdaptiveSection(String name, SectionData sectionData, HashMap<MusicalType, List<AdaptivePart>> partListsMap) {
        super(name, sectionData);
        this.partListsMap = partListsMap;
    }

    @Override
    public List<Pair<PartData, URL>> getNextMusemeURLs(int currentBar, double darkness, double intensity) {
        nextMusemeURLs.clear();
        for (MusicalType musicalType : partListsMap.keySet()) {
            List<AdaptivePart> potentialParts = getPotentialParts(partListsMap.get(musicalType), darkness, intensity);
            nextMusemeURLs.addAll(getUrls(potentialParts, currentBar));
        }
        return nextMusemeURLs;
    }

    List<Pair<PartData, URL>> getUrls(List<AdaptivePart> potentialParts, int currentBar) {
        return potentialParts.stream()
                .map(adaptivePart -> adaptivePart.getURL(nextBarNum(currentBar)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<AdaptivePart> getPotentialParts(List<AdaptivePart> allTypeParts, double darkness, double intensity) {
        return allTypeParts.stream()
                .filter(adaptivePart -> {
                    PartData partData = adaptivePart.getPartData();
                    return partData.darkness() <= darkness && partData.intensity() <= intensity;
                })
                .collect(Collectors.toList());
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
