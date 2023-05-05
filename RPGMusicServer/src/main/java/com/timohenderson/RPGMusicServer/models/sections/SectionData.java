package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.enums.TransitionType;

public record SectionData(
        int order,
        int numBars,
        int numBeats,
        int beatValue,
        int bpm,
        boolean preRendered,
        boolean loop,
        TransitionType transitionType
) {
}
