package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The PlayerGameBoardTest class contains unit tests for the PlayerGameBoard class.
 */
class PlayerGameBoardTest {
    private PlayerGameBoard board;
    private Ship ship;
    
    @BeforeEach
    void setUp(){
        board = new PlayerGameBoard();
        ship = new Ship(3, "test.jpg");
    }
    
    @Test
    void testInitialBoardState() {
        // Check that all cells are initialized as EMPTY
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(CellState.EMPTY, board.getCell(i, j).getState());
            }
        }
    }
    
    @Test
    void testPlaceShipValid() {
        assertTrue(board.placeShip(ship, 0, 0));
        
        // Check that the ship was placed correctly
        for (int i = 0; i < 3; i++) {
            assertEquals(CellState.SHIP, board.getCell(0, i).getState());
            assertEquals(ship, board.getCell(0, i).getShip());
        }
    }
    
    @Test
    void testPlaceShipOutOfBounds() {
        // Try to place ship beyond board boundaries
        assertFalse(board.placeShip(ship, 0, 8)); // Would go beyond column 10
        ship.setOrientation(Orientation.VERTICAL);
        assertFalse(board.placeShip(ship, 8, 0)); // Would go beyond row 10
//        System.out.println("TEST:: "+board.placeShip(ship, 8, 0));
    }
    
    @Test
    void testPlaceShipOverlapping() {
        // Place first ship
        assertTrue(board.placeShip(ship, 0, 0));
        
        // Try to place second ship overlapping
        Ship ship2 = new Ship(3, "test2.jpg");
        assertFalse(board.placeShip(ship2, 0, 1));
    }
    
    @Test
    void testReceiveAttack() {
        // Place a ship
        assertTrue(board.placeShip(ship, 0, 0));
        
        // Test hitting the ship
        assertTrue(board.receiveAttack(0, 0));
        assertEquals(CellState.HIT, board.getCell(0, 0).getState());
        
        // Test missing
        assertTrue(board.receiveAttack(1, 1));
        assertEquals(CellState.MISS, board.getCell(1, 1).getState());
        
        // Test hitting same spot twice
        assertFalse(board.receiveAttack(0, 0));
    }
    
    @Test
    void testIsAllShipsSunk() {
        // Place a ship
        assertTrue(board.placeShip(ship, 0, 0));
        
        // Ship is not sunk initially
        assertFalse(board.isAllShipsSunk());
        
        // Hit all parts of the ship
        board.receiveAttack(0, 0);
        board.receiveAttack(0, 1);
        board.receiveAttack(0, 2);
        
        // Now all ships should be sunk
        assertTrue(board.isAllShipsSunk());
    }

    @Test
    void testPlaceShipDoesNotAffectAdjacentCells() {
        // Place a ship at (0,0)
        assertTrue(board.placeShip(ship, 0, 0));
        
        // Verify that cells adjacent to the ship are not affected
        assertNotEquals(CellState.SHIP, board.getCell(1, 0).getState());
        assertNotEquals(CellState.SHIP, board.getCell(0, 3).getState());
        assertNotEquals(CellState.SHIP, board.getCell(1, 1).getState());
        
        // Verify that cells far from the ship are not affected
        assertNotEquals(CellState.SHIP, board.getCell(5, 5).getState());
        assertNotEquals(CellState.SHIP, board.getCell(9, 9).getState());
    }
} 