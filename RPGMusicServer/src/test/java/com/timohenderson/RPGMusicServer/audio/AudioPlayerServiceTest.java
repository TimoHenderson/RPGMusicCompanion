package com.timohenderson.RPGMusicServer.audio;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AudioPlayerServiceTest {
    @Autowired
    AudioPlayerService audioPlayerService;
    @Autowired
    TuneRepository tuneRepository;
    @Autowired
    RPGMixer mixer;
    Section renderedSection;
    Section adaptiveSection;

    @BeforeEach
    void setUp() {
        Tune tune = tuneRepository.findByName("Enchanted_Forest");
        Movement intro = tune.getMovements().get(0);
        renderedSection = intro.getSections().get(1);
        adaptiveSection = intro.getSections().get(0);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadSection() {
        audioPlayerService.loadSection(renderedSection);
        Section loadedSection = audioPlayerService.getLoadedSection();
        assertEquals(renderedSection, loadedSection);
    }

    @Test
    void setCurrentBar() throws InterruptedException, LineUnavailableException {
        audioPlayerService.loadSection(renderedSection);
        audioPlayerService.setCurrentBar(0);
        System.out.println("loaded");
        Thread.sleep(2000);
        System.out.println("mixerStart");
        mixer.playNextCues();
        Thread.sleep(2000);
    }

    @Test
    void setCurrentBar2() throws InterruptedException, LineUnavailableException {
        audioPlayerService.loadSection(adaptiveSection);
        audioPlayerService.setCurrentBar(0);
        System.out.println("loaded");
        Thread.sleep(2000);
        System.out.println("mixerStart");
        mixer.playNextCues();
        Thread.sleep(2000);
    }

    @Test
    void queueNextMusemes() {
    }
}