package com.sj.batlleship.batlleship.controllers;

import com.sj.batlleship.batlleship.enums.ShipType;
import com.sj.batlleship.batlleship.enums.Orientation;
import com.sj.batlleship.batlleship.enums.CellState;
import com.sj.batlleship.batlleship.enums.GameState;
import com.sj.batlleship.batlleship.models.HumanPlayer;
import com.sj.batlleship.batlleship.models.RandomAIPlayer;
import com.sj.batlleship.batlleship.models.Ship;
import com.sj.batlleship.batlleship.models.Cell;
import com.sj.batlleship.batlleship.models.PlayerGameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

import org.kordamp.bootstrapfx.BootstrapFX;


public class Game{

    private boolean useRandomCPU = true;
    private HumanPlayer humanPlayer;
    private RandomAIPlayer randomAIPlayer;
    final static double visualGridSize = 38.0;
    private int currentShipIndex = 0;
    private List<ShipType> shipsToDisplay;
    private Orientation currentOrientation = Orientation.HORIZONTAL;
    private GameState currentGameState = GameState.SETUP;
    @FXML
    public Button gameControlButton;
    private boolean gameStarted = false;

    @FXML
    public Label shipNameLabel;
    @FXML
    public ImageView showCurrentShip;
    @FXML
    public GridPane playerBoard;
    @FXML
    public GridPane enemyBoard;
    @FXML
    public Label gameStatusText;
    @FXML
    public Button quitGameButton;
    @FXML
    public Button resetGameButton;

    public void setUseRandomCPU(boolean randomCPU){
        this.useRandomCPU = randomCPU;
    }

//    System.out.println("playerBoard is " + (playerBoard == null ? "null" : "not null"));

    @FXML
    public void handleGameControl(ActionEvent event){
        if(!gameStarted){
            startGame(event);
            gameControlButton.setText("Reset Current Game");
            gameStarted = true;
        }else{
            resetGame(event);
            gameControlButton.setText("Start New Game");
            gameStarted = false;
        }
    }

    @FXML
    public void startGame(ActionEvent actionEvent){
        System.out.println("RANDOM CPU: " + useRandomCPU);
        System.out.println("playerBoard is " + (playerBoard == null ? "null" : "not null"));

        // setup game
        humanPlayer = new HumanPlayer();
        randomAIPlayer = new RandomAIPlayer();
        // humanPlayer.getGameBoard().printBoardToConsole();
        randomAIPlayer.getGameBoard().printBoardToConsole();
        // TODO: add ship types on wiki
        shipsToDisplay = List.of(
                ShipType.CARRIER,
                ShipType.BATTLESHIP
        );
        // set up grids
//        randomAIPlayer.placeShipRandom();
        randomAIPlayer.placeShipRandom();
        System.out.println("\nComputer's board after ship placement:");
        randomAIPlayer.getGameBoard().printBoardToConsole();
        setupPlayerBoard();
        System.out.println("startGame - setupPlayerBoard");
        setupEnemyBoard();
        System.out.println("startGame - setupEnemyBoard");
        // Don't call updateBoards here as it will clear the click handlers
        // updateBoards();
        // System.out.println("startGame - updateBoards");
        updateShipPreview();
        System.out.println("startGame - updateShipPreview");
        System.out.println("startGame - Start game END");
        currentGameState = GameState.SETUP;
        gameStatusText.setText("Place your ships. Click to place, use Rotate button to change orientation.");
    }

    private boolean checkWinner(PlayerGameBoard board, boolean isPlayerWin){
        if(board.isAllShipsSunk()){
            currentGameState = GameState.GAME_OVER;
            if(isPlayerWin){
                gameStatusText.setText("Congratulations! You won!");
            }else{
                gameStatusText.setText("Computer Won! You lost!");
            }
            gameControlButton.setDisable(false);
            gameStarted = true; // Keep the button in reset mode
            return true;
        }
        return false;
    }

