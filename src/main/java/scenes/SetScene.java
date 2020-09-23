package main.java.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.additionals.ButtonActions;
import main.java.additionals.ScenesAction;
import main.java.gameEngine.Ocean;
import main.java.gameEngine.Ship;

import static main.java.Main.*;
import static main.java.additionals.KeyEvents.*;
import static main.java.additionals.ScenesAction.setSceneButton;

/**
 * Class creates Set Scene
 */
public class SetScene extends GameScene {
    private static SetScene setScene;
    public Button[][] gameBoard;
    public Ocean ocean;
    public Ship ship;
    public TextField[] inputs = new TextField[2];
    public Stage stage;
    public int gameBoardX = 0;
    public int gameBoardY = 0;
    public int textFieldIndex = 0;
    public int ships = SIZE;
    public int battleships = Ocean.BATTLESHIP_COUNT;
    public int cruisers = Ocean.CRUISER_COUNT;
    public int destroyers = Ocean.DESTROYER_COUNT;
    public int submarines = Ocean.SUBMARINE_COUNT;
    private String next;
    private Label[] statistics = new Label[5];

    public static SetScene getInstance() {
        if (setScene == null)
            setScene = new SetScene();
        return setScene;
    }

    /**
     * Navigate player to game after place all ships
     *
     * @param ocean Ocean object to play
     */
    public static void toGame(Stage stage, Ocean ocean) {
        if (getInstance().ships == 0) {
            try {
                //Music.stop();
                switch (getInstance().next) {
                    case "MultiPlay":
                        switch (ROLE) {
                            case "SERVER":
                                stage.getScene().setRoot(ServerScene.getInstance().getRoot(stage, ocean));
                                stage.removeEventFilter(KeyEvent.KEY_PRESSED, setSceneFilter);
                                stage.addEventFilter(KeyEvent.KEY_PRESSED, serverSceneFilter);
                                ServerScene.getInstance().inputs[0].requestFocus();
                                break;
                            case "CLIENT":
                                stage.getScene().setRoot(ClientScene.getInstance().getRoot(stage, ocean));
                                stage.removeEventFilter(KeyEvent.KEY_PRESSED, setSceneFilter);
                                stage.addEventFilter(KeyEvent.KEY_PRESSED, clientSceneFilter);
                                ClientScene.getInstance().inputs[0].requestFocus();
                                break;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Incorrect `next` argument");
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error on scene load");
                alert.setHeight(50);
                alert.setWidth(100);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Place all ships of press 'A' to place them randomly");
            alert.setHeight(50);
            alert.setWidth(100);
            alert.showAndWait();
        }
    }

    /**
     * Place all ships automatically
     */
    public static void autoPlaceShips() {
        getInstance().ocean = Ocean.createGame();
        getInstance().ocean.placeAllShipsRandomly();
        getInstance().ships = 0;
        getInstance().battleships = getInstance().destroyers = 0;
        getInstance().cruisers = getInstance().submarines = 0;
        getInstance().statistics[0].setText("0");
        for (int i = 1; i < getInstance().statistics.length; i++) {
            getInstance().statistics[i].setText(Integer.toString(i));
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (getInstance().ocean.getShip(i, j).isEmptySea())
                    ButtonActions.resetButton(getInstance(), i, j, false);
                else
                    ButtonActions.successButton(getInstance(), i, j);
            }
        }
    }

    /**
     * Reset all ships and buttons
     */
    public static void resetBoard() {
        getInstance().textFieldIndex = 0;
        getInstance().gameBoardX = 1;
        getInstance().gameBoardY = 0;
        getInstance().ships = SIZE;
        getInstance().battleships = Ocean.BATTLESHIP_COUNT;
        getInstance().cruisers = Ocean.CRUISER_COUNT;
        getInstance().destroyers = Ocean.DESTROYER_COUNT;
        getInstance().submarines = Ocean.SUBMARINE_COUNT;
        getInstance().ocean = Ocean.createGame();
        for (int i = 0; i < getInstance().inputs.length; i++)
            getInstance().inputs[i].setText("0");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ButtonActions.shipButton(getInstance(), i, j);
            }
        }
        getInstance().statistics[0].setText(Integer.toString(SIZE));
        for (int i = 1; i < getInstance().statistics.length; i++) {
            getInstance().statistics[i].setText(Integer.toString(i));
        }
    }

    /**
     * Place ship by coordinates
     */
    public static void set(int row, int column) {
        Ship ship = getInstance().ship;
        if (ship != null) {
            boolean result = ship.okToPlaceShipAt(row, column, ship.isHorizontal(), getInstance().ocean);

            if (result) {
                ship.placeShipAt(row, column, ship.isHorizontal(), getInstance().ocean);
                for (int i = 0; i < ship.getLength(); i++) {
                    ButtonActions.successButton(getInstance(), row, column);
                    if (ship.isHorizontal())
                        ++column;
                    else
                        ++row;
                    if (row == SIZE || column == SIZE)
                        break;
                }

                switch (ship.getLength()) {
                    case 1:
                        --getInstance().submarines;
                        --getInstance().ships;
                        getInstance().statistics[4].setText(Integer.toString(getInstance().submarines));
                        getInstance().statistics[0].setText(Integer.toString(getInstance().ships));
                        break;
                    case 2:
                        --getInstance().destroyers;
                        --getInstance().ships;
                        getInstance().statistics[3].setText(Integer.toString(getInstance().destroyers));
                        getInstance().statistics[0].setText(Integer.toString(getInstance().ships));
                        break;
                    case 3:
                        --getInstance().cruisers;
                        --getInstance().ships;
                        getInstance().statistics[2].setText(Integer.toString(getInstance().cruisers));
                        getInstance().statistics[0].setText(Integer.toString(getInstance().ships));
                        break;
                    case 4:
                        --getInstance().battleships;
                        --getInstance().ships;
                        getInstance().statistics[1].setText(Integer.toString(getInstance().battleships));
                        getInstance().statistics[0].setText(Integer.toString(getInstance().ships));
                        break;
                }

                getInstance().ship = null;
            }
        }
    }

