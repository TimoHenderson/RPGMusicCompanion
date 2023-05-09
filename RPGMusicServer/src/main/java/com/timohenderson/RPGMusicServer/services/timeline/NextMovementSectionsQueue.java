package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.sections.Section;

import java.util.ArrayList;
import java.util.List;

public class NextMovementSectionsQueue {
    private ArrayList<Section> nextMovementSectionsQueue = new ArrayList();

    boolean isEmpty() {
        return nextMovementSectionsQueue.isEmpty();
    }

    ArrayList<Section> popAllSections() {
        ArrayList<Section> sections = new ArrayList<>(nextMovementSectionsQueue);
        nextMovementSectionsQueue.clear();
        return sections;
    }

    void add(List<Section> sections) {
        nextMovementSectionsQueue.addAll(sections);
    }

    void add(Movement movement) {
        nextMovementSectionsQueue.addAll(movement.getSections());
    }

    void addToStart(List<Section> sections) {
        nextMovementSectionsQueue.addAll(0, sections);
    }
}
