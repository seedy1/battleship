package com.sj.batlleship.batlleship.controllers;

import com.sj.batlleship.batlleship.constants.CellColors;
import com.sj.batlleship.batlleship.constants.Constants;
import com.sj.batlleship.batlleship.constants.Messages;
import com.sj.batlleship.batlleship.constants.Paths;
import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.enums.Orientation;
import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.GameState;
import com.sj.batlleship.batlleship.models.*;
import com.sj.batlleship.batlleship.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

import org.kordamp.bootstrapfx.BootstrapFX;

/**
 * The Game class manages the main game logic and UI interactions for the Battleship game.
 * It handles ship placement, attacks, and game state transitions.
 */
public class Game{
    private static final String GAME_TITLE = "Battleship Game";

    @FXML private Button rotateShipButton;
    @FXML private Button gameControlButton;
    @FXML private Label shipNameLabel;
    @FXML private ImageView showCurrentShip;
    @FXML private GridPane playerBoard;
    @FXML private GridPane enemyBoard;
    @FXML private Label gameStatusText;

    private boolean useRandomCPU;
    private HumanPlayer humanPlayer;
    private Player AIComputerPlayer;
    private int currentShipIndex = 0;
    private List<ShipType> shipsToDisplay;
    private Orientation currentOrientation = Orientation.HORIZONTAL;
    private GameState currentGameState = GameState.SETUP;
    private boolean gameStarted = false;

    /**
     * Sets whether the computer player should use random or smart AI.
     */
    public void setUseRandomCPU(boolean randomCPU) {
        this.useRandomCPU = randomCPU;
    }

    /**
     * Handles the game control button click event.
     * Starts a new game or resets the current game based on the current state.
     */
    @FXML
    public void handleGameControl(ActionEvent event) {
        if (!gameStarted) {
            startGame(event);
            gameControlButton.setText("Reset Current Game");
            gameStarted = true;
        } else {
            resetGame(event);
            gameControlButton.setText("Start New Game");
            gameStarted = false;
        }
    }

    /**
     * Initializes and starts a new game.
     */
    @FXML
    public void startGame(ActionEvent actionEvent){
        initializePlayers();
        initializeShips();
        setupBoards();
        updateShipPreview();
        
        currentGameState = GameState.SETUP;
        gameStatusText.setText(Messages.PLACE_SHIP_PROMPT);
    }

    /**
     * Initializes the human and computer players.
     */
    private void initializePlayers(){
        humanPlayer = new HumanPlayer();
        AIComputerPlayer = useRandomCPU ? new RandomAIPlayer() : new SmartAIPlayer();
        AIComputerPlayer.placeShipRandom();
    }

    /**
     * Initializes the list of ships to be placed.
     */
    private void initializeShips(){
        shipsToDisplay = List.of(
                ShipType.CARRIER,
                ShipType.BATTLESHIP,
                ShipType.CRUISER,
                ShipType.SUBMARINE,
                ShipType.DESTROYER
        );
    }

    // Sets up both the player and enemy boards.
    private void setupBoards(){
        setupPlayerBoard();
        setupEnemyBoard();
    }

    /**
     * Checks if a player has won the game.
     * @param board The game board to check
     * @param isPlayerWin Whether the win is for the human player
     * @return true if the game is over, false otherwise
     */
    private boolean checkWinner(PlayerGameBoard board, boolean isPlayerWin){
        if(board.isAllShipsSunk()){
            currentGameState = GameState.GAME_OVER;
            gameStatusText.setText(isPlayerWin ? Messages.GAME_WON : Messages.GAME_LOST);
            gameControlButton.setDisable(false);
            gameStarted = true;
            return true;
        }
        return false;
    }

    /**
     * Sets up the player's game board with clickable cells.
     */
    private void setupPlayerBoard(){
        playerBoard.getChildren().clear();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                StackPane stackPane = createBoardCell(i, j, true);
                playerBoard.add(stackPane, i, j);
            }
        }
    }
