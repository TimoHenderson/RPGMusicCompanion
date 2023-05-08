package com.timohenderson.RPGMusicServer.audio.loaded;

import com.adonax.audiocue.AudioMixer;
import com.timohenderson.RPGMusicServer.models.musemes.Museme;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdaptiveLoadedPart extends LoadedPart {
    HashMap<Integer, List<LoadedMuseme>> loadedMusemesByStartBars = new HashMap<>();
    LoadedMuseme nextMuseme;
    int nextBarToFill = 0;
    boolean needsNextMuseme = true;
    int nextMusemeStartBar = 0;
    boolean isPlaying = false;

    public AdaptiveLoadedPart(AdaptivePart part, AudioMixer mixer) {
        super(part.getPartData());
        // populateMap(part.getMusemes(), mixer);
    }

    private void populateMap(List<Museme> musemes, AudioMixer mixer) {
        musemes.stream()
                .map(m -> {
                    try {
                        return super.loadMuseme(m, mixer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }).forEach(lm -> {
                    for (Integer startBar : lm.getStartBars()) {
                        if (loadedMusemesByStartBars.containsKey(startBar)) {
                            loadedMusemesByStartBars.get(startBar).add(lm);
                        } else {
                            List<LoadedMuseme> newList = new ArrayList<>();
                            newList.add(lm);
                            loadedMusemesByStartBars.put(startBar, newList);
                        }
                    }
                });
    }


    @Override
    public void play(int bar) {
        if (!isPlaying) {
            chooseNextMuseme(bar);
            isPlaying = true;
        }
        if (bar == nextMusemeStartBar) {
            nextMuseme.play();
            needsNextMuseme = true;
        }
    }

    public void reset() {
        nextBarToFill = 0;
        nextMuseme = null;
        isPlaying = false;
    }

    public void chooseNextMuseme(int bar) {
        if (!needsNextMuseme) return;
        if (nextBarToFill == 0) nextBarToFill = bar;
        List<LoadedMuseme> musemes = null;
        while (musemes == null) {
            musemes = loadedMusemesByStartBars.get(nextBarToFill);
            nextMusemeStartBar = nextBarToFill;
            if (musemes == null) incrementNextBarBy(1);
        }
        nextMuseme = musemes.get((int) (Math.random() * musemes.size()));
        incrementNextBarBy(nextMuseme.getLength());

        needsNextMuseme = false;
    }

    private void incrementNextBarBy(int amount) {
        nextBarToFill += amount;
        if (nextBarToFill > sectionLength) nextBarToFill -= sectionLength;
    }

}
