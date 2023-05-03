package com.timohenderson.RPGMusicServer.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tunes")
public class Tune {
    @Id
    private String Id;
    private String name;
    private List<Movement> movements = new ArrayList<>();
    private int currentMovement = 0;

    public Tune() {
    }

    public Tune(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

    public Movement nextMovement() {
        currentMovement += 1;
        if (currentMovement >= movements.size()) {
            currentMovement = 0;
            return null;
        }
        return movements.get(currentMovement);
    }

    public Movement restartTune() {
        currentMovement = 0;
        return movements.get(currentMovement);
    }

    public Movement getCurrentMovement() {
        return movements.get(currentMovement);
    }

}
