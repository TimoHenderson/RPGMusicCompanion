package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.audio.MusicCue;
import com.timohenderson.RPGMusicServer.audio.RPGMixer;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class AudioPlayerService {

    @Autowired
    RPGMixer mixer;
    Section section;
    int currentBar = 0;
    Timer timer;
    ArrayList<MusicCue> nextCues = new ArrayList<MusicCue>();
    ArrayList<MusicCue> currentCues = new ArrayList<MusicCue>();
    ArrayList<MusicCue> fadingCues = new ArrayList<>();

    MusicCue[] nextCuesArray;
    int[] ids;

    public void loadSection(Section section) throws LineUnavailableException {
        section.reset();
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
        nextMusemeURLs.stream().forEach(m -> System.out.println(m.getValue1()));
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
            cleanCurrentCues();
        }
    }

    private void cleanCurrentCues() {
        Iterator<MusicCue> cueItr = currentCues.iterator();
        while (cueItr.hasNext()) {
            MusicCue musicCue = cueItr.next();
            if (!musicCue.getIsActive()) {
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

    @Async
    public void fadeCurrentCues() throws LineUnavailableException {
        cleanCurrentCues();
        fadingCues.addAll(currentCues);
        currentCues.clear();
        timer = new Timer();
        timer.schedule(new FadeTask(), 0, 24);
    }

    public Section getLoadedSection() {
        return section;
    }

    public void playNextCues() throws LineUnavailableException {
        if (nextCuesArray != null) {
            for (int i = 0; i < nextCuesArray.length; i++) {
                nextCuesArray[i].play();
            }
        }
        for (int i = 0; i < nextCuesArray.length; i++) {
            currentCues.add(nextCuesArray[i]);
        }
        nextCuesArray = null;
    }

    private class FadeTask extends TimerTask {
        @Override
        public void run() {
            //System.out.println("Fading");
            Iterator<MusicCue> cueItr = fadingCues.iterator();
            while (cueItr.hasNext()) {
                MusicCue cue = cueItr.next();
                //System.out.println("Fading " + cue.getVolume());
                if (cue.getIsActive()) {
                    double cueVolume = cue.getVolume();
                    if (cueVolume > 0.5) cue.setVolume(cueVolume - 0.05);
                    else if (cueVolume > 0.05) cue.setVolume(cueVolume - 0.02);
                    else if (cueVolume > 0.01) cue.setVolume(cueVolume - 0.01);
                    else if (cueVolume <= 0.01) cue.setVolume(0);
                    else cueItr.remove();
                } else cueItr.remove();
            }
            if (fadingCues.size() == 0) {
                System.out.println("Fading complete");
                timer.cancel();
            }

        }
    }


}
