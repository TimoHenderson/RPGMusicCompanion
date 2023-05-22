package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.gameState.QueueManager;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.timeline.Timeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;

@Service
public class ConductorService {


    private QueueManager qm = new QueueManager();
    private Timeline timeline;
    @Autowired
    private TuneService tuneService;

    public ConductorService() throws LineUnavailableException {
        timeline = new Timeline(this);
    }

    public void loadTune(String tuneName) throws LineUnavailableException, InterruptedException {
        Tune tune = tuneService.loadTune(tuneName);
        Section currentSection;
        if (tuneName.equals("Combat")) {
            qm.loadTuneNow(tune);
            timeline.stopAndRestart();
        } else qm.loadTune(tune);
    }


    public void play() throws LineUnavailableException, InterruptedException {
        System.out.println(qm.getCurrentSection());
        if (timeline.getCurrentSection() == null) timeline.setCurrentSection(qm.getCurrentSection());
        timeline.play();
    }

    public void fadeAndChangeSection() {
//        audioPlayer.fadeCurrentCues();
//        setIsPlaying(false);
    }

    public void stop() {
    }

    public void triggerNextSection() {
        System.out.println("triggerNextSection");
        timeline.triggerNextSection();
    }

    public void loadNextSection() throws LineUnavailableException, InterruptedException {
        System.out.println("loadNextSection");
        timeline.setCurrentSection(qm.loadNextSection());
    }


}
