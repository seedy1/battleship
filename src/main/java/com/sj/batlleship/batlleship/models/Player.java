package com.sj.batlleship.batlleship.models;

/**
 * The Player class is an abstract class representing a player in the game.
 * It contains a PlayerGameBoard instance for managing the player's game board.
 */
public abstract class Player{
    protected PlayerGameBoard gameBoard = new PlayerGameBoard();
    public PlayerGameBoard getGameBoard(){
        return gameBoard;
    }
    public abstract void placeShipRandom(); // random AI/CPU
    public abstract void makeMove(PlayerGameBoard oppBoard);
}
