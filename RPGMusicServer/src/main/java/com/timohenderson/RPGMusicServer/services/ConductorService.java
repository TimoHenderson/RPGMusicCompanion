package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.gameState.QueueManager;
import com.timohenderson.RPGMusicServer.models.Tune;
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
        if (tuneName.equals("Combat")) {
            qm.loadTuneNow(tune);
            changeSectionImmediately();

        } else {
            qm.loadTune(tune);

        }
    }


    public void play() throws LineUnavailableException, InterruptedException {
        System.out.println(qm.getCurrentSection());
        if (timeline.getCurrentSection() == null) timeline.setCurrentSection(qm.getCurrentSection());
        timeline.play();
    }

    public void changeSectionImmediately() throws LineUnavailableException, InterruptedException {
        timeline.changeSectionNow(qm.getCurrentSection());
    }

    public void stop() throws LineUnavailableException {
        timeline.stop();
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
