package com.timohenderson.RPGMusicServer.events;

import com.timohenderson.RPGMusicServer.services.TimelineService;
import com.timohenderson.RPGMusicServer.services.TuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EventsHandler {
    @Autowired
    TuneService tuneService;
    @Autowired
    TimelineService timelineService;

    private ArrayList<String> log = new ArrayList<>();

    @EventListener
    public void logEvents(Event event) {
        log.add(event.toString());
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
    public void handleBarEvent(BarEvent event) {
    }

    @Async
    @EventListener
    public void handleTransportEvent(TransportEvent event) throws InterruptedException {
        switch (event.getAction()) {
            case PLAY:
                timelineService.play();
                break;
            case STOP:
                timelineService.stop();
        }
    }

    public void handleGameParamsEvent(GameParamsEvent event) {
    }

    public void handleLoadTuneEvent(LoadTuneEvent event) {
        tuneService.loadTune(event.getTuneName());
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
