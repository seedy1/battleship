package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.strategies.SmartAIStrategy;

public class SmartAIPlayer extends Player{
    public SmartAIPlayer(){
        System.out.println("SmartSIPlayer: constructor");
        setAIStrategy(new SmartAIStrategy());
    }

    @Override
    public void placeShipRandom(){
        aiStrategy.placeShips(gameBoard);
    }

    @Override
    public void makeMove(PlayerGameBoard oppBoard){
        aiStrategy.makeMove(oppBoard);
    }
}
