package com.timohenderson.RPGMusicServer.models.musemes;

public class Museme {
    MusemeData musemeData;
    private String filePath;

    public Museme() {
    }

    public Museme(String filePath, MusemeData musemeData) {
        this.filePath = filePath;
        this.musemeData = musemeData;
    }

    public String getFilePath() {
        return filePath;
    }

    public MusemeData getMusemeData() {
        return musemeData;
    }
}
