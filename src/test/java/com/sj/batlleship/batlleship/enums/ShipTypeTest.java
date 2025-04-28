package com.sj.batlleship.batlleship.enums;

import com.sj.batlleship.batlleship.models.Ship;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShipTypeTest{
    
    @Test
    void testShipTypeValues(){
        // Test that all expected values exist
        assertEquals(2, ShipType.values().length);
        assertNotNull(ShipType.valueOf("BATTLESHIP"));
        assertNotNull(ShipType.valueOf("CARRIER"));
    }
    
    @Test
    void testShipTypeProperties(){
        // Test the properties of each ship type
        assertEquals(4, ShipType.BATTLESHIP.size);
        assertEquals("battleship.jpg", ShipType.BATTLESHIP.imageName);
        
        assertEquals(5, ShipType.CARRIER.size);
        assertEquals("cru2.jpg", ShipType.CARRIER.imageName);
    }
    
    @Test
    void testCreateShip(){
        // Test the createShip method
        Ship battleship = ShipType.BATTLESHIP.createShip();
        assertNotNull(battleship);
        assertEquals(4, battleship.getSize());
        assertEquals("battleship.jpg", battleship.getImageName());
        
        Ship carrier = ShipType.CARRIER.createShip();
        assertNotNull(carrier);
        assertEquals(5, carrier.getSize());
        assertEquals("cru2.jpg", carrier.getImageName());
    }
    
    @Test
    void testShipTypeToString(){
        // Test toString returns the enum name
        assertEquals("BATTLESHIP", ShipType.BATTLESHIP.toString());
        assertEquals("CARRIER", ShipType.CARRIER.toString());
    }
} 