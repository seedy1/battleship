package com.sj.batlleship.batlleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Objects;

/**
 * This class represents a simple JavaFX application that displays a welcome screen for a Battleship game.
 * It extends the Application class and provides methods to start the application, load FXML files, create scenes with styles,
 * set stage properties, and show the stage.
 */
public class HelloApplication extends Application{
    /**
     * Loads the welcome screen FXML file, creates a scene with BootstrapFX stylesheet, sets the stage title to "Battleship Game",
     * sets the stage scene, and displays the stage.
     *
     * @param stage the primary stage for this application, where the welcome screen will be displayed
     * @throws IOException if an error occurs while loading the welcome.fxml file
     */
    @Override
    public void start(Stage stage) throws IOException{
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome.fxml"));
//        Parent rootNode = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Parent rootNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Battleship Game");
        stage.setScene(scene);
        stage.show();
    }

    private Parent loadFXML(String fxml) throws IOException{
        return FXMLLoader.load(getClass().getResource(fxml));
    }

    private Scene createSceneWithStyles(Parent rootNode, String stylesheet){
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add(stylesheet);
        return scene;
    }

    private void setStageProperties(Stage stage, Scene scene, String title){
        stage.setTitle(title);
        stage.setScene(scene);
    }

    private void showStage(Stage stage){
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}