// TODO: refactor setupPlayerBoard and setupEnemyBoard
    /**
     * Sets up the enemy's game board with clickable cells.
     */
    private void setupEnemyBoard(){
        enemyBoard.getChildren().clear();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                StackPane stackPane = createBoardCell(i, j, false);
                enemyBoard.add(stackPane, j, i);
            }
        }
    }

    /**
     * Creates interactive cell
     */
    private StackPane createBoardCell(int row, int col, boolean isPlayerBoard){
        StackPane cell = new StackPane();
        cell.setPrefSize(Constants.VISUAL_GRID_SIZE, Constants.VISUAL_GRID_SIZE);
        cell.setStyle(CellColors.DEFAULT_CELL_COLOR);

        final int finalRow = row;
        final int finalCol = col;
        cell.setOnMouseClicked(e -> {
            if (isPlayerBoard && currentGameState == GameState.SETUP) {
                handleShipPlacement(finalRow, finalCol);
            } else if (!isPlayerBoard && currentGameState == GameState.PLAYER_TURN) {
                handlePlayerAttack(finalRow, finalCol);
            }
        });

        return cell;
    }

    /**
     * Handles ship placement on the player's board.
     */
    private void handleShipPlacement(int row, int column){
        if(currentShipIndex >= shipsToDisplay.size()){
            return;
        }

        ShipType type = shipsToDisplay.get(currentShipIndex);
        Ship ship = type.createShip();
        ship.setOrientation(currentOrientation);

        if(humanPlayer.getGameBoard().placeShip(ship, row, column)){
            currentShipIndex++;
            updateBoards();
            updateShipPreview();

            if(currentShipIndex >= shipsToDisplay.size()){
                currentGameState = GameState.PLAYER_TURN;
                gameStatusText.setText("Your turn! Click on the enemy board to attack.");
            }
        }
    }

    /**
     * Updates the visual state of both game boards.
     */
    private void updateBoards(){
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                updatePlayerBoardCell(row, col);
                updateEnemyBoardCell(row, col);
            }
        }
    }

    /**
     * Updates a single cell on the player's board.
     */
    private void updatePlayerBoardCell(int row, int col){
        StackPane playerCell = (StackPane) playerBoard.getChildren().get(row * Constants.BOARD_SIZE + col);
        playerCell.getChildren().clear();
        Cell playerCellState = humanPlayer.getGameBoard().getCell(row, col);

        if(playerCellState.getState() == CellState.SHIP){
            addShipImage(playerCell, playerCellState.getShip());
        }else{
            updateCellStyle(playerCell, playerCellState.getState());
        }
    }

    /**
     * Updates a single cell on the enemy's board.
     */
    private void updateEnemyBoardCell(int row, int col) {
        StackPane enemyCell = (StackPane) enemyBoard.getChildren().get(row * Constants.BOARD_SIZE + col);
        enemyCell.getChildren().clear();
        Cell enemyCellState = AIComputerPlayer.getGameBoard().getCell(row, col);
        updateCellStyle(enemyCell, enemyCellState.getState());
    }

    /**
     * Updates the visual style of a cell based on its state.
     */
    private void updateCellStyle(StackPane cell, CellState state){
        switch(state){
            case HIT -> cell.setStyle(CellColors.HIT_CELL_COLOR);
            case MISS -> cell.setStyle(CellColors.MISS_CELL_COLOR);
            default -> cell.setStyle(CellColors.DEFAULT_CELL_COLOR);
        }
    }

    /**
     * Adds a ship image to a cell.
     */
    private void addShipImage(StackPane cell, Ship ship){
        if (ship == null) return;
        try {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream(Paths.SHIP_IMAGES + ship.getImageName())));
            img.setFitWidth(Constants.SHIP_IMAGE_SIZE);
            img.setFitHeight(Constants.SHIP_IMAGE_SIZE);
            img.setRotate(ship.getOrientation() == Orientation.HORIZONTAL ? 90 : 0);
            cell.getChildren().add(img);
        }catch(Exception e){
            System.err.println("Error loading ship image: " + e.getMessage());
        }
    }

    /**
     * Handles a player's attack on the enemy board.
     */
    private void handlePlayerAttack(int row, int column){
        if (currentGameState != GameState.PLAYER_TURN) {
            return;
        }

        Cell targetCell = AIComputerPlayer.getGameBoard().getCell(row, column);
        if (targetCell.getState() == CellState.HIT || targetCell.getState() == CellState.MISS) {
            gameStatusText.setText("You already attacked this cell! Try again.");
            return;
        }
        boolean hit = AIComputerPlayer.getGameBoard().receiveAttack(row, column);
        updateBoards();
        if(checkWinner(AIComputerPlayer.getGameBoard(), true)){
            return;
        }

        gameStatusText.setText(hit ? Messages.HIT_MESSAGE : Messages.MISS_MESSAGE);
        currentGameState = GameState.COMPUTER_TURN;
        AIComputerPlayer.makeMove(humanPlayer.getGameBoard());
        updateBoards();
        if(checkWinner(humanPlayer.getGameBoard(), false)){
            return;
        }
        currentGameState = GameState.PLAYER_TURN;
        gameStatusText.setText("Your turn! Click on the enemy board to attack.");
    }

    /**
     * Resets the game to its initial state.
     */
    @FXML
    public void resetGame(ActionEvent actionEvent) {
        currentGameState = GameState.SETUP;
        currentShipIndex = 0;
        currentOrientation = Orientation.HORIZONTAL;
        gameStarted = false;
        initializePlayers();
        setupBoards();
        updateShipPreview();
        gameStatusText.setText(Messages.PLACE_SHIP_PROMPT);
        gameControlButton.setDisable(false);
    }

    /**
     * Rotates the current ship's orientation.
     */
    @FXML
    public void rotateShip(ActionEvent actionEvent){
        if(currentGameState != GameState.SETUP){
            return;
        }
        currentOrientation = currentOrientation == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;

        if(currentShipIndex < shipsToDisplay.size()){
            ShipType type = shipsToDisplay.get(currentShipIndex);
            Ship ship = type.createShip();
            ship.setOrientation(currentOrientation);
        }
        updateShipPreview();
    }

    /**
     * Updates the ship preview display.
     */
    private void updateShipPreview(){
        if(currentShipIndex < shipsToDisplay.size()){
            ShipType type = shipsToDisplay.get(currentShipIndex);
            try {
                Image shipImage = new Image(getClass().getResourceAsStream(Paths.SHIP_IMAGES + type.imageName));
                showCurrentShip.setImage(shipImage);
                showCurrentShip.setRotate(currentOrientation == Orientation.HORIZONTAL ? 90 : 0);
                shipNameLabel.setText(type.name());
                rotateShipButton.setDisable(false);
            }catch(Exception e){
                System.err.println("Error loading ship preview image: " + e.getMessage());
            }
        }else{
            showCurrentShip.setImage(null);
            shipNameLabel.setText("All ships placed");
            rotateShipButton.setDisable(true);
        }
    }

    /**
     * Handles the quit game button click event.
     */
    @FXML
    public void quitGame(ActionEvent actionEvent){

        // alert dialog to confirm
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadWelcomeScreen(actionEvent);
        }
    }

    /**
     * Loads the welcome screen.
     */
    private void loadWelcomeScreen(ActionEvent actionEvent){
        try {
            Parent root = SceneManager.loadFXML(Paths.WELCOME_SCENE);
            Scene scene = SceneManager.createSceneWithStyles(root, BootstrapFX.bootstrapFXStylesheet());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            SceneManager.setStageProperties(stage, scene, GAME_TITLE);
            SceneManager.setStageIcon(stage, Paths.APP_ICON);
            SceneManager.showStage(stage);
        }catch(IOException e){
            System.err.println("Error loading welcome screen: " + e.getMessage());
        }
    }
}
