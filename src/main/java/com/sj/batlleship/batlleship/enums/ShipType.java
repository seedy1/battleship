package com.sj.batlleship.batlleship.enums;

import com.sj.batlleship.batlleship.models.Ship;

/**
 * Enum representing different types of ships with their size and image name.
 */
public enum ShipType{

    CARRIER(5, "carrier.jpg"),
    BATTLESHIP(4, "battleship.jpg"),
    CRUISER(3, "cruiser.jpg"),
    SUBMARINE(3, "submarine.jpg"),
    DESTROYER(2, "destroyer.jpg");

    public final int size;
    public final String imageName;

    ShipType(int size, String imageName){
        this.size = size;
        this.imageName = imageName;
    }

    public Ship createShip(){
        return new Ship(size, imageName);
    }
}
