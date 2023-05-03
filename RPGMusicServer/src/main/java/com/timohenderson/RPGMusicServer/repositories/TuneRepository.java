package com.timohenderson.RPGMusicServer.repositories;


import com.timohenderson.RPGMusicServer.models.Tune;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuneRepository extends MongoRepository<Tune, String> {
}
