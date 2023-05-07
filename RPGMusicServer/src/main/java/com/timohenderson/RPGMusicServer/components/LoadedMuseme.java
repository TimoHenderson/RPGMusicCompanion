package com.timohenderson.RPGMusicServer.components;

import com.adonax.audiocue.AudioCue;
import com.timohenderson.RPGMusicServer.models.musemes.MusemeData;

import java.util.List;

public class LoadedMuseme {
    int length;
    private AudioCue audioCue;
    private String name;
    private List<Integer> startBars;

    public LoadedMuseme(AudioCue audioCue, MusemeData musemeData) {
        this.audioCue = audioCue;
        this.name = musemeData.name();
        this.length = musemeData.length();
        this.startBars = musemeData.startBars();
    }

    public void play() {
        audioCue.play();
    }

    public List<Integer> getStartBars() {
        return startBars;
    }

    public int getLength() {
        return length;
    }
}
