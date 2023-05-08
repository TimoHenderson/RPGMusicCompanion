package com.timohenderson.RPGMusicServer.models.parts;

import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import org.springframework.data.annotation.Transient;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class AdaptivePart extends Part {
    HashMap<Integer, List<Museme>> musemesByStartBars = new HashMap<>();
    @Transient
    Museme nextMuseme;
    @Transient
    int nextBarToFill = 0;
    @Transient
    boolean needsNextMuseme = true;
    @Transient
    int nextMusemeStartBar = 0;
    @Transient
    boolean isPlaying = false;

    public AdaptivePart(PartData partData, HashMap<Integer, List<Museme>> musemesByStartBars) {
        super(partData);
        this.musemesByStartBars = musemesByStartBars;
    }


    public void chooseNextMuseme(int bar) {
        if (!needsNextMuseme) return;
        if (nextBarToFill == 0) nextBarToFill = bar;
        List<Museme> musemes = null;
        while (musemes == null) {
            musemes = musemesByStartBars.get(nextBarToFill);
            nextMusemeStartBar = nextBarToFill;
            if (musemes == null) incrementNextBarBy(1);
        }
        nextMuseme = musemes.get((int) (Math.random() * musemes.size()));
        incrementNextBarBy(nextMuseme.getMusemeData().length());
        needsNextMuseme = false;
    }

    public URL getURL(int bar) {
        if (bar == nextMusemeStartBar) {
            return nextMuseme.getURL();
        } else return null;
    }

    private void incrementNextBarBy(int amount) {
        nextBarToFill += amount;
        if (nextBarToFill > partData.sectionLength()) nextBarToFill -= partData.sectionLength();
    }
}
