package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.audio.AudioPlayerService;
import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;

@SpringBootTest
class AudioPlayerServiceTest {
    @Autowired
    AudioPlayerService audioPlayerService;
    @Autowired
    TimelineService timelineService;
    Section loopingSection;

    @BeforeEach
    void setUp() {
        loopingSection = new RenderedSection(
                "loopingSection",
                new SectionData(1,
                        4,
                        4,
                        4,
                        45,
                        true,
                        true,
                        TransitionType.NEXT_BAR),
                null);
    }

//    @Test
//    void play() throws LineUnavailableException {
//        audioPlayerService.play(1);
//    }

    @Test
    void playOnBarEvents() throws InterruptedException, LineUnavailableException {
        timelineService.addToSectionQueue(loopingSection);
        timelineService.play();
        Thread.sleep(60000);
    }


}