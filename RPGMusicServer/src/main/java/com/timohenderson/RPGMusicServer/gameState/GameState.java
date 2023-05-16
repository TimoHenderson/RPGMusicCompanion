package com.timohenderson.RPGMusicServer.gameState;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.enums.ParamType;
import com.timohenderson.RPGMusicServer.events.SendGameStateEvent;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.services.AudioPlayerService;
import com.timohenderson.RPGMusicServer.services.timeline.TimelineService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.HashMap;

@Service
public class GameState {
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    AudioPlayerService audioPlayer;
    @Autowired
    private TimelineService timeline;
    @Getter
    private double darkness = 3.0;
    @Getter
    private double intensity = 3.0;
    @Getter
    private boolean isPlaying = false;

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
        setIsPlaying(true);
        sendGameState();
        //if (section != null) audioPlayer.loadSection(section);
    }

    public boolean setIsPlaying(boolean isPlaying) throws LineUnavailableException {
//
        if (isPlaying && qm.getCurrentSection() != null) {
            if (!this.isPlaying) timeline.play();
            this.isPlaying = true;
        } else {
            timeline.stop();
            this.isPlaying = false;
        }
        sendGameStatePlay();
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


    }

    public void loadPrevTune() throws LineUnavailableException, InterruptedException {
        Tune prevTune = qm.getPrevTune();
        if (prevTune == null) {
            System.out.println("No more tunes to play");
            timeline.stopAndCleanUp();
            setIsPlaying(false);
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
        //setIsPlaying(true);
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

        }
        if (!isPlaying) loadNextSection();

        //sendGameState();
    }

    public void sendGameStatePlay() {

        PlayStateForClient psfc = new PlayStateForClient(isPlaying);

        publisher.publishEvent(new SendGameStateEvent(this, toJSONString(psfc)));
    }

    public void sendGameState() {
        GameStateForClient gsfc = toGameStateForClient();
        publisher.publishEvent(new SendGameStateEvent(this, toJSONString(gsfc)));
    }

    public String toJSONString(Object object) {
        Gson gson = new Gson();
        //GameStateForClient gsfc = toGameStateForClient();
        System.out.println(object.toString());
        String json = gson.toJson(object);
        System.out.println(json);
        return json;
    }

    public GameStateForClient toGameStateForClient() {
        return new GameStateForClient(
                qm.getCurrentTuneName(),
                qm.getPrevTuneName(),
                qm.getCurrentMovementName(),
                qm.getNextMovementName(),
                qm.getCurrentSectionName(),
                qm.getNextSectionName(),
                isPlaying
        );
    }

}
