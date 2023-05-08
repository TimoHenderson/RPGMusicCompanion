package com.timohenderson.RPGMusicServer.components;

import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.audio.loaded.AdaptiveLoadedPart;
import com.timohenderson.RPGMusicServer.enums.MusicalType;
import com.timohenderson.RPGMusicServer.events.EventsHandler;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import com.timohenderson.RPGMusicServer.services.TimelineService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AdaptiveLoadedPartTest {
    @Autowired
    TuneRepository tuneRepository;
    @Autowired
    TimelineService timelineService;
    @Autowired
    EventsHandler eventsHandler;
    AudioMixer audioMixer;


    @BeforeEach
    void setUp() throws LineUnavailableException, InterruptedException {
        audioMixer = new AudioMixer();


        audioMixer.start();
    }

    @AfterEach
    void tearDown() {
        audioMixer.stop();
    }

    @Test
    void canPlayMineStoryAllParts() throws InterruptedException, LineUnavailableException {
        Tune tune = tuneRepository.findByName("Abandoned_Mine");
        Movement movement = tune.getMovements().get(1);
        AdaptiveSection section = (AdaptiveSection) movement.getSections().get(0);
        timelineService.addToSectionQueue(section);
        loadParts(section);
        timelineService.play();
        Thread.sleep(60000);

    }

    @Test
    void canPlayCombatStoryAllParts() throws InterruptedException, LineUnavailableException {
        Tune tune = tuneRepository.findByName("Combat");
        Movement movement = tune.getMovements().get(0);
        AdaptiveSection section = (AdaptiveSection) movement.getSections().get(1);
        loadParts(section);
        timelineService.addToSectionQueue(section);
        timelineService.play();
        Thread.sleep(60000);
    }

    @Test
    void canPlayCombatStoryJustLowPerc() throws InterruptedException, LineUnavailableException {
        Tune tune = tuneRepository.findByName("Combat");
        Movement movement = tune.getMovements().get(0);
        AdaptiveSection section = (AdaptiveSection) movement.getSections().get(1);
        loadParts(MusicalType.LOW_PERC, section);
        timelineService.play();
        timelineService.addToSectionQueue(section);
        Thread.sleep(60000);
    }

    @Test
    void canPlayCombatStoryJustPerc() throws InterruptedException, LineUnavailableException {
        Tune tune = tuneRepository.findByName("Combat");
        Movement movement = tune.getMovements().get(0);
        AdaptiveSection section = (AdaptiveSection) movement.getSections().get(1);
        List<MusicalType> musicalTypes = new ArrayList<>();
        musicalTypes.add(MusicalType.LOW_PERC);
        musicalTypes.add(MusicalType.HIGH_PERC);
        musicalTypes.add(MusicalType.MID_PERC);
        musicalTypes.add(MusicalType.CYMBAL);
        loadParts(musicalTypes, section);
        timelineService.play();
        timelineService.addToSectionQueue(section);
        Thread.sleep(60000);
    }

    public void loadParts(List<MusicalType> musicalTypes, AdaptiveSection section) throws InterruptedException {
        for (MusicalType musicalType : musicalTypes) {
            loadParts(musicalType, section);
        }
    }

    public void loadParts(MusicalType musicalType, AdaptiveSection section) throws InterruptedException {
        List<AdaptivePart> adaptiveParts = section.getPartLists().get(musicalType);
        for (AdaptivePart adaptivePart : adaptiveParts) {
            AdaptiveLoadedPart adaptiveLoadedPart = new AdaptiveLoadedPart(adaptivePart, audioMixer);
            eventsHandler.subscribeToBarEvent(adaptiveLoadedPart);
        }
    }

    public void loadParts(AdaptiveSection section) throws InterruptedException {
        Map<MusicalType, List<AdaptivePart>> partLists = section.getPartLists();
        for (MusicalType musicalType : partLists.keySet()) {
            loadParts(musicalType, section);
        }

    }
}