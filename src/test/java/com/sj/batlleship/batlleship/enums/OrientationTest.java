package com.sj.batlleship.batlleship.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Orientation enum class.
 */
class OrientationTest{
    
    @Test
    void testOrientationValues(){
        // Test that all expected values exist
        assertEquals(2, Orientation.values().length);
        assertNotNull(Orientation.valueOf("HORIZONTAL"));
        assertNotNull(Orientation.valueOf("VERTICAL"));
    }
    
    @Test
    void testOrientationToString(){
        // Test toString returns the enum name
        assertEquals("HORIZONTAL", Orientation.HORIZONTAL.toString());
        assertEquals("VERTICAL", Orientation.VERTICAL.toString());
    }
} 