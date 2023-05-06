package com.timohenderson.RPGMusicServer.repositories;

import com.timohenderson.RPGMusicServer.models.Tune;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TuneRepositoryTest {
    @Autowired
    TuneRepository tuneRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void contextLoads() {
    }

    @Test
    void findByName() {
        Tune mine = tuneRepository.findByName("Abandoned_Mine");
        assertEquals("Abandoned_Mine", mine.getName());
    }

    @Test
    void findAllNames() {
        List<String> names = tuneRepository.findNames();
        assertEquals(3, names.size());

    }
}