package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.enums.NavigationType;
import com.timohenderson.RPGMusicServer.events.NavigationEvent;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.AudioPlayerService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;


@Service
public class TimelineService {

    private final int fadeBarsToWait = 3;
    TimeLoop timeLoop;
    @Setter
    private long barLength = 0;
    private boolean end = false;
    private int nextBarTransitionTriggered = 0;
    private int currentBar = 1;
    private Section currentSection;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private AudioPlayerService audioPlayer;

    @Autowired
    public TimelineService(AudioPlayerService audioPlayer) {
        this.audioPlayer = audioPlayer;
        timeLoop = new TimeLoop(this, audioPlayer);
    }


    public void stopAndRestart() throws LineUnavailableException {
        end = true;
        timeLoop.stopLoop();
        timeLoop.play(barLength);
    }


    @Async
    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        applicationEventPublisher.publishEvent(new NavigationEvent(this, NavigationType.NEXT_SECTION));
    }


    private void reset() {
        currentBar = 1;
    }

    public void stopAndCleanUp() throws LineUnavailableException {
        timeLoop.stopLoop();
        currentBar = 1;
        end = false;

    }

    public boolean play() throws LineUnavailableException {
        timeLoop.play(barLength);
        return true;
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
        end = true;
    }

    void nextBar() {
        currentBar++;
        boolean lastBarPlayed = currentSection != null && currentBar > currentSection.getSectionData().numBars();
        if (lastBarPlayed) {
            currentBar = 1;
        }
    }

    public int getCurrentBar() {
        return currentBar;
    }

    public void setCurrentSection(Section currentSection) throws LineUnavailableException {
        this.currentSection = currentSection;

        if (currentSection == null) {
            stopAndCleanUp();
            return;
        }
        reset();
        setBarLength((long)
                (60000.0 / currentSection.getSectionData().bpm())
                * currentSection.getSectionData().numBeats());
        audioPlayer.loadSection(currentSection);
        //play();
    }
}




