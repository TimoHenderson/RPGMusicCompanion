package com.timohenderson.RPGMusicServer;

import com.timohenderson.RPGMusicServer.DirectoryScanner.FileWalker;
import com.timohenderson.RPGMusicServer.models.Museme;
import com.timohenderson.RPGMusicServer.models.Part;
import com.timohenderson.RPGMusicServer.models.Section;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    void canSaveToDB() {
        tuneRepository.deleteAll();
        Museme museme1 = new Museme("cheese.wav", 1, 2);
        Museme museme2 = new Museme("cheese.wav", 1, 2);
        List<Museme> musemes = new ArrayList<>();
        musemes.add(museme1);
        musemes.add(museme2);
        Part melodyPart1 = new Part();
        melodyPart1.setMusemes(musemes);
        Part melodyPart2 = new Part();
        melodyPart2.setMusemes(musemes);
        List<Part> melodyParts = new ArrayList<>();
        melodyParts.add(melodyPart2);
        melodyParts.add(melodyPart1);
        Section section1 = new Section();
        section1.setMelodyParts(melodyParts);
        List<Section> sections = new ArrayList<>();
        sections.add(section1);
        Tune tune1 = new Tune();
        tune1.setSections(sections);
        tune1.setName("Eggy");
        tuneRepository.save(tune1);

    }

//    @Test
//    void canFindPathToResource() {
//        System.out.println(fileWalker.getResourcePath("section_data.json"));
//    }

    @Test
    void canWalkFiles() throws IOException {
        fileWalker.walkFiles();
    }
}
