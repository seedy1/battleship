package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.models.PlayerGameBoard;
import com.sj.batlleship.batlleship.models.Ship;

import java.util.Random;

/**
 * RandomAIStrategy class implements the AIStrategy interface to provide a random AI strategy for placing ships and making moves in a Battleship game.
 */
public class RandomAIStrategy implements AIStrategy{
    private final Random rand = new Random();

    @Override
    public void placeShips(PlayerGameBoard myBoard){
        for(ShipType shipType : ShipType.values()){
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(10);
                int column = rand.nextInt(10);
                Ship ship = shipType.createShip();
                placed = myBoard.placeShip(ship, row, column);
            }
        }
    }

    @Override
    public void makeMove(PlayerGameBoard oppBoard){
        boolean moved = false;
        int attempts = 0;
        // ROW*COL
        int maxAttempts = 100; // Prevent infinite loop

        while(!moved && attempts < maxAttempts){
            int row = rand.nextInt(10);
            int column = rand.nextInt(10);
            System.out.println("AI is attacking (" + row + "," + column + ")");
            moved = oppBoard.receiveAttack(row, column);
            System.out.println("AI's Move result: " + moved);
            attempts++;
        }

        //TODO: add logger since its an important information
        if(!moved){
            System.out.println("AI couldn't find a valid move after " + maxAttempts + " attempts");
        }
    }
}