    /**
     * Sets up the player board by clearing the grid and adding 100 StackPane elements representing a 10x10 grid.
     * Each StackPane element has a white background color and black border.
     */
    private void setupPlayerBoard(){
        System.out.println("setting up player board");
        playerBoard.getChildren().clear();
        for(int i=0;i<10;i++){ // rows
            for(int j=0;j<10;j++){ // cols
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(visualGridSize,visualGridSize);
                stackPane.setStyle("-fx-border-color: black; -fx-background-color: #908a8a;");
                final int row = i;
                final int column = j;
                stackPane.setOnMouseClicked((MouseEvent e)->{
                    if (currentGameState == GameState.SETUP) {
                        handleShipPlacement(row, column);
                    }
                });
                playerBoard.add(stackPane,i,j);
            }
        }
    }

    private void handleShipPlacement(int row, int column){
        if(currentShipIndex < shipsToDisplay.size()){
            ShipType type = shipsToDisplay.get(currentShipIndex);
            Ship ship = type.createShip();
            ship.setOrientation(currentOrientation);
            System.out.println("Attempting to place " + type.name() + " at (" + row + "," + column + ") with orientation: " + currentOrientation);
            boolean placed = humanPlayer.getGameBoard().placeShip(ship, row, column);
            if(placed){
                currentShipIndex++;
                updateBoards();
                updateShipPreview();
                System.out.println("Successfully placed ship");

                // Check if all ships are placed
                if(currentShipIndex >= shipsToDisplay.size()){
                    currentGameState = GameState.PLAYER_TURN;
                    gameStatusText.setText("Your turn! Click on the enemy board to attack.");
                }
            }else{
                System.out.println("Failed to place " + type.name() + " at (" + row + "," + column + ")");
            }
        }
    }

