package com.sj.batlleship.batlleship.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Welcome{

    public Button testClick;

    @FXML
    protected void startSoloGameWithRandomAI(ActionEvent actionEvent){
        System.out.println("Starting solo game with random AI");
        loadGameScene(actionEvent, true);
    }

    @FXML
    protected void startSoloGameWithSmartAI(ActionEvent actionEvent){
        //TODO: add logs / logging
        System.out.println("Starting solo game with Smart AI");
//        testClick.setOnMouseEntered(e -> System.out.println("hover"));
//        testClick.setOnMouseClicked((MouseEvent e)-> {
//                    System.out.println("event: " + e.getEventType()); });
        loadGameScene(actionEvent, false);
    }

    /**
     * Loads the game scene based on the provided action event and CPU mode.
     *
     * @param actionEvent The ActionEvent triggering the game scene loading.
     * @param randomCPU   A boolean indicating whether the CPU mode is random or not.
     */
    private void loadGameScene(ActionEvent actionEvent, boolean randomCPU){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sj/batlleship/batlleship/views/game-scene.fxml"));
            Parent root = fxmlLoader.load();
            Game gameController = fxmlLoader.getController(); // get game controller
            gameController.setUseRandomCPU(randomCPU);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            System.out.println("Error: "+e);
            throw new RuntimeException(e);
        }

    }
}
