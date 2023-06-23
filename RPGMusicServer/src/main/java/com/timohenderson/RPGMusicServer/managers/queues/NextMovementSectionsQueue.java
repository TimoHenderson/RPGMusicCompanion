package com.timohenderson.RPGMusicServer.managers.queues;

import com.timohenderson.RPGMusicServer.models.tunes.Movement;
import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;

import java.util.ArrayList;
import java.util.List;

public class NextMovementSectionsQueue {
    private ArrayList<Section> nextMovementSectionsQueue = new ArrayList();

    public boolean isEmpty() {
        return nextMovementSectionsQueue.isEmpty();
    }

    public ArrayList<Section> popAllSections() {
        ArrayList<Section> sections = new ArrayList<>(nextMovementSectionsQueue);
        nextMovementSectionsQueue.clear();
        return sections;
    }

    public void add(List<Section> sections) {
        nextMovementSectionsQueue.addAll(sections);
    }

    public void add(Movement movement) {
        nextMovementSectionsQueue.addAll(movement.getSections());
    }

    public void addToStart(List<Section> sections) {
        nextMovementSectionsQueue.addAll(0, sections);
    }
}
