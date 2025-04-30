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
    private static final int BOARD_SIZE = 10;
    private static final double VISUAL_GRID_SIZE = 38.0;
    private static final double SHIP_IMAGE_SIZE = 30.0;
    private static final String SHIP_IMAGE_PATH = "/com/sj/batlleship/batlleship/images/";

    private final Cell[][] grid;
    private final List<Ship> ships;

    /**
     * Constructs a new PlayerGameBoard with an empty grid and no ships.
     */
    public PlayerGameBoard(){
        this.grid = new Cell[BOARD_SIZE][BOARD_SIZE];
        this.ships = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the grid with empty cells.
     */
    private void initializeGrid(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
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
        if (!isValidPlacement(ship, row, col)) {
            return false;
        }
        placeShipOnGrid(ship, row, col);
        ships.add(ship);
        return true;
    }

    /**
     * Checks if a ship can be placed at the specified coordinates.
     */
    private boolean isValidPlacement(Ship ship, int row, int col){
        int size = ship.getSize();
        Orientation orientation = ship.getOrientation();

        if(orientation == Orientation.HORIZONTAL && col + size > BOARD_SIZE){
            return false;
        }
        if(orientation == Orientation.VERTICAL && row + size > BOARD_SIZE){
            return false;
        }

        return isAreaEmpty(ship, row, col);
    }

    /**
     * Checks if the area where the ship will be placed is empty.
     */
    private boolean isAreaEmpty(Ship ship, int row, int col){
        int size = ship.getSize();
        Orientation orientation = ship.getOrientation();
        for(int i = 0; i < size; i++){
            int r = orientation == Orientation.HORIZONTAL ? row : row + i;
            int c = orientation == Orientation.HORIZONTAL ? col + i : col;
            if(grid[r][c].getState() != CellState.EMPTY){
                return false;
            }
        }
        return true;
    }

    /**
     * Places the ship on the grid at the specified coordinates.
     */
    private void placeShipOnGrid(Ship ship, int row, int col){
        int size = ship.getSize();
        Orientation orientation = ship.getOrientation();
        for (int i = 0; i < size; i++){
            int r = orientation == Orientation.HORIZONTAL ? row : row + i;
            int c = orientation == Orientation.HORIZONTAL ? col + i : col;
            grid[r][c].setState(CellState.SHIP);
            grid[r][c].setShip(ship);
        }
        ship.setPosition(row, col);
    }

    /**
     * Displays the game board on a JavaFX GridPane.
     *
     * @param gridPane the GridPane to display the board on
     * @param showShips whether to show the ships on the board
     */
    public void displayOnGrid(GridPane gridPane, boolean showShips){
        gridPane.getChildren().clear();
        for(int row = 0; row < BOARD_SIZE; row++){
            for(int col = 0; col < BOARD_SIZE; col++){
                StackPane cellPane = createCellPane(grid[row][col], showShips);
                gridPane.add(cellPane, col, row);
            }
        }
    }

    /**
     * Creates a StackPane representing a cell on the board.
     */
    private StackPane createCellPane(Cell cell, boolean showShips) {
        StackPane cellPane = new StackPane();
        cellPane.setPrefSize(VISUAL_GRID_SIZE, VISUAL_GRID_SIZE);
        cellPane.setStyle(CellColors.DEFAULT_CELL_COLOR);
        if(cell.getState() == CellState.SHIP && showShips){
            addShipImage(cellPane, cell.getShip());
        }else if(cell.getState() == CellState.HIT){
            cellPane.setStyle(CellColors.HIT_CELL_COLOR);
        }else if(cell.getState() == CellState.MISS){
            cellPane.setStyle(CellColors.MISS_CELL_COLOR);
        }
        return cellPane;
    }

    /**
     * Adds a ship image to a cell pane.
     */
    private void addShipImage(StackPane cellPane, Ship ship){
        if (ship == null){
            return;
        }

        try {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream(SHIP_IMAGE_PATH + ship.getImageName())));
            img.setFitWidth(SHIP_IMAGE_SIZE);
            img.setFitHeight(SHIP_IMAGE_SIZE);
            cellPane.getChildren().add(img);
        } catch (Exception e) {
            System.err.println("Error loading ship image: " + e.getMessage());
        }
    }

    /**
     * Processes an attack on the board at the specified coordinates.
     *
     * @param row the row coordinate
     * @param column the column coordinate
     * @return true if the attack was valid and processed, false otherwise
     */
    public boolean receiveAttack(int row, int column){
        Cell attackCell = grid[row][column];
        if (attackCell.getState() == CellState.HIT || attackCell.getState() == CellState.MISS) {
            return false;
        }
        if (attackCell.getState() == CellState.SHIP) {
            processHit(attackCell, row, column);
            return true;
        } else if (attackCell.getState() == CellState.EMPTY) {
            attackCell.setState(CellState.MISS);
            return true;
        }
        return false;
    }

    /**
     * Processes a hit on a ship.
     */
    private void processHit(Cell cell, int row, int column){
        cell.setState(CellState.HIT);
        Ship ship = cell.getShip();
        if (ship != null) {
            int hitIndex = calculateHitIndex(ship, row, column);
            ship.registerHit(hitIndex);
        }
    }

    /**
     * Calculates which part of the ship was hit.
     */
    private int calculateHitIndex(Ship ship, int row, int column){
        return ship.getOrientation() == Orientation.HORIZONTAL ? column - ship.getCol() : row - ship.getRow();
    }

    /**
     * Checks if all ships on the board are sunk.
     *
     * @return true if all ships are sunk, false otherwise
     */
    public boolean isAllShipsSunk(){
        return ships.stream().allMatch(Ship::isSunk);
    }

    public Cell getCell(int row, int col){
        return grid[row][col];
    }

//    public Cell[][] getCells() {
//        return grid;
//    }
}
