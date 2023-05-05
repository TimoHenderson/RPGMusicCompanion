package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TimelineParamsTest {
    TimelineParams timelineParams;

    @BeforeEach
    public void setUp() throws Exception {
        SectionData sectionData = new SectionData(1, 4, 7, 8, 150, false, false, TransitionType.END);
        timelineParams = new TimelineParams(sectionData);
    }

    @Test
    public void hasNumBars() {
        assertEquals(4, timelineParams.getNumBars());
    }

    @Test
    public void hasBarLength() {
        assertEquals(2800.0, timelineParams.getBarLength(), 0.0);
    }

    @Test
    public void hasTransitionType() {
        assertEquals(TransitionType.END, timelineParams.getTransitionType());
    }
}