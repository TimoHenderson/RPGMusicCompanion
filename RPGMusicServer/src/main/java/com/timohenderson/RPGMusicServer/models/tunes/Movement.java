package com.timohenderson.RPGMusicServer.models.tunes;

import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;

import java.util.List;

public class Movement {
    private final List<Section> sections;
    private final int order;
    private final String name;
    private int currentSection = 0;

    public Movement(String name, int order, List<Section> sections) {
        this.name = name;
        this.order = order;
        this.sections = sections;
    }


    public List<Section> getSections() {
        return List.copyOf(sections);
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
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
