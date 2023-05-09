package com.timohenderson.RPGMusicServer.services.timeline;

import com.timohenderson.RPGMusicServer.services.AudioPlayerService;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TimeLoop {

    AudioPlayerService audioPlayer;
    ScheduledExecutorService executorService;
    TimelineService timeline;
    private volatile boolean runTimer;
    private volatile boolean playCues = true;
    private long barLength = 0;


    public TimeLoop(TimelineService timeline, AudioPlayerService audioPlayer) {
        this.timeline = timeline;
        this.audioPlayer = audioPlayer;
    }

    public void setPlayCues(boolean playCues) {
        this.playCues = playCues;
    }

    public void play(long barLength) throws LineUnavailableException {
        if (!runTimer) {
            runTimer = true;
            this.barLength = barLength;
            runExecutor();
        }
    }

    public void stop() {
        runTimer = false;
        audioPlayer.stop();
        executorService.shutdown();
    }

    public void stopLoop() throws LineUnavailableException {
        runTimer = false;
        //audioPlayer.stop();
        //audioPlayer.fadeCurrentCues();
        if (executorService != null)
            executorService.shutdownNow();
    }

    public void resume() throws LineUnavailableException {
        runTimer = true;
        audioPlayer.play();
        runExecutor();
    }

    private void runExecutor() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new TimeLineLoop(), 0, barLength, TimeUnit.MILLISECONDS);
    }


    private class TimeLineLoop implements Runnable {
        @Override
        public void run() {
            try {
                if (playCues) {
                    audioPlayer.playNextCues();
                    System.out.println("playing next cues");
                }
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            try {
                if (timeline.shouldTimeLineEnd()) {
                    try {
                        timeline.loadNextSectionHandler();
                    } catch (InterruptedException e) {

                        throw new RuntimeException(e);
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }

                    timeline.setEnd(false);
                } else {
                    try {
                        audioPlayer.setCurrentBar(timeline.getCurrentBar());
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    timeline.nextBar();
                }
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
