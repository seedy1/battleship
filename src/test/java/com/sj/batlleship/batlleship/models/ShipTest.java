package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.Orientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Ship class, testing various functionalities such as ship sinking, initial state, orientation, position,
 * registering hits, and hit boundaries.
 */
class ShipTest{
    private Ship ship;
    
    @BeforeEach
    void setUp(){
        ship = new Ship(3, "test.jpg");
    }

    @Test
    void testIsSunk(){
        // given a bigger ship
        int shipSize = 5;
        Ship ship = new Ship(shipSize, "battleship.jpg");
        for (int i = 0; i < shipSize; i++) {
            ship.registerHit(i);
        }
        // when
        boolean result = ship.isSunk();
        // then
        assertTrue(result);
        assertTrue(result, "Expected the ship to be sunk");
    }

    @Test
    void testIsSunkNot(){ // TODO: rename
        // given
        int shipSize = 5;
        Ship ship = new Ship(shipSize, "battleship.jpg");
        for (int i = 3; i < shipSize; i++) {
            ship.registerHit(i);
        }
        // when
        boolean result = ship.isSunk();
        // then
        Assertions.assertNotEquals(result, true);
    }

    @Test
    void testInitialState(){
        assertEquals(3, ship.getSize());
        assertEquals("test.jpg", ship.getImageName());
        assertEquals(Orientation.HORIZONTAL, ship.getOrientation());
        assertFalse(ship.isSunk());
    }
    
    @Test
    void testSetOrientation(){
        ship.setOrientation(Orientation.VERTICAL);
        assertEquals(Orientation.VERTICAL, ship.getOrientation());
    }
    
    @Test
    void testSetPosition(){
        ship.setPosition(5, 7);
        assertEquals(5, ship.getRow());
        assertEquals(7, ship.getCol());
    }
    
    @Test
    void testRegisterHit(){
        // Register hits on all positions
        ship.registerHit(0);
        ship.registerHit(1);
        ship.registerHit(2);
        
        assertTrue(ship.isSunk());
    }
    
    @Test
    void testRegisterHitOutOfBounds(){
        // Test hitting out of bounds
        ship.registerHit(-1); // Should not throw exception
        ship.registerHit(3);  // Should not throw exception
        
        assertFalse(ship.isSunk());
    }
    
    @Test
    void testPartialHits(){
        // Register only some hits
        ship.registerHit(0);
        ship.registerHit(1);
        
        assertFalse(ship.isSunk());
    }

    @Test
    void testOrientationChangeDoesNotAffectPosition(){
        // Set initial position
        ship.setPosition(5, 7);
        int initialRow = ship.getRow();
        int initialCol = ship.getCol();
        
        // Change orientation
        ship.setOrientation(Orientation.VERTICAL);
        
        // Verify position hasn't changed
        assertNotEquals(Orientation.HORIZONTAL, ship.getOrientation());
        assertEquals(initialRow, ship.getRow());
        assertEquals(initialCol, ship.getCol());
    }
} 