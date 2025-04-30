package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.models.PlayerGameBoard;

/**
 * AIStrategy interface represents the behavior of an AI/CPU
 */
public interface AIStrategy{
    /**
     * Places the ships on the PlayerGameBoard according to the strategy implemented.
     * This method is used by AI players to set up their ships on the game board.
     *
     * @param board the PlayerGameBoard object on which the ships are to be placed
     */
    void placeShips(PlayerGameBoard board);

    /**
     * Makes a move on the opponent's game board according to the AI's strategy.
     * This method is used to execute the AI player's move during the game.
     *
     * @param oppBoard the PlayerGameBoard representing the opponent's game board
     */
    void makeMove(PlayerGameBoard oppBoard);
}
