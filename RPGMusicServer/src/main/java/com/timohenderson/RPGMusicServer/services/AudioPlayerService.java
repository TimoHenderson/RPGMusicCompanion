package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.audio.MusicCue;
import com.timohenderson.RPGMusicServer.audio.RPGMixer;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AudioPlayerService {

    @Autowired
    RPGMixer mixer;
    Section section;
    int currentBar = 0;

    ArrayList<MusicCue> nextCues = new ArrayList<MusicCue>();
    ArrayList<MusicCue> currentCues = new ArrayList<MusicCue>();

    MusicCue[] nextCuesArray;
    int[] ids;

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
        List<Pair<PartData, URL>> nextMusemeURLs = section.getNextMusemeURLs(currentBar);
        if (nextMusemeURLs != null) {
            nextMusemeURLs.forEach((url) -> {
                try {
                    MusicCue musicCue = new MusicCue(url);
                    musicCue.open(mixer);
                    nextCues.add(musicCue);
                } catch (UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
            });

            nextCuesArray = (nextCues.toArray(new MusicCue[nextCues.size()]));
            ids = new int[nextCuesArray.length];
        }
    }

    private void cleanCurrentCues() {

        Iterator<MusicCue> cueItr = currentCues.iterator();
        while (cueItr.hasNext()) {
            MusicCue entry = cueItr.next();
            if (!entry.isTrackRunning()) {
                cueItr.remove();
            }
        }

    }

    public void stop() {
        mixer.stop();
    }

    public void play() throws LineUnavailableException {
        mixer.start();
    }

    public void stopCurrentCues() throws LineUnavailableException {
        for (MusicCue cue : currentCues) {
            cue.stop();
        }
        currentCues.clear();
    }

    public void fadeCurrentCues() throws LineUnavailableException {
        for (MusicCue cue : currentCues) {
            cue.setVolume(0);
        }
        currentCues.clear();
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
        for (int i = 0; i < nextCuesArray.length; i++) {
            currentCues.add(nextCuesArray[i]);
        }
        nextCuesArray = null;
        cleanCurrentCues();
    }
}
