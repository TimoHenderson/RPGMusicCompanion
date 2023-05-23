package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.AudioPlayer;
import com.timohenderson.RPGMusicServer.services.ConductorService;
import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.LineUnavailableException;


public class Timeline {

    private final int fadeBarsToWait = 3;
    TimeLoop timeLoop;
    @Setter
    private long barLength = 0;
    private boolean end = false;
    private int nextBarTransitionTriggered = 0;
    private int currentBar = 1;
    private boolean immediateTransition = false;
    @Getter
    private Section currentSection;

    private AudioPlayer audioPlayer = new AudioPlayer();
    private ConductorService conductor;

    public Timeline(ConductorService conductor) throws LineUnavailableException {
        this.conductor = conductor;
        timeLoop = new TimeLoop(this, audioPlayer);
    }

    public Timeline() throws LineUnavailableException {
    }

    public boolean play() throws LineUnavailableException {
        if (currentSection == null) return false;
        timeLoop.play(barLength);
        return true;
    }

    public void stop() throws LineUnavailableException {
        timeLoop.stopLoop();
    }

    public void stopAndRestart() throws LineUnavailableException {
        timeLoop.stopLoop();
        timeLoop.play(barLength);
    }

    public void stopAndCleanUp() throws LineUnavailableException {
        timeLoop.stopLoop();
        currentBar = 1;
        end = false;
    }

    private void reset() {
        currentBar = 1;
    }

    //    @Async
    public void loadNextSectionHandler() throws LineUnavailableException, InterruptedException {
        System.out.println("loadNextSectionHandler");
        System.out.println("immediateTransition" + immediateTransition);
        if (immediateTransition) {
            immediateTransition = false;
        }
        conductor.loadNextSection();
    }

    boolean shouldTimeLineEnd() throws LineUnavailableException {
        if (currentSection == null) return false;
        if (end && immediateTransition) {
//            System.out.println("immediateTransition");
            audioPlayer.fadeCurrentCues();
            return true;
        }
        if (end || !currentSection.getSectionData().loop()) {
            switch (currentSection.getSectionData().transitionType()) {
                case END -> {
                    System.out.println("END " + currentBar + " " + currentSection.getSectionData().numBars());
                    return currentBar == currentSection.getSectionData().numBars();
                }
                case NEXT_BAR -> {
                    if (nextBarTransitionTriggered == 0) {
                        System.out.println("nextBarTransitionTriggered");
                        audioPlayer.fadeCurrentCues();
                        timeLoop.setPlayCues(false);
                    }
                    if (nextBarTransitionTriggered < fadeBarsToWait) {
                        System.out.println("nextBarTransitionTriggered<fadeBarsToWait");
                        nextBarTransitionTriggered++;
                        return false;
                    } else {
                        System.out.println("nextBarTransitionTriggered>=fadeBarsToWait");
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

    public void changeSectionNow(Section section) throws LineUnavailableException {
        end = true;
        immediateTransition = true;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void triggerNextSection() {
        end = true;
    }

    void nextBar() {
        System.out.println("currentBar: " + currentBar);
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
        System.out.println("setCurrentSection: " + currentSection.getName());
        reset();
        long previousBarLength = barLength;
        setBarLength((long)
                (60000.0 / currentSection.getSectionData().bpm())
                * currentSection.getSectionData().numBeats());
        audioPlayer.loadSection(currentSection);
        if (previousBarLength != barLength) {
            System.out.println("barLength changed:" + "barLength: " + barLength + " previousBarLength: " + previousBarLength);
            stopAndRestart();
        }
        // play();
    }
}




