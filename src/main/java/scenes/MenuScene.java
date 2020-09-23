package main.java.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static main.java.Main.MENU;
import static main.java.additionals.KeyEvents.*;


/**
 * Class creates menu scene. Possible ways - to SinglePlay, to MultiPlay, to Load
 */
public class MenuScene {
    private static MenuScene menuScene;
    public Stage stage;

    public static MenuScene getInstance() {
        if (menuScene == null)
            menuScene = new MenuScene();
        return menuScene;
    }


    /**
     * Change stage root to SinglePlay root object
     */
    public static void toSinglePlay(Stage stage) {
        try {
            //Music.stop();
            stage.getScene().setRoot(SinglePlayScene.getInstance().getRoot(stage, null));
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, menuSceneFilter);
            stage.addEventFilter(KeyEvent.KEY_PRESSED, singlePlaySceneFilter);
            SinglePlayScene.getInstance().inputs[0].requestFocus();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error on game scene load");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }


    /**
     * Change stage root to MultiPlay root object
     */
    public static void toMultiPlay(Stage stage) {
        try {
            //Music.stop();
            stage.getScene().setRoot(SetScene.getInstance().getRoot(stage, "MultiPlay"));
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, menuSceneFilter);
            stage.addEventFilter(KeyEvent.KEY_PRESSED, setSceneFilter);
            SetScene.getInstance().inputs[0].requestFocus();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error on game scene load");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }


    /**
     * Change stage root to Load root object
     */
    public static void toLoadScene(Stage stage) {
        try {
            //Music.stop();
            stage.getScene().setRoot(LoadScene.getInstance().getRoot(stage));
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, menuSceneFilter);
            stage.addEventFilter(KeyEvent.KEY_PRESSED, loadSceneFilter);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error on load");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }

    /**
     * @return root object of Menu Scene
     */
    public StackPane getRoot(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream(MENU));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(MENU_MUSIC);
        //Music.play();

        this.stage = stage;

        Button singlePlay = new Button("Battle PvE");
        singlePlay.setAlignment(Pos.CENTER);
        singlePlay.setOnAction(e -> toSinglePlay(stage));
        singlePlay.setPrefSize(150, 30);

        Button multiPlay = new Button("Battle PvP");
        multiPlay.setAlignment(Pos.CENTER);
        multiPlay.setOnAction(e -> toMultiPlay(stage));
        multiPlay.setPrefSize(150, 30);

        Button exitButton = new Button("Back");
        exitButton.setAlignment(Pos.CENTER);
        exitButton.setOnAction(e -> toLoadScene(stage));
        exitButton.setPrefSize(150, 30);

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(singlePlay, Priority.ALWAYS);
        VBox.setVgrow(multiPlay, Priority.ALWAYS);
        VBox.setVgrow(exitButton, Priority.ALWAYS);
        box.getChildren().addAll(singlePlay, multiPlay, exitButton);
        box.setSpacing(10);

        return new StackPane(img, box);
    }
}
