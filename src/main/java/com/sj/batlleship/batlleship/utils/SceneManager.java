package com.sj.batlleship.batlleship.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Utility class for managing JavaFX scenes and stages.
 * Provides methods for loading FXML files, creating scenes with styles,
 * setting stage properties, and showing stages.
 */
public class SceneManager{
    /**
     * Loads an FXML file and returns its root node.
     *
     * @param fxmlPath the path to the FXML file
     * @return the root node of the loaded FXML
     * @throws IOException if an error occurs while loading the FXML file
     */
    public static Parent loadFXML(String fxmlPath) throws IOException{
        return FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxmlPath)));
    }

    /**
     * Creates a scene with the specified root node and stylesheet.
     *
     * @param rootNode the root node of the scene
     * @param stylesheet the stylesheet to apply to the scene
     * @return the created scene
     */
    public static Scene createSceneWithStyles(Parent rootNode, String stylesheet){
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add(stylesheet);
        return scene;
    }

    /**
     * Sets the properties of a stage.
     *
     * @param stage the stage to configure
     * @param scene the scene to set on the stage
     * @param title the title to set on the stage
     */
    public static void setStageProperties(Stage stage, Scene scene, String title){
        stage.setTitle(title);
        stage.setScene(scene);
    }

    /**
     * Sets an icon
     *
     * @param stage the stage to set the icon on
     * @param iconPath the path to the icon file
     */
    public static void setStageIcon(Stage stage, String iconPath){
        Image icon = new Image(Objects.requireNonNull(SceneManager.class.getResourceAsStream(iconPath)));
        stage.getIcons().add(icon);
    }

    /**
     * Shows a stage which has a scene
     *
     * @param stage the stage to show
     */
    public static void showStage(Stage stage) {
        stage.show();
    }
} 