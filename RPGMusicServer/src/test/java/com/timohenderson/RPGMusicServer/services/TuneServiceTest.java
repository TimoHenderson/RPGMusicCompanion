package com.timohenderson.RPGMusicServer.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TuneServiceTest {
    @Autowired
    TuneService tuneService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void changeCurrentTune() {
        tuneService.loadTune("Abandoned_Mine");
        assertEquals("Abandoned_Mine", tuneService.getCurrentTune().getName());
        tuneService.loadTune("Combat");
        assertEquals("Combat", tuneService.getCurrentTune().getName());
    }

    @Test
    void getListOfTuneNames() {
        List<String> names = tuneService.getListOfTuneNames();
        assertEquals(3, names.size());
    }
}