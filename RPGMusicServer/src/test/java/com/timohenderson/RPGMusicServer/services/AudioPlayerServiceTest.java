package com.timohenderson.RPGMusicServer.services;

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
    TimelineService timelineService;
    Section renderedSection;
    Section adaptiveSection;
    Section combatAdaptiveSection;
    Section mineAdaptiveSection;
    Section combatRenderedSection1;
    Section combatRenderedSection2;

    @BeforeEach
    void setUp() {
        Tune tune = tuneRepository.findByName("Enchanted_Forest");
        Movement intro = tune.getMovements().get(0);
        renderedSection = intro.getSections().get(1);
        adaptiveSection = intro.getSections().get(0);

        Tune combat = tuneRepository.findByName("Combat");
        Movement mainCombat = combat.getMovements().get(0);
        combatRenderedSection1 = mainCombat.getSections().get(0);
        combatAdaptiveSection = mainCombat.getSections().get(1);
        combatRenderedSection2 = mainCombat.getSections().get(2);

        Tune mine = tuneRepository.findByName("Abandoned_Mine");
        Movement mainMine = mine.getMovements().get(1);
        mineAdaptiveSection = mainMine.getSections().get(0);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadSection() throws LineUnavailableException {
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
        audioPlayerService.playNextCues();
        Thread.sleep(2000);
    }

    @Test
    void setCurrentBar2() throws InterruptedException, LineUnavailableException {
        audioPlayerService.loadSection(adaptiveSection);
        audioPlayerService.setCurrentBar(0);
        System.out.println("loaded");
        Thread.sleep(2000);
        System.out.println("mixerStart");
        audioPlayerService.playNextCues();
        Thread.sleep(2000);
    }


    @Test
    void canPlay() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(renderedSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptive() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(adaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptiveCombat() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(combatAdaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptiveMine() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(mineAdaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayAdaptiveMineAndStop() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(mineAdaptiveSection);
        timelineService.play();
        Thread.sleep(10000);
        timelineService.stop();
        System.out.println("stopped");
        Thread.sleep(2000);
        System.out.println("end");
    }

    @Test
    void canPlayAdaptiveMineAndStopAndResume() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(mineAdaptiveSection);
        timelineService.play();
        Thread.sleep(10000);
        timelineService.stop();
        System.out.println("stopped");
        Thread.sleep(8000);
        System.out.println("resumed");
        timelineService.resume();
        Thread.sleep(2000);
    }

    @Test
    void canAutoMoveFromRenderedToAdaptiveCombat() throws LineUnavailableException, InterruptedException {
        timelineService.addToSectionQueue(combatRenderedSection1);
        timelineService.addToSectionQueue(combatAdaptiveSection);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canAutoMoveFromRenderedToAdaptiveCombatThenOut() throws LineUnavailableException, InterruptedException {
        timelineService.addToSectionQueue(combatRenderedSection1);
        timelineService.addToSectionQueue(combatAdaptiveSection);
        timelineService.addToSectionQueue(combatRenderedSection2);
        timelineService.play();
        Thread.sleep(5000);
        System.out.println("next");
        timelineService.triggerNextSection();
        Thread.sleep(60000);
    }

    @Test
    void queueNextMusemes() {
    }
}