    /**
     * Calls set function with coordinates after player's ENTER
     */
    public static void preSet() {
        set(getInstance().gameBoardX, getInstance().gameBoardY);
    }

    /**
     * Rotate ships and restyle buttons
     */
    public static void rotateShip() {
        ButtonActions.shipButtonExited(getInstance(), getInstance().gameBoardX, getInstance().gameBoardY);
        getInstance().ship.setHorizontal(!getInstance().ship.isHorizontal());
        ButtonActions.shipButtonEntered(getInstance(), getInstance().gameBoardX, getInstance().gameBoardY);
    }

    /**
     * Support function for getRoot
     */
    private VBox buildBox() {
        VBox ships = ScenesAction.buildShipsContainer(gameBoard, SIZE, CELL_SIZE, "Me");

        inputs[0] = ScenesAction.getTextField(this, 0);
        inputs[1] = ScenesAction.getTextField(this, 1);

        GridPane statPane = new GridPane();
        statPane.setHgap(10);
        statPane.setVgap(10);
        Label cLabel = ScenesAction.getLegendLabel("Coordinates to place ship:", "#ffffff");
        cLabel.setPadding(new Insets(10, 0, 0, 0));
        statPane.add(cLabel, 0, 0);
        statPane.add(ScenesAction.getLegendLabel("X coordinate:", "#ffffff"), 1, 1);
        statPane.add(ScenesAction.getLegendLabel("Y coordinate:", "#ffffff"), 1, 2);
        statPane.add(inputs[0], 2, 1);
        statPane.add(inputs[1], 2, 2);
        statPane.setAlignment(Pos.TOP_LEFT);

        GridPane statisticsPane = new GridPane();
        statisticsPane.setVgap(10);
        statisticsPane.setHgap(10);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Remain ship: ", "#000000"), 1, 1);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Remain Battleships: ", "#000000"), 1, 2);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Remain Cruisers: ", "#000000"), 1, 3);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Remain Destroyers: ", "#000000"), 1, 4);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Remain Submarines: ", "#000000"), 1, 5);

        statistics[0] = ScenesAction.getLegendLabel(Integer.toString(SIZE), "#000000");
        statistics[1] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[2] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[3] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[4] = ScenesAction.getLegendLabel("0", "#000000");
        for (int i = 0; i < statistics.length; i++) {
            statisticsPane.add(statistics[i], 2, i + 1);
        }

        Button autoPlace = setSceneButton("Autoplace ships");
        autoPlace.setOnMouseClicked(e -> autoPlaceShips());
        Button reset = setSceneButton("Reset");
        reset.setOnMouseClicked(e -> resetBoard());
        Button play = setSceneButton("Play");
        play.setOnMouseClicked(e-> toGame(stage, ocean));
        VBox controls = new VBox(autoPlace, reset, play);
        controls.setSpacing(10);
        controls.setMinWidth(CELL_SIZE * (SIZE + 1));
        controls.setAlignment(Pos.CENTER);

        HBox mainBox = new HBox(ships, statisticsPane);
        mainBox.setAlignment(Pos.TOP_LEFT);
        mainBox.setSpacing(10);

        HBox supportBox = new HBox(controls, statPane);
        supportBox.setPadding(new Insets(0, 0, 10, 10));
        supportBox.setAlignment(Pos.TOP_LEFT);
        supportBox.setSpacing(10);

        VBox vBox = new VBox(mainBox, supportBox);
        vBox.setSpacing(10);

        resetBoard();

        return vBox;
    }

    /**
     * @param next - next Scene to go after set ships
     * @return root object of Set Scene
     */
    public StackPane getRoot(Stage stage, String next) {
        Image image = new Image(getClass().getResourceAsStream(GAME));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(GAME_MUSIC);
        //Music.play();

        this.stage = stage;
        this.next = next;
        gameBoard = ScenesAction.buildBoard(false, SIZE, CELL_SIZE, SetScene::set);

        return new StackPane(img, buildBox());
    }

    public int getGameBoardX() {
        return gameBoardX;
    }

    public void setGameBoardX(int value) {
        gameBoardX = value;
    }

    public int getGameBoardY() {
        return gameBoardY;
    }

    public void setGameBoardY(int value) {
        gameBoardY = value;
    }

    public Button getButton(int x, int y) {
        return gameBoard[x][y];
    }

    public Button getOpponentButton(int x, int y) {
        return null;
    }

    public TextField getInput(int id) {
        return inputs[id];
    }

    public Ocean getOcean() {
        return ocean;
    }
}
