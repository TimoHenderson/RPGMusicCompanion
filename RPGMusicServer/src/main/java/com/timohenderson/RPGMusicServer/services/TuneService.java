package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.DirectoryScanner.FileWalker;
import com.timohenderson.RPGMusicServer.models.Movement;
import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.models.sections.Section;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TuneService {

    @Autowired
    TuneRepository tuneRepository;
    @Autowired
    FileWalker fileWalker;
    private Tune currentTune;
    private int movementIndex = 0;
    private int sectionIndex = 0;


    public Tune loadTune(String name) {
        currentTune = tuneRepository.findByName(name);
        return currentTune;
    }

    public List<String> getListOfTuneNames() {
        return tuneRepository.findNames();
    }

    public Tune getCurrentTune() {
        return currentTune;
    }

    public Movement getCurrentMovement() {
        return currentTune.getCurrentMovement();
    }

    public List<Section> getCurrentSections() {
        return currentTune.getCurrentMovement().getSections();
    }

    @EventListener
    public void onContextRefreshedEvent(ContextRefreshedEvent event) throws IOException {
        List<Tune> tunes = fileWalker.walkFiles();
        tuneRepository.deleteAll();
        tuneRepository.saveAll(tunes);
    }


}
