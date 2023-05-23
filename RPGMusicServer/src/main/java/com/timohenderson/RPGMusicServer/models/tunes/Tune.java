package com.timohenderson.RPGMusicServer.models.tunes;

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


    public Tune(String name, List<Movement> movements) {
        this.name = name;
        this.movements = movements;
    }

    public String getName() {
        return name;
    }

    public List<Movement> getMovements() {
        return List.copyOf(movements);
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
