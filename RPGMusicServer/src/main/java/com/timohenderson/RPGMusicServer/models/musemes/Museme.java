package com.timohenderson.RPGMusicServer.models.musemes;

import java.nio.file.Path;

public class Museme {
    MusemeData musemeData;
    private Path filePath;

    public Museme() {
    }

    public Museme(Path filePath, MusemeData musemeData) {
        this.filePath = filePath;
        this.musemeData = musemeData;
    }

    public Path getFileName() {
        return filePath;
    }


}
