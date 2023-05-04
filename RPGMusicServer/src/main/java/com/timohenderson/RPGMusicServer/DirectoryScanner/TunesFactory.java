package com.timohenderson.RPGMusicServer.DirectoryScanner;

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
                .map((f) -> new Tune(f.getFileName().toString()))
                .collect(Collectors.toList());

        for (Tune tune : tunes) {
            Path tunePath = startPath.resolve(tune.getName());
            tune.setMovements(buildMovements(tunePath));
        }
        return tunes;
    }
}
