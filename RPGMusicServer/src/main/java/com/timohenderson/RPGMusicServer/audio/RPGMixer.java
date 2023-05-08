package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;

@Component
public class RPGMixer extends AudioMixer {


    AudioCue[] nextCues;

    public RPGMixer() throws LineUnavailableException {
        super();
        start();
    }

//    public RPGMixer(Mixer mixer, int channels, int bufferSize) {
//        super(mixer, channels, bufferSize);
//    }


    public void playNextCues() {
        for (int i = 0; i < nextCues.length; i++) {
            nextCues[i].play();
        }
    }

    public void setNextCues(AudioCue[] nextCues) {
        this.nextCues = nextCues;
    }


}
