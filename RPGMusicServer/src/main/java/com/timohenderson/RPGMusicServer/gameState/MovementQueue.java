package com.timohenderson.RPGMusicServer.gameState;

import com.timohenderson.RPGMusicServer.models.Movement;

import java.util.ArrayList;
import java.util.List;

public class MovementQueue {
    private final ArrayList<Movement> nextMovementQueue = new ArrayList();
    private int movementIndex = 0;


    public Movement getNextMovement() {
        if (movementIndex < nextMovementQueue.size()) {
            Movement movement = nextMovementQueue.get(movementIndex);
            movementIndex++;
            return movement;
        }
        return null;
    }

    public void add(List<Movement> movements) {
        nextMovementQueue.addAll(movements);
        movementIndex = 0;
    }

    public void replace(List<Movement> movements) {
        nextMovementQueue.clear();
        add(movements);
    }

    public void putAtStart(List<Movement> movements) {
        nextMovementQueue.addAll(0, movements);
        nextMovementQueue.stream().forEach(m -> System.out.println(m.getName()));
        movementIndex = 0;
    }

    public String getCurrentMovementName() {
        if (movementIndex < nextMovementQueue.size()) {
            return nextMovementQueue.get(movementIndex - 1).getName();
        }
        return null;
    }

    public String getNextMovementName() {
        if (movementIndex < nextMovementQueue.size()) {
            return nextMovementQueue.get(movementIndex).getName();
        }
        return null;
    }
}
