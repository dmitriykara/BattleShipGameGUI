package main.java.scenes;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.additionals.ScenesAction;

import static main.java.Main.BACKGROUND;


/**
 * Class creates first scene
 */
public class LoadScene {
    private static LoadScene loadScene;
    public Stage stage;

    public static LoadScene getInstance() {
        if (loadScene == null)
            loadScene = new LoadScene();
        return loadScene;
    }

    public static void exit(Event e) {
        System.exit(0);
    }

    /**
     * @return root object of Load Scene
     */
    public StackPane getRoot(Stage stage) {
        Image image = new Image(getClass().getResourceAsStream(BACKGROUND));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(THEME_MUSIC);
        //Music.play();

        this.stage = stage;

        Button startButton = new Button("Menu");
        startButton.setPrefSize(150, 30);
        startButton.setAlignment(Pos.CENTER);
        startButton.setOnAction(e -> ScenesAction.toMenu(stage));

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(150, 30);
        exitButton.setAlignment(Pos.CENTER);
        exitButton.setOnAction(LoadScene::exit);

        VBox box = new VBox(startButton, exitButton);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);

        return new StackPane(img, box);
    }
}
