package com.timohenderson.RPGMusicServer.audio.loaded;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.parts.PartData;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public abstract class LoadedPart {
    int sectionLength;
    String name;
    int darkness;
    int intensity;

    LoadedPart(PartData partData) {
        this.name = partData.name();
        this.darkness = partData.darkness();
        this.intensity = partData.intensity();
        this.sectionLength = partData.sectionLength();
    }

    public abstract void play(int bar);

    public LoadedMuseme loadMuseme(Museme museme, AudioMixer mixer) throws UnsupportedAudioFileException, IOException {
        URL url = museme.getURL();
        AudioCue audioCue = AudioCue.makeStereoCue(url, 3);
        audioCue.open(mixer);
        return new LoadedMuseme(audioCue, museme.getMusemeData());
    }
}
