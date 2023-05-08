package com.timohenderson.RPGMusicServer.audio;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import com.timohenderson.RPGMusicServer.services.TimelineService;
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
    TimelineService timelineService;
    Section renderedSection;
    Section adaptiveSection;
    Section combatAdaptiveSection;

    @BeforeEach
    void setUp() {
        Tune tune = tuneRepository.findByName("Enchanted_Forest");
        Movement intro = tune.getMovements().get(0);
        renderedSection = intro.getSections().get(1);
        adaptiveSection = intro.getSections().get(0);

        Tune combat = tuneRepository.findByName("Combat");
        Movement mainCombat = combat.getMovements().get(0);
        combatAdaptiveSection = mainCombat.getSections().get(1);

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
        audioPlayerService.playNextCues(1);
        Thread.sleep(2000);
    }

    @Test
    void setCurrentBar2() throws InterruptedException, LineUnavailableException {
        audioPlayerService.loadSection(adaptiveSection);
        audioPlayerService.setCurrentBar(0);
        System.out.println("loaded");
        Thread.sleep(2000);
        System.out.println("mixerStart");
        audioPlayerService.playNextCues(1);
        Thread.sleep(2000);
    }


    @Test
    void canPlay() throws InterruptedException {
        timelineService.addToSectionQueue(renderedSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptive() throws InterruptedException {
        timelineService.addToSectionQueue(adaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptiveCombat() throws InterruptedException {
        timelineService.addToSectionQueue(combatAdaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void queueNextMusemes() {
    }
}