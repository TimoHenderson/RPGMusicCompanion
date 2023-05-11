package com.timohenderson.RPGMusicServer.gameState;

public class PlayStateForClient {
    final String event = "gameState";
    boolean isPlaying;

    public PlayStateForClient(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
