package com.timohenderson.RPGMusicServer.audio;

import com.adonax.audiocue.AudioCue;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioPlayerService {

    @Autowired
    RPGMixer mixer;
    Section section;
    int currentBar = 0;

    ArrayList<AudioCue> nextCues = new ArrayList<AudioCue>();

    public void loadSection(Section section) {
        this.section = section;
    }

    public void setCurrentBar(int currentBar) throws LineUnavailableException {
        this.currentBar = currentBar;
        queueNextMusemes();
    }

    public void queueNextMusemes() throws LineUnavailableException {
        List<URL> nextMusemeURLs = section.getNextMusemeURLs(currentBar);
        System.out.println(nextMusemeURLs);
        nextMusemeURLs.forEach((url) -> {
            try {
                AudioCue audioCue = AudioCue.makeStereoCue(url, 1);
                audioCue.open(mixer);
                nextCues.add(audioCue);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            mixer.setNextCues(nextCues.toArray(new AudioCue[nextCues.size()]));
        });
    }

    public void stop() {
    }

    public Section getLoadedSection() {
        return section;
    }
}