    private void setupEnemyBoard(){
        System.out.println("setting up enemy board");
        enemyBoard.getChildren().clear();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                StackPane cellPane = new StackPane();
                cellPane.setPrefSize(visualGridSize, visualGridSize);
                cellPane.setStyle("-fx-border-color: black; -fx-background-color: #908a8a;");

                final int row = i;
                final int col = j;
                cellPane.setOnMouseClicked(e -> {
                    if(currentGameState == GameState.PLAYER_TURN){
                        handlePlayerAttack(row, col);
                    }
                });
                enemyBoard.add(cellPane, j, i);
            }
        }
    }

    private void updateBoards(){
        // Instead of calling displayOnGrid which clears the grid, we'll update the visual state directly
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                // Update player board
                StackPane playerCell = (StackPane) playerBoard.getChildren().get(row * 10 + col);
                playerCell.getChildren().clear();
                Cell playerCellState = humanPlayer.getGameBoard().getCell(row, col);

                if(playerCellState.getState() == CellState.SHIP){
                    Ship ship = playerCellState.getShip();
                    if(ship != null){
                        try{
                            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/com/sj/batlleship/batlleship/images/" + ship.getImageName())));
                            img.setFitWidth(30);
                            img.setFitHeight(30);
                            // Rotate the image based on ship orientation
                            img.setRotate(ship.getOrientation() == Orientation.HORIZONTAL ? 90 : 0);
                            playerCell.getChildren().add(img);
                        }catch(Exception e){
                            System.out.println("Error loading ship image: " + e.getMessage());
                        }
                    }
                }else if(playerCellState.getState() == CellState.HIT){
                    playerCell.setStyle("-fx-border-color: black; -fx-background-color: red;");
                }else if(playerCellState.getState() == CellState.MISS){
                    playerCell.setStyle("-fx-border-color: black; -fx-background-color: gray;");
                }else{
                    playerCell.setStyle("-fx-border-color: black; -fx-background-color: #908a8a;");
                }

                // Update enemy board
                StackPane enemyCell = (StackPane) enemyBoard.getChildren().get(row * 10 + col);
                enemyCell.getChildren().clear();
                Cell enemyCellState = randomAIPlayer.getGameBoard().getCell(row, col);

                if(enemyCellState.getState() == CellState.HIT){
                    enemyCell.setStyle("-fx-border-color: black; -fx-background-color: red;");
                }else if(enemyCellState.getState() == CellState.MISS){
                    enemyCell.setStyle("-fx-border-color: black; -fx-background-color: gray;");
                }else{
                    enemyCell.setStyle("-fx-border-color: black; -fx-background-color: #908a8a;");
                }
            }
        }
    }

    private void handlePlayerAttack(int row, int column){
        if(currentGameState != GameState.PLAYER_TURN){
            return;
        }

        // Check if the cell has already been attacked
        Cell targetCell = randomAIPlayer.getGameBoard().getCell(row, column);
        if(targetCell.getState() == CellState.HIT || targetCell.getState() == CellState.MISS){
            gameStatusText.setText("You already attacked this cell! Try again.");
            return;
        }

        boolean hit = randomAIPlayer.getGameBoard().receiveAttack(row, column);
        updateBoards();

        // Check if player won
        if(checkWinner(randomAIPlayer.getGameBoard(), true)){
            return;
        }

        if(hit){
            gameStatusText.setText("Hit! Computer's turn...");
        }else{
            gameStatusText.setText("Miss! Computer's turn...");
        }

        currentGameState = GameState.COMPUTER_TURN;
        // Computer's turn
        randomAIPlayer.makeMove(humanPlayer.getGameBoard());
        updateBoards();

        // Check if computer won
        if(checkWinner(humanPlayer.getGameBoard(), false)){
            return;
        }

        currentGameState = GameState.PLAYER_TURN;
        gameStatusText.setText("Your turn! Click on the enemy board to attack.");
    }

    @FXML
    public void resetGame(ActionEvent actionEvent){
        // Reset game state
        currentGameState = GameState.SETUP;
        currentShipIndex = 0;
        currentOrientation = Orientation.HORIZONTAL;
        gameStarted = false;

        // Reset players
        humanPlayer = new HumanPlayer();
        randomAIPlayer = new RandomAIPlayer();

        // Place computer's ships
        randomAIPlayer.placeShipRandom();

        // Reset boards
        setupPlayerBoard();
        setupEnemyBoard();

        // Reset ship preview
        updateShipPreview();

        // Reset status
        gameStatusText.setText("Place your ships. Click to place, use Rotate button to change orientation.");
        gameControlButton.setDisable(false);

        System.out.println("Game reset complete");
    }

    @FXML
    public void rotateShip(ActionEvent actionEvent){
        if(currentGameState != GameState.SETUP){
            return;
        }
        currentOrientation = currentOrientation == Orientation.HORIZONTAL ?
                Orientation.VERTICAL : Orientation.HORIZONTAL;
        if(currentShipIndex < shipsToDisplay.size()){
            ShipType type = shipsToDisplay.get(currentShipIndex);
            Ship ship = type.createShip();
            ship.setOrientation(currentOrientation);
            System.out.println("Rotated ship to: " + currentOrientation);
        }
        updateShipPreview();
    }

    private void updateShipPreview(){
        if(currentShipIndex < shipsToDisplay.size()){
            ShipType type = shipsToDisplay.get(currentShipIndex);
            System.out.println("Updating ship preview for: " + type.name() + " with image: " + type.imageName);
            try{
                Image shipImage = new Image(getClass().getResourceAsStream("/com/sj/batlleship/batlleship/images/" + type.imageName));
                showCurrentShip.setImage(shipImage);
                // Rotate the image based on orientation
                showCurrentShip.setRotate(currentOrientation == Orientation.HORIZONTAL ? 90 : 0);
                shipNameLabel.setText(type.name());
                System.out.println("Ship preview updated successfully with orientation: " + currentOrientation);
            }catch(Exception e){
                System.out.println("Error loading ship preview image: " + e.getMessage());
                e.printStackTrace();
            }
        }else{
            showCurrentShip.setImage(null);
            shipNameLabel.setText("All ships placed");
        }
    }

    @FXML
    public void quitGame(ActionEvent actionEvent){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("All progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try{
                // Load the welcome screen
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sj/batlleship/batlleship/welcome.fxml"));
                Parent root = fxmlLoader.load();

                // Get the current stage
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                stage.setScene(scene);
                stage.show();
            }catch(Exception e){
                System.out.println("Error loading welcome screen: " + e.getMessage());
//                e.printStackTrace();
            }
        }
    }
}
