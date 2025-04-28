package com.sj.batlleship.batlleship.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellStateTest{
    
    @Test
    void testCellStateValues(){
        // Test that all expected values exist
        assertEquals(4, CellState.values().length);
        assertNotNull(CellState.valueOf("EMPTY"));
        assertNotNull(CellState.valueOf("SHIP"));
        assertNotNull(CellState.valueOf("HIT"));
        assertNotNull(CellState.valueOf("MISS"));
    }
    
    @Test
    void testCellStateToString(){
        // Test toString returns the enum name
        assertEquals("EMPTY", CellState.EMPTY.toString());
        assertEquals("SHIP", CellState.SHIP.toString());
        assertEquals("HIT", CellState.HIT.toString());
        assertEquals("MISS", CellState.MISS.toString());
    }
} 