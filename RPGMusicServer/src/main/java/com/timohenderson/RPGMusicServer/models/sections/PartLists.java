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
    @Override
    public List<Part> melodyParts() {
        return List.copyOf(melodyParts);
    }

    @Override
    public List<Part> padParts() {
        return List.copyOf(padParts);
    }

    @Override
    public List<Part> bassParts() {
        return List.copyOf(bassParts);
    }

    @Override
    public List<Part> highPercParts() {
        return List.copyOf(highPercParts);
    }

    @Override
    public List<Part> midPercParts() {
        return List.copyOf(midPercParts);
    }

    @Override
    public List<Part> lowPercParts() {
        return List.copyOf(lowPercParts);
    }

    @Override
    public List<Part> cymbalParts() {
        return List.copyOf(cymbalParts);
    }
}
