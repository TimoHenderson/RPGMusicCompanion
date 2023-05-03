package com.timohenderson.RPGMusicServer.models;

import java.nio.file.Path;
import java.util.ArrayList;

public class Museme {
    private Path filePath;
    private int length;
    private ArrayList<Integer> startBars = new ArrayList<>();

    public Museme() {
    }

    public Museme(Path filePath, int length) {
        this.filePath = filePath;
        this.length = length;
    }

    public Path getFileName() {
        return filePath;
    }

    public void setFileName(Path fileName) {
        this.filePath = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Integer> getStartBars() {
        return startBars;
    }

    public void setStartBars(ArrayList<Integer> startBars) {
        this.startBars = startBars;
    }

    public void addStartBar(int bar) {
        startBars.add(bar);
    }
}
