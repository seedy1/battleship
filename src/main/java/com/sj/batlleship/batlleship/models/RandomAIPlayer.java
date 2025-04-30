package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.strategies.RandomAIStrategy;

public class RandomAIPlayer extends Player{
    public RandomAIPlayer(){
        setAIStrategy(new RandomAIStrategy());
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
