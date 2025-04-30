package com.sj.batlleship.batlleship.strategies;

import com.sj.batlleship.batlleship.constants.Constants;
import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.Orientation;
import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.models.PlayerGameBoard;
import com.sj.batlleship.batlleship.models.Ship;

import java.util.*;

public class SmartAIStrategy implements AIStrategy {
    private final Random rand = new Random();

    private List<int[]> hitCells = new ArrayList<>();
    private Set<String> triedFollowUps = new HashSet<>();
    private List<int[]> potentialTargets = new ArrayList<>();
    private int currentTargetIndex = 0;
    private boolean isHuntingMode = true;
    private boolean isHorizontalSearch = true;
    private boolean triedReversing = false;

    private int lastHitRow = -1;
    private int lastHitCol = -1;

    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction confirmedDirection = null;

    @Override
    public void placeShips(PlayerGameBoard myBoard) {
        for (ShipType shipType : ShipType.values()) {
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 100) {
                boolean isHorizontal = rand.nextBoolean();
                int row, col;

                if (shipType.size >= 4) {
                    row = 2 + rand.nextInt(6);
                    col = 2 + rand.nextInt(6);
                } else {
                    row = 1 + rand.nextInt(8);
                    col = 1 + rand.nextInt(8);
                }

                Ship ship = shipType.createShip();
                ship.setOrientation(isHorizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL);
                placed = myBoard.placeShip(ship, row, col);
                attempts++;
            }
        }
    }

    @Override
    public void makeMove(PlayerGameBoard oppBoard) {
        if (hitCells.isEmpty()) {
            makeInitialMove(oppBoard);
        } else {
            makeFollowUpMove(oppBoard);
        }
    }

    private void makeInitialMove(PlayerGameBoard oppBoard) {
        if (potentialTargets.isEmpty()) {
            for (int i = 0; i < 10; i += 2) {
                for (int j = 0; j < 10; j += 2) {
                    if ((i + j) % 2 == 0) {
                        potentialTargets.add(new int[]{i, j});
                    }
                }
            }
            Collections.shuffle(potentialTargets);
        }

        if (currentTargetIndex < potentialTargets.size()) {
            int[] target = potentialTargets.get(currentTargetIndex);
            boolean hit = oppBoard.receiveAttack(target[0], target[1]);
            if (hit) {
                hitCells.add(target);
                lastHitRow = target[0];
                lastHitCol = target[1];
                isHuntingMode = false;
            }
            currentTargetIndex++;
        }
    }

    private void makeFollowUpMove(PlayerGameBoard oppBoard) {
        // If direction is confirmed, follow it
        if (confirmedDirection != null) {
            int[] lastHit = hitCells.get(hitCells.size() - 1);
            int newRow = lastHit[0];
            int newCol = lastHit[1];

            switch (confirmedDirection) {
                case UP -> newRow--;
                case DOWN -> newRow++;
                case LEFT -> newCol--;
                case RIGHT -> newCol++;
            }

            if (isValidCell(newRow, newCol) && oppBoard.getCell(newRow, newCol).getState() == CellState.EMPTY) {
                String key = newRow + "," + newCol;
                if (!triedFollowUps.contains(key)) {
                    boolean hit = oppBoard.receiveAttack(newRow, newCol);
                    triedFollowUps.add(key);
                    if (hit) {
                        hitCells.add(new int[]{newRow, newCol});
                        lastHitRow = newRow;
                        lastHitCol = newCol;
                        return;
                    } else if (!triedReversing) {
                        confirmedDirection = reverseDirection(confirmedDirection);
                        triedReversing = true;
                        return;
                    } else {
                        resetTargetingState();
                        makeInitialMove(oppBoard);
                        return;
                    }
                }
            } else {
                // invalid or already tried — reverse once
                if (!triedReversing) {
                    confirmedDirection = reverseDirection(confirmedDirection);
                    triedReversing = true;
                    return;
                } else {
                    resetTargetingState();
                    makeInitialMove(oppBoard);
                    return;
                }
            }
        }

        // Try to determine direction from hits
        if (hitCells.size() >= 2 && confirmedDirection == null) {
            hitCells.sort(Comparator.comparingInt(a -> a[0] * 10 + a[1]));
            int[] first = hitCells.get(0);
            int[] second = hitCells.get(1);

            if (first[0] == second[0]) {
                confirmedDirection = (second[1] > first[1]) ? Direction.RIGHT : Direction.LEFT;
            } else if (first[1] == second[1]) {
                confirmedDirection = (second[0] > first[0]) ? Direction.DOWN : Direction.UP;
            }
        }

        // Try surrounding cells if no direction yet
        int[][] directions = {
                {lastHitRow - 1, lastHitCol}, // UP
                {lastHitRow + 1, lastHitCol}, // DOWN
                {lastHitRow, lastHitCol - 1}, // LEFT
                {lastHitRow, lastHitCol + 1}  // RIGHT
        };
        Direction[] dirEnums = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        for (int i = 0; i < directions.length; i++) {
            int r = directions[i][0];
            int c = directions[i][1];
            if (isValidCell(r, c) && oppBoard.getCell(r, c).getState() == CellState.EMPTY) {
                boolean hit = oppBoard.receiveAttack(r, c);
                if (hit) {
                    hitCells.add(new int[]{r, c});
                    lastHitRow = r;
                    lastHitCol = c;
                    confirmedDirection = dirEnums[i];
                    return;
                }
            }
        }

        // Nothing worked — reset to hunting
        resetTargetingState();
        makeInitialMove(oppBoard);
    }

    private void resetTargetingState() {
        hitCells.clear();
        confirmedDirection = null;
        triedFollowUps.clear();
        triedReversing = false;
        isHuntingMode = true;
        isHorizontalSearch = true;
    }

    private Direction reverseDirection(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }}
