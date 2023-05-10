package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.enums.ParamType;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.AudioPlayerService;
import com.timohenderson.RPGMusicServer.services.timeline.TimelineService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.HashMap;

@Service
public class GameState {
    @Autowired
    AudioPlayerService audioPlayer;
    @Autowired
    private TimelineService timeline;
    @Getter
    private double darkness = 2.5;
    @Getter
    private double intensity = 2.5;
    @Getter
    private boolean isPlaying = false;
    private Tune currentTune = null;
    private Tune nextTune = null;
    private Movement currentMovement = null;
    private Movement nextMovement = null;
    private QueueManager qm = new QueueManager();


    public void setGameParams(HashMap<ParamType, Double> params) {
        if (params.containsKey(ParamType.DARKNESS)) {
            this.darkness = params.get(ParamType.DARKNESS);
            audioPlayer.setDarkness(this.darkness);
        }
        if (params.containsKey(ParamType.INTENSITY)) {
            this.intensity = params.get(ParamType.INTENSITY);
            audioPlayer.setIntensity(this.intensity);
        }
    }

    public void setCurrentSection(Section section) throws LineUnavailableException, InterruptedException {
        qm.setCurrentSection(section);
        timeline.setCurrentSection(section);
        //if (section != null) audioPlayer.loadSection(section);
    }

    public boolean setIsPlaying(boolean isPlaying) throws LineUnavailableException {
        if (isPlaying == true && qm.getCurrentSection() != null) {
            this.isPlaying = timeline.play();
        } else {
            timeline.stop();
            this.isPlaying = false;
        }
        return this.isPlaying;
    }

    public void loadNextSection() throws InterruptedException, LineUnavailableException {
        if (qm.isOnLastSection()) {
            setCurrentSection(null);
            loadNextMovement();
            return;
        }
        Section newSection = qm.getNextSection();
        if (newSection == null) {
            System.out.println("No more sections to play");
            setIsPlaying(false);
            return;
        }
        setCurrentSection(newSection);
        setIsPlaying(true);
    }

    public void loadPrevTune() throws LineUnavailableException, InterruptedException {
        Tune prevTune = qm.getPrevTune();
        if (prevTune == null) {
            System.out.println("No more tunes to play");
            timeline.stopAndCleanUp();
            return;
        }
        loadTune(prevTune, false);
    }

    public void loadNextMovement() throws LineUnavailableException, InterruptedException {
        if (qm.isMovementSectionsEmpty()) {
            qm.fillNextMovementSectionQueue();
        }
        if (qm.isMovementSectionsEmpty()) {
            loadPrevTune();
            return;
        }
        qm.replaceSectionQueue();
        updateCurrentSection();
        //  setIsPlaying(true);
    }

    public void updateCurrentSection() throws LineUnavailableException, InterruptedException {
        if (qm.getCurrentSection() == null)
            loadNextSection();
    }

    public void loadMovementNow() throws LineUnavailableException, InterruptedException {
        loadNextSection();
        timeline.stopAndRestart();
        audioPlayer.fadeCurrentCues();
    }

    public void loadTune(Tune tune, boolean loadNow) throws LineUnavailableException, InterruptedException {
        if (loadNow) {
            qm.loadTuneNow(tune);
            loadMovementNow();
            return;
        }
        qm.loadTune(tune);
        if (qm.isSectionQueueEmpty()) {
            loadNextMovement();
            qm.fillNextMovementSectionQueue();
            timeline.triggerNextSection();
        }
    }

}
