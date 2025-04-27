package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.enums.ShipType;
import java.util.Random;

public class RandomAIPlayer extends Player{
    private final Random rand = new Random();

    @Override
    public void placeShipRandom(){
        for(ShipType shipType : ShipType.values()){
            boolean placed = false;
            while(!placed){
                int row = rand.nextInt(10);
                int column = rand.nextInt(10);
                Ship ship = shipType.createShip();
                placed = gameBoard.placeShip(ship, row, column);
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

        if(!moved){
            System.out.println("AI couldn't find a valid move after " + maxAttempts + " attempts");
        }
    }
}
