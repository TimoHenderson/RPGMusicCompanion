package com.timohenderson.RPGMusicServer.models;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private List<Section> sections = new ArrayList<>();
    private String name;

    public Movement() {
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
}
