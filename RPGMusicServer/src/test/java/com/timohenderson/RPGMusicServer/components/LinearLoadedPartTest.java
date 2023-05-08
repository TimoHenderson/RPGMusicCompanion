package com.timohenderson.RPGMusicServer.components;

import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.musemes.MusemeData;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.parts.PartData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class LinearLoadedPartTest {
    LinearLoadedPart linearLoadedPart;
    AudioMixer audioMixer;

    @BeforeEach
    void setUp() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        PartData partData = new PartData("Test", 1, 1, 1);
        List<Integer> startBars = new ArrayList<Integer>();
        startBars.add(1);
        MusemeData musemeData = new MusemeData("Test", 4, startBars);
        Museme museme = new Museme("/static/tunes/Enchanted_Forest/0_Intro/main/PAD/Cyclosa2_i3/1.wav", musemeData);
        LinearPart linearPart = new LinearPart(partData, museme);
        audioMixer = new AudioMixer();
        audioMixer.start();
        linearLoadedPart = new LinearLoadedPart(linearPart, audioMixer);
    }

    @AfterEach
    void tearDown() {
        audioMixer.stop();
    }

    @Test
    void playOnStartBar() throws InterruptedException {
        linearLoadedPart.play(1);
        Thread.sleep(1000);
    }

    @Test
    void notPlayOnNonStartBar() throws InterruptedException {
        linearLoadedPart.play(2);
        Thread.sleep(1000);
    }
}