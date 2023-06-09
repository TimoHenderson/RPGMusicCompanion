package com.timohenderson.RPGMusicServer.models.tunes.parts;

import com.timohenderson.RPGMusicServer.models.tunes.musemes.Museme;
import org.javatuples.Pair;
import org.springframework.data.annotation.Transient;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class AdaptivePart extends Part {
    HashMap<Integer, List<Museme>> musemesByStartBars = new HashMap<>();
    @Transient
    int currentMusemeLength = 0;
    @Transient
    int barsPassedSinceMusemeLoaded = 0;


    public AdaptivePart(PartData partData, HashMap<Integer, List<Museme>> musemesByStartBars) {
        super(partData);
        this.musemesByStartBars = musemesByStartBars;
    }

    public Museme chooseNextMuseme(int bar) {
        barsPassedSinceMusemeLoaded++;
        if (barsPassedSinceMusemeLoaded == currentMusemeLength || currentMusemeLength == 0) {
            List<Museme> potentialMusemes = musemesByStartBars.get(bar);
            if (potentialMusemes == null) return null;
            return potentialMusemes.get((int) (Math.random() * potentialMusemes.size()));
        }

        return null;
    }

    @Override
    public Pair<PartData, URL> getURL(int bar) {
        Museme museme = chooseNextMuseme(bar);
        if (museme != null) {
            currentMusemeLength = museme.getMusemeData().length();
            barsPassedSinceMusemeLoaded = 0;
            return new Pair<>(partData, museme.getURL());
        }
        return null;
    }

    public void reset() {
        currentMusemeLength = 0;
        barsPassedSinceMusemeLoaded = 0;
    }


}
