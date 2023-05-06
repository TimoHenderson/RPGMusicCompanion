package com.timohenderson.RPGMusicServer.services;

import com.timohenderson.RPGMusicServer.models.Tune;
import com.timohenderson.RPGMusicServer.repositories.TuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TuneService {

    @Autowired
    TuneRepository tuneRepository;


    private Tune currentTune;


    public Tune changeCurrentTune(String name) {
        currentTune = tuneRepository.findByName(name);
        return currentTune;
    }

    public List<String> getListOfTuneNames() {
        return tuneRepository.findNames();
    }


}
