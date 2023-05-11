package com.timohenderson.RPGMusicServer.DirectoryScanner;

import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static com.timohenderson.RPGMusicServer.DirectoryScanner.MovementsFactory.buildMovements;

class TunesFactory {
    static List<Tune> buildTunes(Path startPath) throws IOException {
        List<Tune> tunes = Files.list(startPath)
                .map((f) -> {
                    String name = f.getFileName().toString();
                    List<Movement> movements;
                    try {
                        movements = buildMovements(startPath.resolve(name));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new Tune(name, movements);
                })
                .collect(Collectors.toList());
        return tunes;
    }
}
