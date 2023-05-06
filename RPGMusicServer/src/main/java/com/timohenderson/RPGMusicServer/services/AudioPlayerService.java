package com.timohenderson.RPGMusicServer.services;

import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioMixer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

@Component
public class AudioPlayerService {
    AudioCue audioCue1;
    AudioCue audioCue2;
    AudioCue audioCue3;
    AudioCue audioCue4;
    AudioCue audioCue5;
    AudioCue audioCue6;
    AudioCue hitCue;
    AudioMixer audioMixer;
    int currentBar = 0;
    AudioCue[] audioCues = new AudioCue[4];


    public AudioPlayerService() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
//        URL url = this.getClass().getResource("/testwav/Voice1.wav");
//        URL url2 = this.getClass().getResource("/testwav/Voice2.wav");
//        URL url3 = this.getClass().getResource("/testwav/Voice3.wav");
//        URL url4 = this.getClass().getResource("/testwav/Voice4.wav");
        //URL hitUrl = this.getClass().getResource("/testwav/HitHit.wav");

        URL url = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/BASS/Cello_d2_i2/1_b1_l2.wav");
        URL url2 = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/BASS/Cello_d2_i2/4_b3_l2.wav");
        URL url3 = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/BASS/Cello_d2_i2/6_b5_l2.wav");
        URL url4 = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/BASS/Cello_d2_i2/8_b7_l2.wav");
        URL url5 = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/PAD/Harp_i3_d2/1.wav");
        URL url6 = this.getClass().getResource("/static/tunes/Enchanted_Forest/1_Story/main/PAD/Cyclosa/1.wav");
        audioMixer = new AudioMixer();
        // hitCue = AudioCue.makeStereoCue(hitUrl, 1);
        audioCue1 = AudioCue.makeStereoCue(url, 1);
        audioCue2 = AudioCue.makeStereoCue(url2, 1);
        audioCue3 = AudioCue.makeStereoCue(url3, 1);
        audioCue4 = AudioCue.makeStereoCue(url4, 1);
        audioCue5 = AudioCue.makeStereoCue(url5, 2);
        audioCue6 = AudioCue.makeStereoCue(url6, 2);

        audioCue1.open(audioMixer);
//        hitCue.open(audioMixer);
        audioCue2.open(audioMixer);
        audioCue3.open(audioMixer);
        audioCue4.open(audioMixer);
        audioCue5.open(audioMixer);
        audioCue6.open(audioMixer);
        audioCues[0] = audioCue1;
        audioCues[1] = audioCue2;
        audioCues[2] = audioCue3;
        audioCues[3] = audioCue4;
        audioMixer.start();
    }

    @Async
    public void play(int bar) throws LineUnavailableException {
        if (bar == 1) {
            audioCue5.play();
            audioCue6.play();
        }
        audioCues[bar - 1].play();


    }

    public void stop() {
    }
}
