package com.timohenderson.RPGMusicServer.models.tunes.musemes;

import org.springframework.data.annotation.Transient;

import java.net.URL;

public class Museme {
    MusemeData musemeData;
    private String filePath;
    @Transient
    private URL url;


    public Museme(String filePath, MusemeData musemeData) {
        this.filePath = filePath;
        this.musemeData = musemeData;
        this.url = getClass().getResource(filePath);
    }

    public URL getUrl() {
        return url;
    }

    public MusemeData getMusemeData() {
        return musemeData;
    }

    public URL getURL() {
        return url;
    }
}
