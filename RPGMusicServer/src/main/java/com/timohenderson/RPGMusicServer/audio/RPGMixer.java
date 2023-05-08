package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioMixer;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;

@Component
public class RPGMixer extends AudioMixer {


    public RPGMixer() throws LineUnavailableException {
        super();
        start();
    }

//    public RPGMixer(Mixer mixer, int channels, int bufferSize) {
//        super(mixer, channels, bufferSize);
//    }


}
