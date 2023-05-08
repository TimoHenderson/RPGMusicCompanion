package com.timohenderson.RPGMusicServer.audio;

import com.timohenderson.RPGMusicServer.models.sections.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudioPlayerService {

    @Autowired
    RPGMixer mixer;

    Section section;


    public void loadSection(Section section) {
        this.section = section;
    }

    public void queueNextMusemes() {
//        section.getNextMusemeURLs(nextBar).forEach((url) -> {
//            try {
//                mixer.addAudioCue(AudioCue.makeStereoCue(url, 1));
//            } catch (LineUnavailableException e) {
//                e.printStackTrace();
//            }
//        });
    }

    public void stop() {
    }
}
