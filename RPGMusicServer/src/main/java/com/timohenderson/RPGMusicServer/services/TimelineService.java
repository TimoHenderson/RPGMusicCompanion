package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;


@Service
public class TimelineService {
    private final ArrayList<Section> sectionQueue = new ArrayList();
    private final ArrayList<Section> nextMovementSectionQueue = new ArrayList();

    private final ArrayList<Movement> nextMovementQueue = new ArrayList();
    private final BarEvent[] barEvents = new BarEvent[17];
    // Clock clock = Clock.systemUTC();
    //  Timer timer = new Timer();
    TimeLoop timeLoop;

    private long barLength = 0;
    private Section currentSection;
    private boolean end = false;
    private volatile boolean runTimer;
    private int currentBar = 1;
    private int nextSectionIndex = 0;
    private int nextMovementIndex = 0;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private AudioPlayerService audioPlayer;

    @Autowired
    public TimelineService(AudioPlayerService audioPlayer) {
        this.audioPlayer = audioPlayer;
        timeLoop = new TimeLoop(this, audioPlayer);
    }


    boolean shouldTimeLineEnd() {
        if (currentSection == null) return false;
        if (end || !currentSection.getSectionData().loop()) {
            switch (currentSection.getSectionData().transitionType()) {
                case END -> {
                    return currentBar == currentSection.getSectionData().numBars();
                }
                case NEXT_BAR -> {
                    currentBar = 0;
                    return true;
                }
            }
        }
        return false;
    }


    public void setEnd(boolean end) {
        this.end = end;
    }

    public void triggerNextSection() {
        end = true;
    }

    void nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentSection != null && currentBar > currentSection.getSectionData().numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
        System.out.println(currentBar);
    }

    public int getCurrentBar() {
        return currentBar;
    }

    public void addToSectionQueue(Section section) throws InterruptedException, LineUnavailableException {
        System.out.println("Adding section to queue: " + section.getName());
        System.out.println("Section queue size: " + sectionQueue.size());
        sectionQueue.add(section);
        System.out.println("Section queue size: " + sectionQueue.size());
        if (currentSection == null) {
            System.out.println("current section is null");
            loadNextSection();
//            audioPlayerService.loadSection(section);
        }
    }

    public void loadTune(Tune tune) throws LineUnavailableException, InterruptedException {
        nextMovementQueue.addAll(tune.getMovements());
        nextMovementIndex = 0;
        loadMovement(nextMovementQueue.get(0));
        if (tune.getName().equals("Combat")) {
            loadMovementNow(nextMovementQueue.get(0));
            //loadNextMovementNow();
        }
    }

    public void goToNextMovement() {

    }

    public void loadNextMovementNow() throws LineUnavailableException, InterruptedException {
        loadMovementNow(nextMovementQueue.get(nextMovementIndex));
    }

    public void loadMovementNow(Movement movement) throws LineUnavailableException, InterruptedException {
        System.out.println("Loading movement now");
        end = true;
        nextSectionIndex = 0;
        int size = movement.getSections().size();
        for (int i = 0; i < size; i++) {
            System.out.println("Loading movement: " + i);
            nextMovementSectionQueue.add(movement.getSections().get(i));
            System.out.println("Movement queue size: " + sectionQueue.size());
        }
        loadNextMovement();
        timeLoop.stopLoop();
        loadNextSection();
        audioPlayer.fadeCurrentCues();
        timeLoop.play(barLength);
    }

    public void loadMovement(Movement movement) throws LineUnavailableException, InterruptedException {
        System.out.println(movement.getSections());
        nextMovementSectionQueue.addAll(movement.getSections());
        if (sectionQueue.isEmpty()) {
            loadNextMovement();
            fillNextMovementSectionQueue();
        }
    }

    private void fillNextMovementSectionQueue() {
        if (nextMovementIndex < nextMovementQueue.size()) {
            nextMovementSectionQueue.addAll(nextMovementQueue.get(nextMovementIndex).getSections());
            //nextMovementIndex++;
        }
    }

    public void loadNextMovement() throws LineUnavailableException, InterruptedException {
        System.out.println("Loading next movement");
        if (nextMovementSectionQueue.isEmpty()) {
            System.out.println("No more movements to play");
            stopAndCleanUp();
            return;
        } else {
            runTimer = false;
            System.out.println("Loading next movement else");
            sectionQueue.clear();
            nextSectionIndex = 0;
            int size = nextMovementSectionQueue.size();
            for (int i = 0; i < size; i++) {
                System.out.println("Loading movement: " + i);
                addToSectionQueue(nextMovementSectionQueue.get(i));
                System.out.println("Movement queue size: " + nextMovementSectionQueue.size());
            }
            nextMovementSectionQueue.clear();
            // loadNextSection();

            System.out.println("before play");
            play();
            System.out.println("after play");
        }
    }

    @Async
    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        loadNextSection();
    }

    public void loadNextSection() throws InterruptedException, LineUnavailableException {

        if (nextSectionIndex >= sectionQueue.size()) {
            stopAndCleanUp();
            System.out.println("No more sections to play1");
            currentSection = null;
            loadNextMovement();
            runTimer = false;
            // play();
            return;
        } else {

            currentSection = sectionQueue.get(nextSectionIndex);
            nextSectionIndex++;
        }
        if (currentSection == null) {
            System.out.println("No more sections to play");
            stopAndCleanUp();
            return;
        }
        reset();
        barLength = ((long) (60000.0 / currentSection.getSectionData().bpm()) * currentSection.getSectionData().numBeats());
        System.out.println("Loading section: " + currentSection.getName());
        if (!runTimer) {
            audioPlayer.loadSection(currentSection);
        } else {
            audioPlayer.loadNextSection(currentSection);
        }
        applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    }

    private void reset() {
        currentBar = 1;
    }

    public void stopAndCleanUp() {
        timeLoop.stopLoop();
        sectionQueue.clear();
        currentSection = null;
        currentBar = 1;
        end = false;
        runTimer = false;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void play() throws LineUnavailableException {
        timeLoop.play(barLength);
    }

    public void stop() {
        timeLoop.stopLoop();
    }


}




