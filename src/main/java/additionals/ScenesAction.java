package main.java.additionals;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.java.gameEngine.Ocean;
import main.java.scenes.GameScene;
import main.java.scenes.MenuScene;
import main.java.scenes.MultiPlayScene;

import java.util.Optional;
import java.util.function.BiConsumer;

import static main.java.Main.CELL_SIZE;
import static main.java.Main.SIZE;
import static main.java.additionals.KeyEvents.*;

/**
 * Common actions for game scenes (SetScene, SinglePlayScene, MultiPlayScene)
 * and some function common for all scene
 */
public class ScenesAction {
    public static final KeyCode INFORMATION_KEY = KeyCode.K;
    public static final KeyCode SUBMARINE_KEY = KeyCode.Q;
    public static final KeyCode DESTROYER_KEY = KeyCode.W;
    public static final KeyCode CRUISER_KEY = KeyCode.E;
    public static final KeyCode BATTLESHIP_KEY = KeyCode.R;
    public static final KeyCode TRANSPOSE_KEY = KeyCode.T;
    public static final KeyCode RESET_KEY = KeyCode.N;
    public static final KeyCode AUTOPLACE_KEY = KeyCode.A;
    public static final KeyCode PLAYER_PAUSE_KEY = KeyCode.P;
    public static final KeyCode PLAYER_VOLUME_UP_KEY = KeyCode.O;
    public static final KeyCode PLAYER_VOLUME_DOWN_KEY = KeyCode.I;

    /**
     * Change stage root to Menu root object
     */
    public static void toMenu(Stage stage) {
        try {
            //Music.stop();
            stage.getScene().setRoot(MenuScene.getInstance().getRoot(stage));
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, singlePlaySceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, multiPlaySceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, setSceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, loadSceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, clientSceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, serverSceneFilter);
            stage.addEventFilter(KeyEvent.KEY_PRESSED, menuSceneFilter);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error on menu load");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }

    /**
     * Creates numeric label for game scenes
     */
    public static Label getNumberLabel(int value, int width, int height) {
        Label label = new Label(Integer.toString(value));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMinSize(width, height);
        label.setMaxSize(width, height);
        return label;
    }

    /**
     * Creates array of buttons
     *
     * @param opponent option for MultiPlay
     * @param size     amount of buttons
     * @param cellSize size of buttons
     * @param func     action function for MouseClick
     * @return gameBoard for gameScenes
     */
    public static Button[][] buildBoard(boolean opponent, int size, int cellSize, BiConsumer<Integer, Integer> func) {
        Button[][] gameBoard = new Button[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int x = i;
                final int y = j;
                Button button = new Button();
                button.setMinSize(cellSize, cellSize);
                button.setMaxSize(cellSize, cellSize);
                if (!opponent)
                    button.setOnMouseClicked(e -> func.accept(x, y));
                gameBoard[j][i] = button;
            }
        }
        return gameBoard;
    }

    /**
     * Creates GUI version of gameBoard (function buildBoard)
     */
    public static VBox buildShipsContainer(Button[][] gameBoard, int size, int cellSize, String label) {
        Label tmp = new Label(label);
        tmp.setAlignment(Pos.CENTER);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setMinSize(cellSize * (size + 1), cellSize * (size + 1));
        gridPane.setMaxSize(cellSize * (size + 1), cellSize * (size + 1));

        for (int i = 0; i < size; i++) {
            gridPane.add(getNumberLabel(i, cellSize, cellSize), i + 1, 0);
            gridPane.add(getNumberLabel(i, cellSize, cellSize), 0, i + 1);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridPane.add(gameBoard[i][j], i + 1, j + 1);
            }
        }

