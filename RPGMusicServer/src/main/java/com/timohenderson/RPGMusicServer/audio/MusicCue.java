package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.tunes.parts.PartData;
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
        audioCue.setRecycleWhenDone(id, true);

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

    public boolean getIsPlaying() {
        return audioCue.getIsPlaying(id);
    }

    public boolean getIsActive() {
        return audioCue.getIsActive(id);
    }

    public double getVolume() {
        return audioCue.getVolume(id);
    }

    public void setVolume(double volume) {
        try {
            audioCue.setVolume(id, volume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        audioCue.close();
    }
}
