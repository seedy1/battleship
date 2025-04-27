package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.Orientation;

public class Ship{
    private int size;
    private int startRow;
    private int startCol;
    private Orientation orientation;
    private boolean[] hits;
    private String imageName;

    public Ship(int size, String imageName){
        this.size = size;
        this.imageName = imageName;
        this.hits = new boolean[size];
        this.orientation = Orientation.HORIZONTAL; // default
    }

    public int getSize(){
        return size;
    }

    public Orientation getOrientation(){
        return orientation;
    }
    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setPosition(int row, int col){
        this.startRow = row;
        this.startCol = col;
    }

    public int getRow(){
        return startRow;
    }
    public int getCol(){
        return startCol;
    }
    public String getImageName(){
        return imageName;
    }

    public boolean isSunk(){
        for(boolean hit: hits){
            if(!hit){
                return false;
            }
        }
        return true;
    }

    /**
     * Registers a hit on the ship at the specified index.
     * If the index is within the bounds of the hits array, marks the hit at that index.
     *
     * @param index the index of the hit to register
     */
    public void registerHit(int index){
        if(index >= 0 && index < hits.length){
            hits[index] = true;
        }
    }

}
