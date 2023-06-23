package com.timohenderson.RPGMusicServer.timeline;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TimeLoop {

    AudioPlayer audioPlayer;
    ScheduledExecutorService executorService;
    Timeline timeline;
    private volatile boolean runTimer;
    private volatile boolean playCues = true;
    private long barLength = 0;


    public TimeLoop(Timeline timeline, AudioPlayer audioPlayer) {
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
        if (executorService != null)
            executorService.shutdownNow();
    }

    public void resume() throws LineUnavailableException {
        runTimer = true;
        audioPlayer.play();
        runExecutor();
    }

    private void runExecutor() {
        executorService = Executors.newScheduledThreadPool(4, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        });
        executorService.scheduleAtFixedRate(new TimeLineLoop(), 0, barLength * 1000000, TimeUnit.NANOSECONDS);
    }


    private class TimeLineLoop implements Runnable {
        @Override
        public void run() throws RuntimeException {
            if (playCues) {
                try {
//                    System.out.println("playCues");
                    audioPlayer.playNextCues();

                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                if (timeline.shouldTimeLineEnd()) {
                    System.out.println("shouldTimeLineEnd");
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
