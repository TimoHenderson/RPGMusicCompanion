package com.timohenderson.RPGMusicServer;

import com.timohenderson.RPGMusicServer.DirectoryScanner.FileWalker;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Museme;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.parts.AdaptivePart;
import com.timohenderson.RPGMusicServer.models.parts.LinearPart;
import com.timohenderson.RPGMusicServer.models.parts.Part;
import com.timohenderson.RPGMusicServer.models.sections.AdaptiveSection;
import com.timohenderson.RPGMusicServer.models.sections.RenderedSection;
import com.timohenderson.RPGMusicServer.models.sections.Section;
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
        //tuneRepository.deleteAll();
        //Museme
        Museme museme1 = new Museme("Trumpet1.wav", 2);
        museme1.addStartBar(1);
        museme1.addStartBar(3);
        //Museme
        Museme museme2 = new Museme("Trumpet2.wav", 2);
        museme2.addStartBar(4);
        museme2.addStartBar(5);
        //AdaptivePadPart
        AdaptivePart adaptivePart = new AdaptivePart();
        adaptivePart.addMuseme(museme1);
        adaptivePart.addMuseme(museme2);

        //Museme
        Museme museme3 = new Museme("fullMelody.wav", 4);
        //LinearMelodyPart
        LinearPart melodyPart = new LinearPart();
        melodyPart.setMuseme(museme3);

        //AdaptiveSection
        AdaptiveSection adaptiveSection = new AdaptiveSection();
        ArrayList<Part> melodyParts = new ArrayList<>();
        melodyParts.add(adaptivePart);
        melodyParts.add(melodyPart);
        adaptiveSection.setMelodyParts(melodyParts);

        //Museme
        Museme museme4 = new Museme("IntroRendered", 4);
        //LinearPart
        LinearPart linearPart = new LinearPart();
        linearPart.setMuseme(museme4);
        //RenderedSection
        RenderedSection renderedSection = new RenderedSection();
        renderedSection.setPart(linearPart);

        //Movement
        Movement movement = new Movement();
        List<Section> sections = new ArrayList<>();
        sections.add(renderedSection);
        sections.add(adaptiveSection);
        movement.setSections(sections);

        //Tune
        Tune tune1 = new Tune("Hench_Forest");
        List<Movement> movements = new ArrayList<>();
        movements.add(movement);
        tune1.setMovements(movements);

        tuneRepository.save(tune1);

        List<Tune> tunes = tuneRepository.findAll();

        System.out.println(tunes);

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
