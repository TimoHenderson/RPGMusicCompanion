package com.timohenderson.RPGMusicServer.models.sections;

public record SectionData(
        int order,
        int numBars,
        int numBeats,
        int beatValue,
        int bpm,
        boolean preRendered,
        boolean loop,
        String transitionType
) {
}
