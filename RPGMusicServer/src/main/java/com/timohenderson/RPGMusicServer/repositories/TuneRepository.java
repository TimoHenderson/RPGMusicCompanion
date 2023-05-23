package com.timohenderson.RPGMusicServer.repositories;


import com.timohenderson.RPGMusicServer.models.tunes.Tune;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuneRepository extends MongoRepository<Tune, String> {
    Tune findByName(String name);

    @Query(value = "{}", fields = "{ 'name' : 1}")
    List<String> findNames();
}
