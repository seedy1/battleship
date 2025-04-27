package com.sj.batlleship.batlleship.enums;

import com.sj.batlleship.batlleship.models.Ship;

public enum ShipType{

    BATTLESHIP(4, "battleship.jpg"),
    CARRIER(5, "cru2.jpg");

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
