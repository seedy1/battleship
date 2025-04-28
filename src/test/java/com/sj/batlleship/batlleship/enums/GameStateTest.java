package com.sj.batlleship.batlleship.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for testing the GameState enum values.
 */
class GameStateTest{
    
    @Test
    void testGameStateValues(){
        // Test that all expected values exist
        assertEquals(4, GameState.values().length);
        assertNotNull(GameState.valueOf("SETUP"));
        assertNotNull(GameState.valueOf("PLAYER_TURN"));
        assertNotNull(GameState.valueOf("COMPUTER_TURN"));
        assertNotNull(GameState.valueOf("GAME_OVER"));
    }

} 