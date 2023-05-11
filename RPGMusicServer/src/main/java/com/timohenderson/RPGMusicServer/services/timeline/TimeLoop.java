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

    public boolean play(long barLength) {
        if (!runTimer) {
            System.out.println("play");
            runTimer = true;
            this.barLength = barLength;
            runExecutor();
        }
        return true;
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
        public void run() throws RuntimeException {
            if (playCues) {
                try {
                    audioPlayer.playNextCues();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                if (timeline.shouldTimeLineEnd()) {
                    timeline.loadNextSectionHandler();
                    timeline.setEnd(false);
                } else {
                    audioPlayer.setCurrentBar(timeline.getCurrentBar());
                    timeline.nextBar();
                }
            } catch (LineUnavailableException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
