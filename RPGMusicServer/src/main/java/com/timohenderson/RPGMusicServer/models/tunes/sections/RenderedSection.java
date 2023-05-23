package com.timohenderson.RPGMusicServer.models.tunes.sections;

import com.timohenderson.RPGMusicServer.models.tunes.parts.Part;
import com.timohenderson.RPGMusicServer.models.tunes.parts.PartData;
import org.javatuples.Pair;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class RenderedSection extends Section {

    Part part;


    public RenderedSection(String name, SectionData sectionData, Part part) {
        super(name, sectionData);
        this.part = part;
    }


    @Override
    public List<Pair<PartData, URL>> getNextMusemeURLs(int currentBar, double darkness, double intensity) {
        nextMusemeURLs.clear();
        Pair<PartData, URL> url = part.getURL(nextBarNum(currentBar));
        if (url != null) nextMusemeURLs.add(url);
        return nextMusemeURLs;
    }

    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }
}
