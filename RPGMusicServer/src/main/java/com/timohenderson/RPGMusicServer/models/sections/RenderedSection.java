package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

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
    public List<URL> getNextMusemeURLs(int currentBar) {
        nextMusemeURLs.clear();
        nextMusemeURLs.add(part.getURL(nextBarNum(currentBar)));
        return nextMusemeURLs;
    }

    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }
}
