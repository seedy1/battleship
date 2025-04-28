package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.models.PlayerGameBoard;

public interface AIStrategy{
    void placeShips(PlayerGameBoard board);
    void makeMove(PlayerGameBoard oppBoard);
}
