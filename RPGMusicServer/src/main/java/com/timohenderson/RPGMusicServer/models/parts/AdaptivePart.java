package com.timohenderson.RPGMusicServer.models.parts;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;

import java.util.List;

public class AdaptivePart extends Part {

    private List<Museme> musemes;

    public AdaptivePart(PartData partData, List<Museme> musemes) {
        super(partData);
        this.musemes = musemes;
    }

//    public List<Museme> getMusemes() {
//        return musemes;
//    }

//    public void setMusemes(List<Museme> musemes) {
//        this.musemes = musemes;
//    }
//
//    public void addMuseme(Museme museme) {
//        musemes.add(museme);
//    }
}
