//package com.timohenderson.RPGMusicServer.gameState;
//
//import com.google.gson.Gson;
//import com.timohenderson.RPGMusicServer.enums.ParamType;
//import com.timohenderson.RPGMusicServer.events.SendGameStateEvent;
//import com.timohenderson.RPGMusicServer.services.timeline.AudioPlayer;
//import com.timohenderson.RPGMusicServer.services.timeline.Timeline;
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//
//import javax.sound.sampled.LineUnavailableException;
//import java.util.HashMap;
//
//@Service
//public class GameState {
//    @Autowired
//    ApplicationEventPublisher publisher;
//    @Autowired
//    AudioPlayer audioPlayer;
//    @Autowired
//    private Timeline timeline;
//    @Getter
//    private double darkness = 3.0;
//    @Getter
//    private double intensity = 3.0;
//    @Getter
//    private boolean isPlaying = false;
//
//
//    public void setGameParams(HashMap<ParamType, Double> params) {
//        if (params.containsKey(ParamType.DARKNESS)) {
//            this.darkness = params.get(ParamType.DARKNESS);
//            audioPlayer.setDarkness(this.darkness);
//        }
//        if (params.containsKey(ParamType.INTENSITY)) {
//            this.intensity = params.get(ParamType.INTENSITY);
//            audioPlayer.setIntensity(this.intensity);
//        }
//    }
//
//    public boolean setIsPlaying(boolean isPlaying) throws LineUnavailableException {
////
//        if (isPlaying && qm.getCurrentSection() != null) {
//            if (!this.isPlaying) timeline.play();
//            this.isPlaying = true;
//        } else {
//            timeline.stop();
//            this.isPlaying = false;
//        }
//        sendGameStatePlay();
//        return this.isPlaying;
//    }
//
//
//    public void sendGameStatePlay() {
//        PlayStateForClient psfc = new PlayStateForClient(isPlaying);
//        publisher.publishEvent(new SendGameStateEvent(this, toJSONString(psfc)));
//    }
//
//    public void sendGameState() {
//        GameStateForClient gsfc = toGameStateForClient();
//        publisher.publishEvent(new SendGameStateEvent(this, toJSONString(gsfc)));
//    }
//
//    public String toJSONString(Object object) {
//        Gson gson = new Gson();
//        //GameStateForClient gsfc = toGameStateForClient();
//        System.out.println(object.toString());
//        String json = gson.toJson(object);
//        System.out.println(json);
//        return json;
//    }
//
//    public GameStateForClient toGameStateForClient() {
//        return new GameStateForClient(
//                qm.getCurrentTuneName(),
//                qm.getPrevTuneName(),
//                qm.getCurrentMovementName(),
//                qm.getNextMovementName(),
//                qm.getCurrentSectionName(),
//                qm.getNextSectionName(),
//                isPlaying
//        );
//    }
//
//}
