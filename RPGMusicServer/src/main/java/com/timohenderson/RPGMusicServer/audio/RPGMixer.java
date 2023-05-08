package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.sound.sampled.Mixer;

@Component
public class RPGMixer extends AudioMixer {

    public RPGMixer() {
        super();
    }

    public RPGMixer(Mixer mixer, int channels, int bufferSize) {
        super(mixer, channels, bufferSize);
    }

    public void addAudioCue(@NotNull AudioCue audioCue) {
        audioCue.open(this);
    }
}
