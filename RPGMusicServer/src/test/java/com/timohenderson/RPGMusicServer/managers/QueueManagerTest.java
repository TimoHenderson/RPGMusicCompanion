package com.timohenderson.RPGMusicServer.managers;

import com.google.gson.Gson;
import com.timohenderson.RPGMusicServer.models.tunes.Tune;
import com.timohenderson.RPGMusicServer.services.TuneService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QueueManagerTest {
    @Autowired
    TuneService tuneService;

    QueueManager qm;

    @BeforeEach
    void setUp() {
        qm = new QueueManager();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadTune() {
        Tune tune = tuneService.loadTune("Enchanted_Forest");
        qm.loadTune(tune);
        System.out.println(qm.getMovements().toString());
        Gson gson = new Gson();
        String json = gson.toJson(qm);
        System.out.println(json);
    }

    @Test
    void loadTuneNow() {
    }
}