        VBox vBox = new VBox(tmp, gridPane);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    /**
     * Creates label with const string. Used in legend for game scenes
     */
    public static Label getLegendLabel(String value, String color) {
        Label label = new Label(value);
        label.setTextFill(Color.web(color));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font("Alias", FontWeight.EXTRA_BOLD, 20));
        return label;
    }

    /**
     * Alert popUp for player's win
     *
     * @param opponentOcean, ocean - Ocean object to get statistics
     */
    public static void loadWinResults(Ocean opponentOcean, Ocean ocean) {
        Alert alert;
        if (ocean == null)
            alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Good game, Admiral!\nYou have sunk %d ships by %d hits from %d shots!",
                            opponentOcean.getShipsSunk(), opponentOcean.getHitCount(),
                            opponentOcean.getShotsFired()));
        else
            alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Good game, Admiral!\nYou have sunk %d ships by %d hits from %d shots!\n" +
                                    "The opponent have sunk %d ships by %d hits from %d shots",
                            opponentOcean.getShipsSunk(), opponentOcean.getHitCount(),
                            opponentOcean.getShotsFired(), ocean.getShipsSunk(),
                            ocean.getHitCount(), ocean.getShotsFired()));
        alert.setHeight(300);
        alert.setWidth(300);
        alert.setHeaderText("Victory!");
        alert.showAndWait();
    }

    /**
     * Alert popUp for player's lose
     *
     * @param opponentOcean Ocean object to get statistics
     */
    public static void loadLoseResults(Ocean opponentOcean, Ocean ocean) {
        Alert alert;
        if (ocean == null)
            alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Nice try, Admiral!\nYou have sunk %d ships by %d hits from %d shots!",
                            opponentOcean.getShipsSunk(), opponentOcean.getHitCount(),
                            opponentOcean.getShotsFired()));
        else
            alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Nice try, Admiral!\nYou have sunk %d ships by %d hits from %d shots!\n" +
                                    "The opponent have sunk %d ships by %d hits from %d shots",
                            opponentOcean.getShipsSunk(), opponentOcean.getHitCount(),
                            opponentOcean.getShotsFired(), ocean.getShipsSunk(),
                            ocean.getHitCount(), ocean.getShotsFired()));
        alert.setHeight(300);
        alert.setWidth(300);
        alert.setHeaderText("Defeat!");
        alert.showAndWait();
    }

    public static void earlyVictory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "You won because the opponent left the game!");
        alert.setHeight(300);
        alert.setWidth(300);
        alert.setHeaderText("Victory!");
        alert.showAndWait();
    }

    /**
     * Creates TextField for coordinates input
     */
    public static TextField getTextField(GameScene scene, int id) {
        TextField textField = new TextField();
        textField.setMinSize(50, 30);
        textField.setMaxSize(50, 30);
        textField.setText("0");
        textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCharacter().matches("[0-9]")) {
                    textField.setText(event.getCharacter());
                    if (id == 0)
                        scene.setGameBoardX(Integer.parseInt(event.getCharacter()));
                    if (id == 1)
                        scene.setGameBoardY(Integer.parseInt(event.getCharacter()));
                    scene.getButton(scene.getGameBoardY(), scene.getGameBoardX()).requestFocus();
                }
                event.consume();
            }
        });
        return textField;
    }

    /**
     * Creates ScrollPane to log game process
     */
    public static ScrollPane getLog() {
        TextFlow log = new TextFlow();
        log.setMinHeight(250);
        log.setPadding(new Insets(0, 0, 40, 0));
        log.setMinWidth(CELL_SIZE * (SIZE + 1) - 10);
        log.setMaxWidth(CELL_SIZE * (SIZE + 1) - 10);

        ScrollPane scrollPane = new ScrollPane(log);
        scrollPane.setMinHeight(250);
        scrollPane.setMaxHeight(2000);
        scrollPane.setMinWidth(CELL_SIZE * (SIZE + 1) - 10);
        scrollPane.setMaxWidth(CELL_SIZE * (SIZE + 1) - 10);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }

    /**
     * Key actions for Music MediaPlayer
     */
    public static void setPlayer(KeyEvent event) {
        if (event.getCode() == PLAYER_PAUSE_KEY)
            Music.pause();
        if (event.getCode() == PLAYER_VOLUME_UP_KEY)
            Music.volumeUp();
        if (event.getCode() == PLAYER_VOLUME_DOWN_KEY)
            Music.volumeDown();
    }

    /**
     * Common key actions for all scenes
     *
     * @param event
     */
    public static void keyActions(KeyEvent event) {
        if (event.getCode() == KeyCode.K)
            infoAlert();
        //setPlayer(event);
    }

    /**
     * Set key actions to navigate by arrows for game scenes
     */
    public static void setControls(GameScene scene, KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            int value = scene.getGameBoardX();
            --value;
            if (value == -1)
                value = 9;
            scene.setGameBoardX(value);
            scene.getInput(1).setText(Integer.toString(scene.getGameBoardX()));
            scene.getButton(scene.getGameBoardY(), scene.getGameBoardX()).requestFocus();
        }
        if (event.getCode() == KeyCode.DOWN) {
            int value = scene.getGameBoardX();
            ++value;
            if (value == 10)
                value = 0;
            scene.setGameBoardX(value);
            scene.getInput(1).setText(Integer.toString(scene.getGameBoardX()));
            scene.getButton(scene.getGameBoardY(), scene.getGameBoardX()).requestFocus();
        }
        if (event.getCode() == KeyCode.LEFT) {
            int value = scene.getGameBoardY();
            --value;
            if (value == -1)
                value = 9;
            scene.setGameBoardY(value);
            scene.getInput(0).setText(Integer.toString(scene.getGameBoardY()));
            scene.getButton(scene.getGameBoardY(), scene.getGameBoardX()).requestFocus();
        }
        if (event.getCode() == KeyCode.RIGHT) {
            int value = scene.getGameBoardY();
            ++value;
            if (value == 10)
                value = 0;
            scene.setGameBoardY(value);
            scene.getInput(0).setText(Integer.toString(scene.getGameBoardY()));
            scene.getButton(scene.getGameBoardY(), scene.getGameBoardX()).requestFocus();
        }
    }

    /**
     * Creates Button with `text` for SetScene
     */
    public static Button setSceneButton(String text) {
        Button btn = new Button(text);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setPrefSize(150, 30);
        btn.setAlignment(Pos.CENTER);
        return btn;
    }

    /**
     * Starts online game
     */
    public static void toOnlineGame(Stage stage, Ocean ocean, Ocean opponentOcean) {
        try {
            stage.getScene().setRoot(MultiPlayScene.getInstance().getRoot(stage, ocean, opponentOcean));
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, clientSceneFilter);
            stage.removeEventFilter(KeyEvent.KEY_PRESSED, serverSceneFilter);
            stage.addEventFilter(KeyEvent.KEY_PRESSED, multiPlaySceneFilter);
            MultiPlayScene.getInstance().inputs[0].requestFocus();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error on game scene load");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }

    /**
     * Creates new info alert with `text`
     */
    public static void makeAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setHeight(50);
        alert.setWidth(100);
        alert.showAndWait();
    }

    /**
     * Ask user to leave
     * @return if user wants to leave
     */
    public static boolean leaveGameAlert() {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Do you want to leave game?", yes, cancel);

        alert.setTitle("Date format warning");
        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(cancel) == yes;
    }

    /**
     * Creates alert popUp to provide information about keyActions
     */
    public static void infoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Information");
        alert.setContentText("\n" +
                String.format("See information: %s\n", INFORMATION_KEY) +
                "Menu / Load Screen: Escape\n" +
                "Menu commands:\n" +
                "\tENTER - singleplay game\n" +
                "\tSHIFT + ENTER - multiplay game\n" +
                //"Player commands:\n" +
                //String.format("\t%s - pause player\n", PLAYER_STOP_KEY) +
                //String.format("\t%s - player volume up\n", PLAYER_VOLUME_UP_KEY) +
                //String.format("\t%s - player volume down\n", PLAYER_VOLUME_DOWN_KEY) +
                "SetMode commands:\n" +
                String.format("\t%s - select Submarine\n", SUBMARINE_KEY) +
                String.format("\t%s - select Destroyer\n", DESTROYER_KEY) +
                String.format("\t%s - select Cruiser\n", CRUISER_KEY) +
                String.format("\t%s - select Battleship\n", BATTLESHIP_KEY) +
                String.format("\t%s - transpose ship\n", TRANSPOSE_KEY) +
                String.format("\t%s - reset board\n", RESET_KEY) +
                String.format("\t%s - autoplace ships\n", AUTOPLACE_KEY) +
                "\tSPACE - set and start game\n" +
                "\tARROWS / Mouse / Coordinates inputs - navigate\n" +
                "\tTAB - switch coordinates input\n" +
                "\tENTER / MouseClick - set ship\n" +
                "Game commands:\n" +
                "\tARROWS / Mouse / Coordinates inputs - navigate\n" +
                "\tTAB - switch coordinates input\n" +
                "\tENTER / MouseClick - shoot\n");
        alert.setWidth(400);
        alert.setHeight(600);
        alert.showAndWait();
    }
}
