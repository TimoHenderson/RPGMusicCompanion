package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.timohenderson.RPGMusicServer.models.Movement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.SectionFactory.buildSections;

class MovementsFactory {
    static List<Movement> buildMovements(Path tunePath) throws IOException {

        List<Movement> movements = Files.list(tunePath)
                .map((f) -> {
                    String fileName = f.getFileName().toString();
                    int order = parseMovementOrder(fileName);
                    return new Movement(fileName, order);
                }).sorted(
                        Comparator.comparingInt(Movement::getOrder))
                .collect(Collectors.toList());

        for (Movement movement : movements) {
            Path movementPath = tunePath.resolve(movement.getName());
            movement.setSections(buildSections(movementPath));
        }

        return movements;
    }

    private static int parseMovementOrder(String fileName) {
        String[] parts = fileName.split("_");
        return Integer.parseInt(parts[0]);
    }
}
