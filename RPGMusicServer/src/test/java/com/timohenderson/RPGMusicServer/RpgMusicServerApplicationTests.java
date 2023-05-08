package com.timohenderson.RPGMusicServer;

import com.timohenderson.RPGMusicServer.DirectoryScanner.FileWalker;
import com.timohenderson.RPGMusicServer.enums.TransitionType;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.SectionData;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RpgMusicServerApplicationTests {
    @Autowired
    TuneRepository tuneRepository;
    @Autowired
    FileWalker fileWalker;


    @Test
    void contextLoads() {
    }

    @Test
    void canWalkFiles() throws IOException {
        List<Tune> tunes = fileWalker.walkFiles();
        System.out.println(tunes);
        tuneRepository.deleteAll();
        tuneRepository.saveAll(tunes);
        List<Tune> tunesFromDB = tuneRepository.findAll();
        System.out.println(tunesFromDB);
        assertEquals(tunes.size(), tunesFromDB.size());
    }

    @Test
    void canCreateSectionData() {
        SectionData sectionData = new SectionData(1, 2, 4, 4, 110, false, true, TransitionType.END);
        System.out.println(sectionData.numBeats());
        assertEquals(4, sectionData.numBeats());
    }
}
