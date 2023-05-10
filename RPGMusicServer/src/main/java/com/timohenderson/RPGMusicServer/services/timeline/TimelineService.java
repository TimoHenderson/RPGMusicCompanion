package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.gameState.GameState;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.AudioPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;


@Service
public class TimelineService {

    private final int fadeBarsToWait = 3;
    TimeLoop timeLoop;
    private long barLength = 0;
    private Section currentSection;
    private boolean end = false;
    private int nextBarTransitionTriggered = 0;
    private volatile boolean runTimer;
    private int currentBar = 1;


    @Autowired
    private GameState gs;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private AudioPlayerService audioPlayer;

    @Autowired
    public TimelineService(AudioPlayerService audioPlayer) {
        this.audioPlayer = audioPlayer;
        timeLoop = new TimeLoop(this, audioPlayer);
    }

    public void loadTune(Tune tune, boolean loadNow) throws LineUnavailableException, InterruptedException {
        if (loadNow) {
            gs.getMovements().replace(tune.getMovements());
            loadMovementNow(gs.getMovements().getMovement());
            return;
        }
        gs.getMovements().replace(tune.getMovements());
        loadMovement(gs.getMovements().getMovement());
    }

    private void fillNextMovementSectionQueue() {
        Movement movement = gs.getMovements().getMovement();
        if (movement != null) gs.getMovementSections().add(movement);
    }

    public void loadNextMovementNow() throws LineUnavailableException, InterruptedException {
        loadMovementNow(gs.getMovements().getMovement());
    }

    public void loadMovementNow(Movement movement) throws LineUnavailableException, InterruptedException {
        System.out.println("Loading movement now");
        end = true;
        List<Section> oldSections = gs.getSectionQueue().replaceQueueAndGetOld(movement.getSections());
        gs.getMovementSections().addToStart(oldSections);
        timeLoop.stopLoop();
        loadNextSection();
        audioPlayer.fadeCurrentCues();
        timeLoop.play(barLength);
    }

    public void loadMovement(Movement movement) throws LineUnavailableException, InterruptedException {
        gs.getMovementSections().add(movement.getSections());
        if (gs.getSectionQueue().isEmpty()) {
            loadNextMovement();
            fillNextMovementSectionQueue();
        }
    }

    public void addToSectionQueue(Section section) throws InterruptedException, LineUnavailableException {
        gs.getSectionQueue().addSection(section);
        updateCurrentSection();
    }

    private void updateCurrentSection() throws LineUnavailableException, InterruptedException {
        if (currentSection == null) {

            System.out.println("current section is null");
            loadNextSection();
        }
    }

    public void loadNextMovement() throws LineUnavailableException, InterruptedException {
        System.out.println("Loading next movement");
        if (gs.getMovementSections().isEmpty()) {
            System.out.println("No more gs.movements to play");
            stopAndCleanUp();
            return;
        }
        runTimer = false;
        ArrayList<Section> sections = gs.getMovementSections().popAllSections();
        gs.getSectionQueue().replaceQueue(sections);
        updateCurrentSection();
        play();
    }

    @Async
    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        loadNextSection();
    }

    public void loadNextSection() throws InterruptedException, LineUnavailableException {
        if (gs.getSectionQueue().isOnLastSection()) {
            stopAndCleanUp();
            System.out.println("No more sections to play1");
            currentSection = null;
            loadNextMovement();
            runTimer = false;
            end = false;
            return;
        }

        currentSection = gs.getSectionQueue().getSection();

        if (currentSection == null) {
            System.out.println("No more sections to play");
            stopAndCleanUp();
            return;
        }

        reset();
        barLength = ((long) (60000.0 / currentSection.getSectionData().bpm()) * currentSection.getSectionData().numBeats());
        audioPlayer.loadSection(currentSection);
        applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    }

    private void reset() {
        currentBar = 1;
    }

    public void stopAndCleanUp() throws LineUnavailableException {
        timeLoop.stopLoop();
        gs.getSectionQueue().clear();
        currentSection = null;
        currentBar = 1;
        end = false;
        runTimer = false;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void play() throws LineUnavailableException {
        if (currentSection == null) return;
        timeLoop.play(barLength);
    }

    public void stop() throws LineUnavailableException {
        timeLoop.stopLoop();
    }

    boolean shouldTimeLineEnd() throws LineUnavailableException {
        if (currentSection == null) return false;
        if (end || !currentSection.getSectionData().loop()) {
            switch (currentSection.getSectionData().transitionType()) {
                case END -> {
                    return currentBar == currentSection.getSectionData().numBars();
                }
                case NEXT_BAR -> {
                    if (nextBarTransitionTriggered == 0) {
                        audioPlayer.fadeCurrentCues();
                        timeLoop.setPlayCues(false);

                    }
                    if (nextBarTransitionTriggered < fadeBarsToWait) {
                        nextBarTransitionTriggered++;
                        return false;
                    } else {
                        nextBarTransitionTriggered = 0;
                        currentBar = -1;
                        timeLoop.setPlayCues(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void setEnd(boolean end) {
        this.end = end;
    }

    public void triggerNextSection() {
        System.out.println("Trigger next Section");
        end = true;
    }

    void nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentSection != null && currentBar > currentSection.getSectionData().numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
//        System.out.println(currentBar);
    }

    public int getCurrentBar() {
        return currentBar;
    }

}




