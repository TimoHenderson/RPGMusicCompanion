package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import lombok.Getter;
import lombok.Setter;

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


    public void loadTuneNow(Tune tune) {
        movements.replace(tune.getMovements());
        replaceSectionQueueStoreOld(movements.getMovement());
        // replaceSectionQueue();
        prevTune = currentTune;
        currentTune = tune;
    }

    public void loadTune(Tune tune) {
        currentTune = tune;
        prevTune = null;
        movements.replace(tune.getMovements());
        movementSections.add(movements.getMovement().getSections());
    }

    public boolean isSectionQueueEmpty() {
        return sectionQueue.isEmpty();
    }

    public void fillNextMovementSectionQueue() {
        Movement movement = movements.getMovement();
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

    public boolean isOnLastSection() {
        return sectionQueue.isOnLastSection();
    }

    public boolean isMovementSectionsEmpty() {
        return movementSections.isEmpty();
    }


    //applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    public Section getNextSection() {
        return sectionQueue.getSection();
    }


}