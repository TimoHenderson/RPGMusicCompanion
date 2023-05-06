package com.timohenderson.RPGMusicServer.models;


import com.timohenderson.RPGMusicServer.enums.MusicalType;

public record PartRequest(
        MusicalType musicalType,
        int numParts,
        int darknessThreshold,
        int intensityThreshold
) {
}
