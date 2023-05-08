package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.events.BarEvent;
import com.timohenderson.RPGMusicServer.events.SectionLoadedEvent;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class TimelineService {
    private final LinkedBlockingQueue<Section> sectionQueue = new LinkedBlockingQueue<Section>();
    private final LinkedBlockingQueue<Section> nextMovementSectionQueue = new LinkedBlockingQueue<>();
    private final BarEvent[] barEvents = new BarEvent[17];
    // Clock clock = Clock.systemUTC();
    //  Timer timer = new Timer();
    ScheduledExecutorService executorService;
    private Section currentSection;
    private boolean end = false;
    private volatile boolean runTimer;
    private int currentBar = 1;
    private long barLength = 0;
    private long previousTime = 0;
    private long overTime = 0;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AudioPlayerService audioPlayerService;


    public TimelineService() {
        for (int i = 0; i < 17; i++) {
            barEvents[i] = new BarEvent(this, i);
        }
    }


    private boolean shouldTimeLineEnd() {
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

    private void runExecutor() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new TimeLineLoop(), 0, barLength, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        runTimer = false;
        audioPlayerService.stop();
        executorService.shutdown();
    }

    public void stopLoop() {
        runTimer = false;
        executorService.shutdownNow();
    }

    public void resume() throws LineUnavailableException {
        runTimer = true;
        audioPlayerService.play();
        runExecutor();
    }

    public void end() {
        end = true;
    }

    public void triggerNextSection() {
        end = true;
    }

    private void nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentSection != null && currentBar > currentSection.getSectionData().numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
        System.out.println(currentBar);
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

    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        // end = false;
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
        this.barLength = (long) (60000.0 / currentSection.getSectionData().bpm()) * currentSection.getSectionData().numBeats();
        System.out.println("Loading section: " + currentSection.getName());
        if (!runTimer) {
            audioPlayerService.loadSection(currentSection);
        } else {
            audioPlayerService.loadNextSection(currentSection);
        }
        applicationEventPublisher.publishEvent(new SectionLoadedEvent(this, currentSection));
    }

    private void reset() {
        currentBar = 1;
    }

    public void stopAndCleanUp() {
        stopLoop();
        sectionQueue.clear();
        currentSection = null;
        currentBar = 1;
        previousTime = 0;
        overTime = 0;
        end = false;
        runTimer = false;
        barLength = 0;

    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void play() throws LineUnavailableException {
        if (!runTimer) {
            runTimer = true;
            runExecutor();
        }
    }


    private class TimeLineLoop implements Runnable {
        @Override
        public void run() {
            System.out.println("looping");
            try {
                audioPlayerService.playNextCues();
                System.out.println("playing next cues");
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            if (shouldTimeLineEnd()) {
                try {
                    loadNextSectionHandler();
                } catch (InterruptedException e) {

                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }

                end = false;
            } else {
                try {
                    audioPlayerService.setCurrentBar(currentBar);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                nextBar();
            }
//            long currentTime = clock.millis();
//            if (previousTime != 0) {
//                overTime = (currentTime - previousTime - barLength) / 2;
//            }
//            previousTime = currentTime;
//            System.out.println("Bar: " + currentBar + " OverTime: " + overTime + " BarLength: " + barLength);
            // Thread.sleep(barLength - overTime);
        }
    }

}


