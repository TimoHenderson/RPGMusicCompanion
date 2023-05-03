package com.timohenderson.RPGMusicServer.models;

import com.timohenderson.RPGMusicServer.models.sections.Section;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private List<Section> sections = new ArrayList<>();

    private int currentSection = 0;
    private String name;

    public Movement() {
    }

    public Movement(String name) {
        this.name = name;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Section nextSection() {
        currentSection += 1;
        if (currentSection >= sections.size()) {
            currentSection = 0;
            return null;
        }
        return sections.get(currentSection);
    }

    public Section restartMovement() {
        currentSection = 0;
        return sections.get(currentSection);
    }

    public Section getCurrentSection() {
        return sections.get(currentSection);
    }
}
