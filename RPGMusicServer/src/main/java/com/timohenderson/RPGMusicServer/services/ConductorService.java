package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.gameState.QueueManager;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.timeline.Timeline;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;

@Service
public class ConductorService {


    private QueueManager qm = new QueueManager();
    private Timeline timeline = new Timeline();
    private TuneService tuneService = new TuneService();

    public ConductorService() throws LineUnavailableException {
    }

    public void loadTune(String tuneName) throws LineUnavailableException, InterruptedException {
        Tune tune = tuneService.loadTune(tuneName);
        if (tuneName.equals("Combat")) {
            qm.loadTuneNow(tune);
            timeline.stopAndRestart();
            audioPlayer.fadeCurrentCues();
            setIsPlaying(false);
        } else qm.loadTune(tune);
    }


    public void loadMovementNow() throws LineUnavailableException, InterruptedException {
    }

    public void play() throws LineUnavailableException {
        timeline.play();
    }

    public void setCurrentSection(Section section) throws LineUnavailableException, InterruptedException {
        qm.setCurrentSection(section);
        timeline.setCurrentSection(section);
        setIsPlaying(true);
        sendGameState();
        //if (section != null) audioPlayer.loadSection(section);
    }


}
