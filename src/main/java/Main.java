package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.java.scenes.LoadScene;
import main.java.web.Client;
import main.java.web.Server;

import static main.java.additionals.KeyEvents.loadSceneFilter;

public class Main extends Application {
    public static final int SIZE = 10;
    public static final int CELL_SIZE = 35;
    public static final String ICON = "/main/resources/images/icon.png";
    public static final String MENU = "/main/resources/images/menu.jpg";
    public static final String GAME = "/main/resources/images/game.jpg";
    public static final String BACKGROUND = "/main/resources/images/background.jpg";
    public static final String WEB = "/main/resources/images/web.gif";
    public static String ROLE = "";
    public static String NAME = "";
    public static String OPPONENT = "";
    //public static final String THEME_MUSIC = "main/resources/music/theme.aac";
    //public static final String MENU_MUSIC = "main/resources/music/menu.acc";
    //public static final String GAME_MUSIC = "main/resources/music/game.acc";

    public static void forceExit() {
        System.err.println("Role required");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length == 0)
            forceExit();
        if (args[0].equals("-role=server"))
            Main.ROLE = "SERVER";
        else if (args[0].equals("-role=client"))
            Main.ROLE = "CLIENT";
        else forceExit();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = LoadScene.getInstance().getRoot(primaryStage);
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, loadSceneFilter);

        Image image = new Image(getClass().getResourceAsStream(ICON));
        primaryStage.getIcons().add(image);

        primaryStage.setTitle("BattleShip");
        primaryStage.setScene(new Scene(root));

        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);

        primaryStage.setOnCloseRequest(event -> {
            if (ROLE.equals("SERVER"))
                Server.stop();
            else
                Client.stop();
            System.exit(0);
        });

        primaryStage.show();
    }
}
