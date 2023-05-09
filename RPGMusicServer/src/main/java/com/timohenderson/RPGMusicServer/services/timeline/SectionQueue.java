package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.models.sections.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionQueue {
    private ArrayList<Section> sectionQueue = new ArrayList();
    private int sectionIndex = 0;

    boolean isOnLastSection() {
        return sectionIndex == sectionQueue.size();
    }

    boolean isEmpty() {
        return sectionQueue.isEmpty();
    }


    Section getSection() {
        if (sectionIndex < sectionQueue.size()) {
            Section section = sectionQueue.get(sectionIndex);
            sectionIndex++;
            return section;
        }
        return null;
    }

    void clear() {
        sectionQueue.clear();
        sectionIndex = 0;
    }

    void addToQueue(ArrayList<Section> sections) {
        sectionQueue.addAll(sections);
    }

    void addSection(Section section) {
        sectionQueue.add(section);
    }

    List<Section> replaceQueueAndGetOld(List<Section> sections) {
        List<Section> oldSections = new ArrayList<>(sectionQueue);
        sectionQueue.clear();
        sectionQueue.addAll(sections);
        sectionIndex = 0;
        return oldSections;
    }

    void replaceQueue(List<Section> sections) {
        sectionQueue.clear();
        sectionQueue.addAll(sections);
        sectionIndex = 0;
    }

    public void putAtStart(List<Section> sections) {
        sectionQueue.addAll(0, sections);
        sectionIndex = 0;
    }
}
