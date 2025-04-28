package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.constants.Constants;
import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.models.PlayerGameBoard;
import com.sj.batlleship.batlleship.models.Ship;

import java.util.*;

public class SmartAIStrategy implements AIStrategy{
    private final Random rand = new Random();
    private List<int[]> hitCells = new ArrayList<>();
    private List<int[]> potentialTargets = new ArrayList<>();
    private int currentTargetIndex = 0;
    private boolean isHuntingMode = true;
    private int lastHitRow = -1;
    private int lastHitCol = -1;
    private boolean isHorizontalSearch = true;
//    private List<int[]> potentialTargets = new ArrayList<>();
//    private Queue<int[]> potentialTargets = new LinkedList<>();
//    private int checkerboardIndex = 0;

    /*public SmartAIStrategy(){
        for(int i=0;i<Constants.TEN; i++){
            potentialTargets.add(new int[]{i,i});
            System.out.println("Potential Targets: " + i+","+i);
            // Anti-diagonal (0,9 to 9,0)
            potentialTargets.add(new int[]{i, 9-i});
        }
    }*/

    @Override
    public void placeShips(PlayerGameBoard myBoard){
        // Place ships in a more strategic pattern
        // Place larger ships in the center and smaller ships around the edges
        for (ShipType shipType : ShipType.values()) {
            boolean placed = false;
            int attempts = 0;
            int maxAttempts = 100;

            while (!placed && attempts < maxAttempts) {
                int row, column;
                if (shipType.size >= 4) {
                    // Place larger ships in the center
                    row = 3 + rand.nextInt(4);
                    column = 3 + rand.nextInt(4);
                } else {
                    // Place smaller ships around the edges
                    if (rand.nextBoolean()) {
                        row = rand.nextInt(2) == 0 ? 0 : 8;
                        column = rand.nextInt(10);
                    } else {
                        row = rand.nextInt(10);
                        column = rand.nextInt(2) == 0 ? 0 : 8;
                    }
                }

                Ship ship = shipType.createShip();
                placed = myBoard.placeShip(ship, row, column);
                attempts++;
            }
        }
    }

    @Override
    public void makeMove(PlayerGameBoard oppBoard){
        if(hitCells.isEmpty()){
            System.out.println("No hit");
            makeInitialMove(oppBoard);
        }else{
            System.out.println("Hit");
            makeFollowUpMove(oppBoard);
        }
    }

    private void makeInitialMove(PlayerGameBoard oppBoard){
        // Use a checkers board pattern for initial moves
//        int row = rand.nextInt(5) * 2;
//        int col = rand.nextInt(5) * 2;
//        int row = rand.nextInt(6);  // 0-5
//        int col = row;
        // Generate diagonal pattern targets if not already generated
        if(potentialTargets.isEmpty()){
            for(int i = 0; i < 10; i += 2){
                for(int j = 0; j < 10; j += 2){
                    if((i + j) % 2 == 0){
                        potentialTargets.add(new int[]{i, j});
                    }
                }
            }
            Collections.shuffle(potentialTargets);
        }

        // Make move from potential targets
        if(currentTargetIndex < potentialTargets.size()){
            int[] target = potentialTargets.get(currentTargetIndex);
            boolean hit = oppBoard.receiveAttack(target[0], target[1]);
            if(hit){
                hitCells.add(target);
                lastHitRow = target[0];
                lastHitCol = target[1];
                isHuntingMode = false;
            }
            currentTargetIndex++;
        }

    }

    private void makeFollowUpMove(PlayerGameBoard oppBoard){
        // If we have hit cells, try to find the rest of the ship
        /*
        if (!hitCells.isEmpty()) {
            int[] lastHit = hitCells.get(hitCells.size() - 1);
            int row = lastHit[0];
            int col = lastHit[1];

            // Check adjacent cells
            int[][] adjacentCells = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};

            for (int[] cell : adjacentCells) {
                if (isValidCell(cell[0], cell[1]) && oppBoard.getCell(cell[0], cell[1]).getState() == CellState.EMPTY) {
                    boolean hit = oppBoard.receiveAttack(cell[0], cell[1]);
                    if (hit) {
                        hitCells.add(new int[]{cell[0], cell[1]});
                    }
                    return;
                }
            }
        }
        */
        if(isHuntingMode){
            // Continue with diagonal pattern
            makeInitialMove(oppBoard);
            return;
        }

        // Try to find the rest of the ship
        int[][] adjacentCells = {
                {lastHitRow-1, lastHitCol}, {lastHitRow+1, lastHitCol},
                {lastHitRow, lastHitCol-1}, {lastHitRow, lastHitCol+1}
        };

        // First try horizontal direction
        if(isHorizontalSearch){
            for(int i = 0; i < 2; i++){
                int[] cell = adjacentCells[i];
                if(isValidCell(cell[0], cell[1]) && oppBoard.getCell(cell[0], cell[1]).getState() == CellState.EMPTY){
                    boolean hit = oppBoard.receiveAttack(cell[0], cell[1]);
                    if(hit){
                        hitCells.add(cell);
                        lastHitRow = cell[0];
                        lastHitCol = cell[1];
                        return;
                    }
                }
            }
            // If horizontal search failed, try vertical
            isHorizontalSearch = false;
        }

        // Try vertical direction
        for (int j = 2; j < 4; j++){
            int[] cell = adjacentCells[j];
            // always check isValidCell first
            if(isValidCell(cell[0], cell[1]) && oppBoard.getCell(cell[0], cell[1]).getState() == CellState.EMPTY){
                boolean hit = oppBoard.receiveAttack(cell[0], cell[1]);
                if(hit){
                    hitCells.add(cell);
                    lastHitRow = cell[0];
                    lastHitCol = cell[1];
                    return;
                }
            }
        }

        // If no adjacent cells are available, switch back to hunting mode
        isHuntingMode = true;
        isHorizontalSearch = true;
        makeInitialMove(oppBoard);
    }
    // If no adjacent cells are available, make a random move
    // TODO: take upper or lower diagonal
    /*private void randomAttack(PlayerGameBoard oppBoard){
        boolean moved = false;
        int attempts = 0;
        while(!moved && attempts < 100){
            int row = rand.nextInt(10);
            int col = rand.nextInt(10);
            if(oppBoard.getCell(row, col).getState() == CellState.EMPTY){
                boolean hit = oppBoard.receiveAttack(row, col);
                if(hit){
                    hitCells.add(new int[]{row, col});
                }
                moved = true;
            }
            attempts++;
        }
    }*/

    /**
     * Checks if the given cell coordinates are within the boundaries of a 10x10 grid.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return true if the cell is within the grid, false otherwise
     */
    private boolean isValidCell(int row, int col){
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }
}
