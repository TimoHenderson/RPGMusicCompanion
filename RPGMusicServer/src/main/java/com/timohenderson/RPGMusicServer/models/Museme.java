package com.timohenderson.RPGMusicServer.models;

import java.util.ArrayList;

public class Museme {
    private String fileName;
    private int length;
    private ArrayList<Integer> startBars = new ArrayList<>();

    public Museme() {
    }

    public Museme(String fileName, int length) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
