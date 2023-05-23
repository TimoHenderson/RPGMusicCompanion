package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.enums.ParamType;
import com.timohenderson.RPGMusicServer.gameState.GameParameters;
import com.timohenderson.RPGMusicServer.managers.QueueManager;
import com.timohenderson.RPGMusicServer.models.tunes.Tune;
import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;
import com.timohenderson.RPGMusicServer.timeline.Timeline;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.HashMap;

@Service
public class ConductorService {

    @Getter
    private GameParameters gameParameters = new GameParameters();
    private QueueManager qm = new QueueManager();
    private Timeline timeline;
    @Autowired
    private TuneService tuneService;

    public ConductorService() throws LineUnavailableException {
        timeline = new Timeline(this, gameParameters);
    }

    public void play() throws LineUnavailableException, InterruptedException {
        System.out.println(qm.getCurrentSection());
        if (timeline.getCurrentSection() == null) {
            Section currentSection = qm.getCurrentSection();
            if (currentSection == null) {
                loadNextSection();
            } else {
                timeline.setCurrentSection(currentSection);
            }
        }
        timeline.play();
    }

    public void stop() throws LineUnavailableException {
        timeline.stop();
    }

    public void changeSectionImmediately() throws LineUnavailableException, InterruptedException {
        timeline.changeSectionNow(qm.getCurrentSection());
    }

    public void triggerNextSection() {
        System.out.println("triggerNextSection");
        timeline.triggerNextSection();
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

    public void loadNextSection() throws LineUnavailableException, InterruptedException {
        System.out.println("loadNextSection");
        timeline.setCurrentSection(qm.loadNextSection());
    }


    public void setGameParams(HashMap<ParamType, Double> params) {
        gameParameters.setParams(params);
    }
}
