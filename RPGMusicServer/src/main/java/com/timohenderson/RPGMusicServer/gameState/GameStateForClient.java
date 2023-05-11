package com.timohenderson.RPGMusicServer.gameState;

public class GameStateForClient {
    final String event = "gameState";
    String currentTune;
    String prevTune;
    String currentMovement;
    String nextMovement;
    String currentSection;
    String nextSection;
    boolean isPlaying;

    public GameStateForClient(String currentTune, String prevTune, String currentMovement, String nextMovement, String currentSection, String nextSection, boolean isPlaying) {
        this.currentTune = currentTune;
        this.prevTune = prevTune;
        this.currentMovement = currentMovement;
        this.nextMovement = nextMovement;
        this.currentSection = currentSection;
        this.nextSection = nextSection;
        this.isPlaying = isPlaying;
    }
}
