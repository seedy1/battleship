package com.sj.batlleship.batlleship.enums;

//generate java doc
/**
 * The CellState class defines the state of a cell in a certain context (like a game board).
 * It contains an enumeration CellState that lists all the possible states a cell can have.
 *
 * @author seedy1
 * @version 1.0
 */
//public class CellState{
    /**
     * The cellState enumeration lists the possible states of a cell.
     * It contains the following states:
     * EMPTY: The cell is empty.
     * SHIP: The cell contains a ship.
     * HIT: The cell has been hit.
     * MISS: The cell has been missed.
     */
    public enum CellState{
        EMPTY, SHIP, HIT, MISS
    }
//}
