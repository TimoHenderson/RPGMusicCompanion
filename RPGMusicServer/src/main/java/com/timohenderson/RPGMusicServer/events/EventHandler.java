package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.gameState.GameState;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.services.AudioPlayer;
import com.timohenderson.RPGMusicServer.services.TuneService;
import com.timohenderson.RPGMusicServer.services.timeline.Timeline;
import com.timohenderson.RPGMusicServer.websocket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class EventHandler {
    @Autowired
    TuneService tuneService;
    @Autowired
    Timeline timeline;
    @Autowired
    AudioPlayer audioPlayer;
    @Autowired
    SocketHandler socketHandler;
    @Autowired
    GameState gs;
    private ArrayList<String> log = new ArrayList<>();

    @EventListener
    public void logEvents(Event event) {
        log.add(event.toString());
        System.out.println(event.toString());
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void printLog() {
        for (int i = 0; i < log.size(); i++) {
            System.out.println(i + ": " + log.get(i));
        }
    }

    public void clearLog() {
        log.clear();
    }


    @Async
    @EventListener
    public void handleBarEvent(BarEvent event) throws LineUnavailableException {

    }


    @EventListener
    public void handleTransportEvent(TransportEvent event) throws InterruptedException, LineUnavailableException {
        switch (event.getAction()) {
            case PLAY:
                System.out.println("PLAY");
                gs.setIsPlaying(true);
                break;
            case STOP:
                System.out.println("STOP");
                gs.setIsPlaying(false);
                break;
            case PAUSE:
                System.out.println("PAUSE");

        }
    }

    @Async
    @EventListener
    public void handleGameParamsEvent(GameParamsEvent event) {
        gs.setGameParams(event.getParams());
    }

    @EventListener
    public void handleLoadTuneEvent(LoadTuneEvent event) throws LineUnavailableException, InterruptedException {
        System.out.println("LoadTuneEvent");
        String tuneName = event.getTuneName();
        Tune tune = tuneService.loadTune(tuneName);
        //gs.loadTune(tune, tuneName.equals("Combat"));

    }

    @EventListener
    public void handleNavigationEvent(NavigationEvent event) throws LineUnavailableException, InterruptedException {
        System.out.println("NavigateEvent");
//        if (event.getSource() instanceof Timeline) {
//            gs.loadNextSection();
//            return;
//        }

        switch (event.getAction()) {
            case NEXT_SECTION:
                timeline.triggerNextSection();
                break;
        }
    }

    @Async
    @EventListener
    public void handleSendGameStateEvent(SendGameStateEvent event) throws IOException {

        System.out.println("SendGameStateEvent");
        if (event.getSource() instanceof SocketHandler) {
            gs.sendGameState();
            System.out.println("State Please");
            return;
        }
        socketHandler.sendTextMessage(event.getState());
    }

//
//
//

}
