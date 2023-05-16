package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioMixer;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;

@Component
public class RPGMixer extends AudioMixer {

//    @Autowired
//    public RPGMixer(VBMixer vbMixer) throws LineUnavailableException {
//        super(vbMixer.getMixer(), 4096, 10);
//        start();
//    }

    public RPGMixer() throws LineUnavailableException {
        super();
        start();
    }
}
