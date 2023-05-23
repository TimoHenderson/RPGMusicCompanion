package com.timohenderson.RPGMusicServer.models.tunes.sections;

import com.timohenderson.RPGMusicServer.models.tunes.parts.Part;
import com.timohenderson.RPGMusicServer.models.tunes.parts.PartData;
import org.javatuples.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Section {

    List<Pair<PartData, URL>> nextMusemeURLs;
    String name;
    SectionData sectionData;

    public Section(String name, SectionData sectionData) {
        this.name = name;
        this.sectionData = sectionData;
        nextMusemeURLs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public SectionData getSectionData() {
        return sectionData;
    }

    public abstract List<Pair<PartData, URL>> getNextMusemeURLs(int currentBar, double darkness, double intensity);

    int nextBarNum(int currentBar) {
        int nextBarNum = currentBar + 1;
        if (nextBarNum > sectionData.numBars()) nextBarNum -= sectionData.numBars();
        return nextBarNum;
    }

    public abstract List<Part> getNextParts(HashMap<String, Integer> partsNeeded);

    public void reset() {
    }
}
