package com.timohenderson.RPGMusicServer.managers.queues;

import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionQueue {
    private ArrayList<Section> sectionQueue = new ArrayList();
    private int sectionIndex = 0;

    public boolean isOnLastSection() {
        return sectionIndex == sectionQueue.size();
    }

    public boolean isEmpty() {
        return sectionQueue.isEmpty();
    }


    public Section getSection() {
        if (sectionIndex < sectionQueue.size()) {
            Section section = sectionQueue.get(sectionIndex);
            sectionIndex++;
            return section;
        }
        return null;
    }

    public void clear() {
        sectionQueue.clear();
        sectionIndex = 0;
    }

    void addToQueue(ArrayList<Section> sections) {
        sectionQueue.addAll(sections);
    }

    public void addSection(Section section) {
        sectionQueue.add(section);
    }

    public List<Section> replaceQueueAndGetOld(List<Section> sections) {
        List<Section> oldSections = new ArrayList<>(sectionQueue);
        sectionQueue.clear();
        sectionQueue.addAll(sections);
        sectionIndex = 0;
        return oldSections;
    }

    public void replaceQueue(List<Section> sections) {
        sectionQueue.clear();
        sectionQueue.addAll(sections);
        sectionIndex = 0;
    }

    public void putAtStart(List<Section> sections) {
        sectionQueue.addAll(0, sections);
        sectionIndex = 0;
    }

    public String getNextSectionName() {
        if (sectionIndex < sectionQueue.size()) {
            return sectionQueue.get(sectionIndex).getName();
        }
        return null;
    }
}
