package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.services.ConductorService;
import com.timohenderson.RPGMusicServer.websocket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

@Service
public class EventHandler {

    @Autowired
    ConductorService conductor;
    @Autowired
    SocketHandler socketHandler;

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
                conductor.play();
                break;
            case STOP:
                System.out.println("STOP");
                conductor.stop();
                break;
            case PAUSE:
                System.out.println("PAUSE");

        }
    }

//    @Async
//    @EventListener
//    public void handleGameParamsEvent(GameParamsEvent event) {
//        gs.setGameParams(event.getParams());
//    }

    @EventListener
    public void handleLoadTuneEvent(LoadTuneEvent event) throws LineUnavailableException, InterruptedException {
        System.out.println("LoadTuneEvent");
        String tuneName = event.getTuneName();
        conductor.loadTune(tuneName);
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
                conductor.triggerNextSection();
                break;
        }
    }

//    @Async
//    @EventListener
//    public void handleSendGameStateEvent(SendGameStateEvent event) throws IOException {
//
//        System.out.println("SendGameStateEvent");
//        if (event.getSource() instanceof SocketHandler) {
//            gs.sendGameState();
//            System.out.println("State Please");
//            return;
//        }
//        socketHandler.sendTextMessage(event.getState());
//    }

//
//
//

}
