package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.enums.Orientation;
import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.models.PlayerGameBoard;
import com.sj.batlleship.batlleship.models.Ship;

import java.util.*;

/**
 * Implements the AIStrategy interface to define the behavior of a smart AI player in a game.
 * The SmartAIStrategy class uses a combination of strategies to intelligently place ships
 * on its board and make calculated moves against the opponent.
 */
public class SmartAIStrategy implements AIStrategy{
    private final Random rand = new Random();
    private final List<int[]> checkerPatternTargets = new ArrayList<>();
    private final Queue<int[]> followUpTargets = new LinkedList<>();
    private final Set<String> visited = new HashSet<>();

    public SmartAIStrategy(){
        // Generate checkerboard pattern
        // |x| |x| |x| |
        // | |x| |x| | |
        // |x| |x| |x| |
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                if((row + col) % 2 == 0){
                    checkerPatternTargets.add(new int[]{row, col});
                }
            }
        }
        Collections.shuffle(checkerPatternTargets);
    }

    @Override
    public void placeShips(PlayerGameBoard myBoard){
        for (ShipType shipType : ShipType.values()) {
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 100) {
                boolean isHorizontal = rand.nextBoolean();
                int row = rand.nextInt(10);
                int col = rand.nextInt(10);

                Ship ship = shipType.createShip();
                ship.setOrientation(isHorizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL);
                placed = myBoard.placeShip(ship, row, col);
                attempts++;
            }
        }
        System.out.println("Ships placed by AI.");
    }

    @Override
    public void makeMove(PlayerGameBoard oppBoard){
        if(!followUpTargets.isEmpty()){
            makeFollowUpMove(oppBoard);
        }else{
            makeCheckerboardMove(oppBoard);
        }
    }

    private void makeCheckerboardMove(PlayerGameBoard oppBoard){
        while(!checkerPatternTargets.isEmpty()){
            int[] cell = checkerPatternTargets.removeFirst();
            int row = cell[0], col = cell[1];

            if(visited.add(row + "," + col)){
                boolean hit = oppBoard.receiveAttack(row, col);
                if(hit){
                    addAdjacentCells(row, col);
                }
                return;
            }
        }
    }

    private void makeFollowUpMove(PlayerGameBoard oppBoard){
        while(!followUpTargets.isEmpty()){
            int[] cell = followUpTargets.poll();
            int row = cell[0], col = cell[1];

            // always call isValidCell() first
            if(isValidCell(row, col) && visited.add(row + "," + col)){
                boolean hit = oppBoard.receiveAttack(row, col);
                if(hit){
                    addAdjacentCells(row, col); // expand from new hit
                }
                return;
            }
        }
        makeCheckerboardMove(oppBoard);
    }

    // Adds adjacent cells to the follow-up targets list if they are valid cells
    private void addAdjacentCells(int row, int col){
        if(isValidCell(row - 1, col)){followUpTargets.offer(new int[]{row - 1, col});}
        if(isValidCell(row + 1, col)){followUpTargets.offer(new int[]{row + 1, col});}
        if(isValidCell(row, col - 1)){followUpTargets.offer(new int[]{row, col - 1});}
        if(isValidCell(row, col + 1)){followUpTargets.offer(new int[]{row, col + 1});}
    }

    // Checks if the given cell coordinates are within the boundaries of a 10x10 grid.
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }
}
