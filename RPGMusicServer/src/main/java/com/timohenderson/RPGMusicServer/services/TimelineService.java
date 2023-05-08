package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class TimelineService {
    private final LinkedBlockingQueue<Section> sectionQueue = new LinkedBlockingQueue<Section>();
    private final LinkedBlockingQueue<Section> nextMovementSectionQueue = new LinkedBlockingQueue<>();
    private final BarEvent[] barEvents = new BarEvent[17];
    // Clock clock = Clock.systemUTC();
    //  Timer timer = new Timer();
    TimeLoop timeLoop;

    private long barLength = 0;
    private Section currentSection;
    private boolean end = false;
    private volatile boolean runTimer;
    private int currentBar = 1;
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
        sectionQueue.put(section);
        System.out.println("Section queue size: " + sectionQueue.size());
        if (currentSection == null) {
            System.out.println("current section is null");
            loadNextSection();
//            audioPlayerService.loadSection(section);
        }
    }

    @Async
    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        loadNextSection();
    }

    public void loadMovement(Movement movement) throws LineUnavailableException, InterruptedException {
        System.out.println(movement.getSections());
        nextMovementSectionQueue.addAll(movement.getSections());
        if (sectionQueue.isEmpty()) {
            loadNextMovement();
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
            int size = nextMovementSectionQueue.size();
            for (int i = 0; i < size; i++) {
                System.out.println("Loading movement: " + i);
                addToSectionQueue(nextMovementSectionQueue.poll());
                System.out.println("Movement queue size: " + nextMovementSectionQueue.size());
            }
            nextMovementSectionQueue.clear();
            // loadNextSection();

            System.out.println("before play");
            play();
            System.out.println("after play");
        }
    }


    public void loadNextSection() throws InterruptedException, LineUnavailableException {
        if (sectionQueue.isEmpty()) {
            stopAndCleanUp();
            System.out.println("No more sections to play1");
            currentSection = null;
            loadNextMovement();
            runTimer = false;
            // play();
            return;
        } else {
            System.out.println("Loading next section else");
            currentSection = sectionQueue.take();
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




