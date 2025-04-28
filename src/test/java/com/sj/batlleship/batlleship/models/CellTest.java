package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The CellTest class contains unit tests for the Cell class.
 */
class CellTest{
    private Cell cell;
    
    @BeforeEach
    void setUp(){
        cell = new Cell(0, 0);
    }
    
    @Test
    void testInitialState(){
        assertEquals(CellState.EMPTY, cell.getState());
        assertNull(cell.getShip());
    }

    @Test
    void testSetState(){
        cell.setState(CellState.SHIP);
        assertEquals(CellState.SHIP, cell.getState());
        
        cell.setState(CellState.HIT);
        assertEquals(CellState.HIT, cell.getState());
        
        cell.setState(CellState.MISS);
        assertEquals(CellState.MISS, cell.getState());
    }
    
    @Test
    void testSetAndGetShip(){
        Ship ship = new Ship(3, "test.jpg");
        cell.setShip(ship);
        assertEquals(ship, cell.getShip());
    }

    @Test
    void testSetShipDoesNotChangeState(){
        // Set initial state
        cell.setState(CellState.EMPTY);
        CellState initialState = cell.getState();
        
        // Set a ship
        Ship ship = new Ship(3, "test.jpg");
        cell.setShip(ship);
        
        // Verify the state hasnt changed
        assertNotEquals(CellState.SHIP, cell.getState());
        assertEquals(initialState, cell.getState());
    }
} 