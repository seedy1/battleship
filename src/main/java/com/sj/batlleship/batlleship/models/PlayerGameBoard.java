package com.sj.batlleship.batlleship.models;

import com.sj.batlleship.batlleship.constants.CellColors;
import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerGameBoard class represents the player's game board in a Battleship game.
 * It manages the grid of cells, placement of ships, tracking of ship positions, receiving attacks,
 * and checking game state.
 */
public class PlayerGameBoard {
    private Cell[][] grid = new Cell[10][10];
    private List<Ship> ships = new ArrayList<>();
    final static double visualGridSize = 38.0;
    // get grids for easy tracking
    PlayerGameBoard(){
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Attempts to place a ship on the game board at the specified row and column coordinates with the given orientation.
     *
     * @param ship the Ship object to be placed on the board
     * @param row the row coordinate where the ship will be placed
     * @param col the column coordinate where the ship will be placed
     * @return true if the ship is successfully placed, false otherwise
     */
    public boolean placeShip(Ship ship, int row, int col){
        System.out.println("Attempting to place ship: " + ship.getImageName() + " at (" + row + "," + col + ") with orientation: " + ship.getOrientation());
        int size = ship.getSize();
        Orientation ori = ship.getOrientation();
        if(ori == Orientation.HORIZONTAL && col + size > 10){
            System.out.println("Ship placement failed: Would exceed board width");
            return false;
        }
        if(ori == Orientation.VERTICAL && row + size > 10){
            System.out.println("Ship placement failed: Would exceed board height");
            return false;
        }
        for(int i = 0; i < size; i++){
            int r = ori == Orientation.HORIZONTAL ? row : row + i;
            int c = ori == Orientation.HORIZONTAL ? col + i : col;
            if (grid[r][c].getState() != CellState.EMPTY) {
                System.out.println("Ship placement failed: Cell at (" + r + "," + c + ") is not empty");
                return false;
            }
        }
        for(int i = 0; i < size; i++){
            int r = ori == Orientation.HORIZONTAL ? row : row + i;
            int c = ori == Orientation.HORIZONTAL ? col + i : col;
            grid[r][c].setState(CellState.SHIP);
            grid[r][c].setShip(ship);
            System.out.println("Placed ship part at (" + r + "," + c + ")");
        }
        ship.setPosition(row, col);
        ships.add(ship);
        System.out.println("Ship placement successful");
        return true;
    }

    public void placeShipInteractive(int row, int col){
        Ship ship = new Ship(3, "cru2.jpg"); // example ship, fixed size for demo
        placeShip(ship, row, col);
    }

//    for debugging purposes
    public void displayOnGrid(GridPane gridPane, boolean showShips){
        System.out.println("Displaying grid with showShips=" + showShips);
        // Clear the grid first
        gridPane.getChildren().clear();
        /*for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = grid[i][j];
                ImageView imageView = new ImageView();
                if (cell.getState() == CellState.SHIP && showShips) {

                }
            }
        }*/
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                Cell cell = grid[row][col];
                StackPane cellPane = new StackPane();
                cellPane.setPrefSize(visualGridSize, visualGridSize);
//                cellPane.setStyle("-fx-border-color: black; -fx-background-color: #908a8a;");
                cellPane.setStyle(CellColors.DEFAULT_CELL_COLOR);

                if(cell.getState() == CellState.SHIP && showShips){
                    System.out.println("Found ship at (" + row + "," + col + ")");
                    Ship ship = cell.getShip();
                    if(ship != null){
                        System.out.println("Loading ship image: " + ship.getImageName());
                        try{
                            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/com/sj/batlleship/batlleship/images/" + ship.getImageName())));
                            img.setFitWidth(30);
                            img.setFitHeight(30);
                            cellPane.getChildren().add(img);
                            System.out.println("Ship image added to cell");
                        }catch(Exception e){
                            System.out.println("Error loading ship image: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }else if(cell.getState() == CellState.HIT){
                    cellPane.setStyle(CellColors.HIT_CELL_COLOR);
                }else if(cell.getState() == CellState.MISS){
                    cellPane.setStyle(CellColors.MISS_CELL_COLOR);
                }
                gridPane.add(cellPane, col, row);
            }
        }
    }

    public void printBoardToConsole(){
        System.out.println("Computer Board (Console View):");
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                Cell cell = grid[row][col];
                switch(cell.getState()){
                    case EMPTY -> System.out.print(". ");
                    case SHIP -> System.out.print("S ");
                    case HIT -> System.out.print("X ");
                    case MISS -> System.out.print("O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean receiveAttack(int row, int column){
        Cell attackCell = grid[row][column];
        // Check if the cell has already been attacked
        if(attackCell.getState() == CellState.HIT || attackCell.getState() == CellState.MISS){
            return false;
        }

        if(attackCell.getState() == CellState.SHIP){
            attackCell.setState(CellState.HIT);
            Ship ship = attackCell.getShip();
            if(ship != null){
                // Calculate which part of the ship was hit
                int hitIndex;
                if(ship.getOrientation() == Orientation.HORIZONTAL){
                    hitIndex = column - ship.getCol();
                }else{
                    hitIndex = row - ship.getRow();
                }
                ship.registerHit(hitIndex);
                System.out.println("Ship hit at index " + hitIndex + " of " + ship.getSize());
            }
            return true;
        }else if(attackCell.getState() == CellState.EMPTY){
            attackCell.setState(CellState.MISS);
            return true;
        }
        return false;// handle better; maybe just if e;lse if needed
    }

    /**
     * Checks if all ships on a game board are sunk.
     * @return true if all ships are sunk, false otherwise
     */
    public boolean isAllShipsSunk(){
        for(Ship ship: ships){
            if(!ship.isSunk()){
                return false;
            }
        }
        return true;
    }

    public Cell getCell(int row, int col){
        return grid[row][col];
    }

    public Cell[][] getCells(){
        return grid;
    }

}
