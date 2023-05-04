package com.timohenderson.RPGMusicServer.models.sections;

import com.timohenderson.RPGMusicServer.models.parts.Part;

import java.util.List;

public record PartLists(
        List<Part> melodyParts,
        List<Part> padParts,
        List<Part> bassParts,
        List<Part> highPercParts,
        List<Part> midPercParts,
        List<Part> lowPercParts,
        List<Part> cymbalParts
) {
}
