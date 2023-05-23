package com.timohenderson.RPGMusicServer.managers;

import com.timohenderson.RPGMusicServer.models.queues.MovementQueue;
import com.timohenderson.RPGMusicServer.models.queues.NextMovementSectionsQueue;
import com.timohenderson.RPGMusicServer.models.queues.SectionQueue;
import com.timohenderson.RPGMusicServer.models.tunes.Movement;
import com.timohenderson.RPGMusicServer.models.tunes.Tune;
import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;
import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    @Getter
    private Tune currentTune = null;
    @Getter
    private Tune prevTune = null;
    @Getter
    private MovementQueue movements = new MovementQueue();
    @Getter
    private NextMovementSectionsQueue movementSections = new NextMovementSectionsQueue();
    @Getter
    private SectionQueue sectionQueue = new SectionQueue();

    @Getter
    @Setter
    private Section currentSection = null;

    public Section loadTune(Tune tune) throws LineUnavailableException, InterruptedException {
        if (!tune.getName().equals("Combat")) {
            currentTune = tune;
            prevTune = null;
            movements.replace(tune.getMovements());
            movementSections.add(movements.getNextMovement().getSections());
            if (sectionQueue.isEmpty()) {
                loadNextMovement();
                fillNextMovementSectionQueue();
            }
        } else loadTuneNow(tune);

        return currentSection;
    }

    public Section loadTuneNow(Tune tune) throws LineUnavailableException, InterruptedException {
        movements.replace(tune.getMovements());
        replaceSectionQueueStoreOld(movements.getNextMovement());
        prevTune = currentTune;
        currentTune = tune;
        //loadNextSection();
        return currentSection;
    }

    public void loadNextMovement() throws LineUnavailableException, InterruptedException {
        if (movementSections.isEmpty()) {
            fillNextMovementSectionQueue();
        }
        if (movementSections.isEmpty()) {
            loadPrevTune();
            return;
        }
        replaceSectionQueue();
        updateCurrentSection();
    }

    public void updateCurrentSection() throws LineUnavailableException, InterruptedException {
        if (currentSection == null)
            loadNextSection();
    }

    public void loadPrevTune() throws LineUnavailableException, InterruptedException {
        if (prevTune == null) {
            System.out.println("No more tunes to play");
            return;
        }
        loadTune(prevTune);
    }

    public Section loadNextSection() throws InterruptedException, LineUnavailableException {
        if (sectionQueue.isOnLastSection()) {
            setCurrentSection(null);
            loadNextMovement();
        } else {
            setCurrentSection(sectionQueue.getSection());
        }
        return currentSection;
    }

    public void fillNextMovementSectionQueue() {
        Movement movement = movements.getNextMovement();
        if (movement != null) movementSections.add(movement);
    }

    public void replaceSectionQueueStoreOld(Movement movement) {
        List<Section> oldSections = sectionQueue.replaceQueueAndGetOld(movement.getSections());
        movementSections.addToStart(oldSections);
    }

    public void replaceSectionQueue() {
        ArrayList<Section> sections = movementSections.popAllSections();
        sectionQueue.replaceQueue(sections);
    }


//    public String getCurrentTuneName() {
//        if (currentTune != null) return currentTune.getName();
//        return null;
//    }
//
//    public String getPrevTuneName() {
//        if (prevTune != null) return prevTune.getName();
//        return null;
//    }
//
//    public String getCurrentMovementName() {
//        return movements.getCurrentMovementName();
//    }
//
//    public String getNextMovementName() {
//        return movements.getNextMovementName();
//    }
//
//    public String getCurrentSectionName() {
//        if (currentSection != null) return currentSection.getName();
//        return null;
//    }
//
//    public String getNextSectionName() {
//        return sectionQueue.getNextSectionName();
//    }
}
