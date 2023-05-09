package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.services.AudioPlayerService;
import com.timohenderson.RPGMusicServer.services.TimelineService;
import com.timohenderson.RPGMusicServer.services.TuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

@Service
public class EventsHandler {
    @Autowired
    TuneService tuneService;
    @Autowired
    TimelineService timelineService;
    @Autowired
    AudioPlayerService audioPlayerService;

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

    @Async
    @EventListener
    public void handleTransportEvent(TransportEvent event) throws InterruptedException, LineUnavailableException {
        switch (event.getAction()) {
            case PLAY:
                System.out.println("PLAY");
                //timelineService.play();
                break;
            case STOP:
                System.out.println("STOP");
                //timelineService.stop();
                break;
            case PAUSE:
                System.out.println("PAUSE");
        }
    }

    @EventListener
    public void handleGameParamsEvent(GameParamsEvent event) {
        System.out.println("GameParamsEvent" + event.toString());
    }

    @EventListener
    public void handleLoadTuneEvent(LoadTuneEvent event) {
        System.out.println("LoadTuneEvent");
        tuneService.loadTune(event.getTuneName());
    }

    @EventListener
    public void handleNavigationEvent(NavigationEvent event) {
        System.out.println("NavigateEvent");
    }

//    public void handleChangeMovementEvent(ChangeMovementEvent event) {
//    }
//
//
//
//    public void handleLoadTuneEvent(ChangeTuneEvent event) {
//    }
//
//
//
//    public void changeSectionEvent(ChangeSectionEvent event) {
//    }
//
//

}
