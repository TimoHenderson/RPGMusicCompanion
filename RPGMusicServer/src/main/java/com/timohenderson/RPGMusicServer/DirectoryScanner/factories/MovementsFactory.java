package com.timohenderson.RPGMusicServer.DirectoryScanner.factories;

import com.timohenderson.RPGMusicServer.models.tunes.Movement;
import com.timohenderson.RPGMusicServer.models.tunes.sections.Section;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.factories.SectionFactory.buildSections;

class MovementsFactory {
    static List<Movement> buildMovements(Path tunePath) throws IOException {

        List<Movement> movements = Files.list(tunePath)
                .map((f) -> {
                    String fileName = f.getFileName().toString();
                    int order = parseMovementOrder(fileName);
                    List<Section> sections;
                    try {
                        sections = buildSections(tunePath.resolve(fileName));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new Movement(fileName, order, sections);
                }).sorted(
                        Comparator.comparingInt(Movement::getOrder))
                .collect(Collectors.toList());
        return movements;
    }

    private static int parseMovementOrder(String fileName) {
        String[] parts = fileName.split("_");
        return Integer.parseInt(parts[0]);
    }
}
