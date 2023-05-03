package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.HashMap;
import java.util.List;

public class RenderedSection extends Section {

    Part part;

    public RenderedSection() {
    }

    public RenderedSection(String name, SectionData sectionData) {
        super(name, sectionData);
    }
    
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public List<Part> getNextParts(HashMap<String, Integer> partsNeeded) {
        return null;
    }
}
