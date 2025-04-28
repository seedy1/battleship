package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.strategies.AIStrategy;

/**
 * The Player class is an abstract class representing a player in the game.
 * It contains a PlayerGameBoard instance for managing the player's game board.
 */
public abstract class Player{
    protected PlayerGameBoard gameBoard = new PlayerGameBoard();
    protected AIStrategy aiStrategy;
    public PlayerGameBoard getGameBoard(){
        return gameBoard;
    }

    /**
     * Sets up a strategy for the AI player.
     *
     * @param strategy the AIStrategy object representing the AI strategy to be set
     */
    public void setAIStrategy(AIStrategy strategy){
        this.aiStrategy = strategy;
    }
    public abstract void placeShipRandom(); // random AI/CPU
    public abstract void makeMove(PlayerGameBoard oppBoard);
}
