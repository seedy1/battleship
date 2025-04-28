package model;

import com.sj.batlleship.batlleship.models.Ship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShipTest{
    @Test
    void testIsSunk(){
        // given
        int shipSize = 5;
        Ship ship = new Ship(shipSize, "battleship.jpg");
        for (int i = 0; i < 5; i++) {
            ship.registerHit(i);
        }
        // when
        boolean result = ship.isSunk();
        // then
        Assertions.assertTrue(result, "Expected the ship to be sunk");
    }
}
