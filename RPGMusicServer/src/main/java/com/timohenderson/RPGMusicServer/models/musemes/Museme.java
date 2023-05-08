package com.timohenderson.RPGMusicServer.models.musemes;

import org.springframework.data.annotation.Transient;

import java.net.URL;

public class Museme {
    MusemeData musemeData;
    private String filePath;
    @Transient
    private URL url;

    public Museme() {
    }

    public Museme(String filePath, MusemeData musemeData) {
        this.filePath = filePath;
        this.musemeData = musemeData;
        url = getClass().getResource(filePath);
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
