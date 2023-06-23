package com.timohenderson.RPGMusicServer.timeline;

import com.timohenderson.RPGMusicServer.audio.MusicCue;
import com.timohenderson.RPGMusicServer.audio.RPGMixer;
import com.timohenderson.RPGMusicServer.gameState.GameParameters;
import com.timohenderson.RPGMusicServer.models.tunes.parts.PartData;
import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;
import org.javatuples.Pair;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class AudioPlayer {


    RPGMixer mixer = new RPGMixer();
    Section section;
    int currentBar = 0;
    GameParameters gameParameters;
    Timer timer;
    ArrayList<MusicCue> nextCues = new ArrayList<MusicCue>();
    ArrayList<MusicCue> currentCues = new ArrayList<MusicCue>();
    ArrayList<MusicCue> fadingCues = new ArrayList<>();
    MusicCue[] nextCuesArray;
    int[] ids;

    public AudioPlayer(GameParameters gameParameters) throws LineUnavailableException {
        this.gameParameters = gameParameters;
    }

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


    public void queueNextMusemes() {
        nextCues.clear();
        System.out.println(gameParameters.getDarkness() + " " + gameParameters.getIntensity());
        List<Pair<PartData, URL>> nextMusemeURLs =
                section.getNextMusemeURLs(currentBar, gameParameters.getDarkness(), gameParameters.getIntensity());
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
        //System.out.println("Cleaning current cues");
        if (currentCues != null) {
            Iterator<MusicCue> cueItr = currentCues.iterator();
            while (cueItr.hasNext()) {
                MusicCue musicCue = cueItr.next();
                //          System.out.println(musicCue);
                if (!musicCue.getIsActive()) {
                    musicCue.close();
                    cueItr.remove();
                }
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

    //@Async
    public void fadeCurrentCues() throws LineUnavailableException {
        //
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
                // System.out.println("Fading complete");
                timer.cancel();
            }

        }
    }


}
