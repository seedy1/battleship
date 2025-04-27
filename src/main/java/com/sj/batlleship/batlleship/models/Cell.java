package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.CellState;

/**
 * The Cell class represents a single cell on a game board.
 * It contains information about its position, state, and any ship that may be present on it.
 */
public class Cell{
    private int row;
    private int column;
    private CellState cellState;
    private Ship ship;

    Cell(int row, int column){
        this.cellState = CellState.EMPTY;
        this.row = row;
        this.column = column;
    }

    public CellState getState(){
        return cellState;
    }

    public void setState(CellState state){
        this.cellState = state;
    }

    public Ship getShip(){
        return ship;
    }

    public void setShip(Ship ship){
        this.ship = ship;
    }
}
