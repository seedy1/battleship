package com.sj.batlleship.batlleship;

import com.sj.batlleship.batlleship.constants.Paths;
import com.sj.batlleship.batlleship.utils.SceneManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

/**
 * This class represents a simple JavaFX application that displays a welcome screen for a Battleship game.
 * It extends the Application class and provides methods to start the application.
 */
public class HelloApplication extends Application{
    private static final String GAME_TITLE = "Battleship Game";

    /**
     * Loads the welcome screen FXML file, creates a scene with BootstrapFX stylesheet, sets the stage title to "Battleship Game",
     * sets the stage scene, and displays the stage.
     *
     * @param stage the primary stage for this application, where the welcome screen will be displayed
     * @throws IOException if an error occurs while loading the welcome.fxml file
     */
    @Override
    public void start(Stage stage) throws IOException{
        Parent root = SceneManager.loadFXML(Paths.WELCOME_SCENE);
        Scene scene = SceneManager.createSceneWithStyles(root, BootstrapFX.bootstrapFXStylesheet());
        SceneManager.setStageProperties(stage, scene, GAME_TITLE);
        SceneManager.setStageIcon(stage, Paths.APP_ICON);
        SceneManager.showStage(stage);
    }

    public static void main(String[] args){
        launch();
    }
}