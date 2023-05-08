package com.timohenderson.RPGMusicServer.services;

import com.adonax.audiocue.AudioCue;
import com.timohenderson.RPGMusicServer.audio.RPGMixer;
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

    AudioCue[] nextCuesArray;

    public void loadSection(Section section) throws LineUnavailableException {
        this.section = section;
        currentBar = 0;
        queueNextMusemes();
    }

    public void loadNextSection(Section section) throws LineUnavailableException {

        this.section = section;
        currentBar = 0;
        queueNextMusemes();
    }


    public void setCurrentBar(int currentBar) throws LineUnavailableException {
        this.currentBar = currentBar;
        queueNextMusemes();
    }

    public void queueNextMusemes() throws LineUnavailableException {
        nextCues.clear();
        List<URL> nextMusemeURLs = section.getNextMusemeURLs(currentBar);
        if (nextMusemeURLs != null) {
            nextMusemeURLs.forEach((url) -> {
                try {
                    AudioCue audioCue = AudioCue.makeStereoCue(url, 1);
                    audioCue.open(mixer);
                    nextCues.add(audioCue);
                } catch (UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
            });
            nextCuesArray = (nextCues.toArray(new AudioCue[nextCues.size()]));

        }
    }

    public void stop() {
        mixer.stop();
    }

    public void play() throws LineUnavailableException {
        mixer.start();
    }

    public Section getLoadedSection() {
        return section;
    }


    public void playNextCues() throws LineUnavailableException {
        if (nextCuesArray != null) {
            for (int i = 0; i < nextCuesArray.length; i++) {
                // print current system time
//                System.out.println(i + ": Current time in milliseconds = " + System.currentTimeMillis());
                nextCuesArray[i].play();
//                System.out.println(i + ": Current time in milliseconds = " + System.currentTimeMillis());
            }
        }
        nextCuesArray = null;


    }
}
