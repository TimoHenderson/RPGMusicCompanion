package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import org.javatuples.Pair;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class MusicCue {
    private PartData partData;
    private AudioCue audioCue;
    private int id;

    public MusicCue(Pair<PartData, URL> data) throws UnsupportedAudioFileException, IOException {
        this.audioCue = AudioCue.makeStereoCue(data.getValue1(), 1);
        this.partData = data.getValue0();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void open(AudioMixer mixer) throws IOException {
        audioCue.open(mixer);
    }

    public void play() {
        id = audioCue.play();
    }

    public void play(double volume) {
        id = audioCue.play(volume);
    }

    public void stop() {
        audioCue.stop(id);
    }

    public void start() {
        audioCue.start(id);
    }

    public boolean isTrackRunning() {
        return audioCue.isTrackRunning();
    }

    public void setVolume(double volume) {
        audioCue.setVolume(id, volume);
    }


}
