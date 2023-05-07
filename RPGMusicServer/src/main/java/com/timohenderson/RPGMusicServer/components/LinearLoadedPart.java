package com.timohenderson.RPGMusicServer.components;

import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class LinearLoadedPart extends LoadedPart {
    LoadedMuseme loadedMuseme;


    public LinearLoadedPart(LinearPart part, AudioMixer mixer) throws UnsupportedAudioFileException, IOException {
        super(part.getPartData());
        loadedMuseme = super.loadMuseme(part.getMuseme(), mixer);
    }

    @Override
    public void play(int bar) {
        loadedMuseme.play();
    }
}
