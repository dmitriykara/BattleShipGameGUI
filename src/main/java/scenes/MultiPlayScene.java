package main.java.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.java.additionals.ButtonActions;
import main.java.additionals.Messages;
import main.java.gameEngine.Ocean;
import main.java.gameEngine.Ship;
import main.java.web.Client;
import main.java.web.Server;
import main.java.web.Signals;

import static main.java.Main.*;
import static main.java.additionals.ScenesAction.*;

/**
 * Class creates MultiPlay Scene
 */
public class MultiPlayScene extends GameScene {
    private static MultiPlayScene multiPlayScene;
    public ScrollPane log;
    public Button[][] gameBoard, opponentBoard;
    public Ocean ocean, opponentOcean;
    public int gameBoardX = 0;
    public int gameBoardY = 0;
    public TextField[] inputs = new TextField[2];
    public int textFieldIndex = 0;
    public Stage stage;
    private boolean lock = false;

    public static MultiPlayScene getInstance() {
        if (multiPlayScene == null) {
            multiPlayScene = new MultiPlayScene();
            if (ROLE.equals("SERVER"))
                multiPlayScene.lock = true;
        }
        return multiPlayScene;
    }

    public static void endGame() {
        Signals.leave();
        if (ROLE.equals("SERVER"))
            Server.stop();
        else
            Client.stop();
        toMenu(getInstance().stage);
    }

    /**
     * Set fields values and set styles for buttons
     *
     * @param ocean         - player's Ocean object (build on SetScene)
     * @param opponentOcean - Ocean of opponent player
     */
    public static void prepareGame(Ocean ocean, Ocean opponentOcean) {
        getInstance().textFieldIndex = 0;
        getInstance().gameBoardX = 0;
        getInstance().gameBoardY = 0;

        getInstance().ocean = ocean;
        getInstance().opponentOcean = opponentOcean;

        TextFlow textFlow = (TextFlow) getInstance().log.getContent();
        textFlow.getChildren().clear();
        for (int i = 0; i < getInstance().inputs.length; i++) {
            getInstance().inputs[i].setText("0");
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ButtonActions.resetButton(getInstance(), i, j, false);
                ButtonActions.resetButton(getInstance(), i, j, true);
            }
        }
    }

    /**
     * Actions for opposite player's shoot
     */
    public static int opponentShoot(int row, int column) {
        int result = getInstance().ocean.shootAt(row, column);
        switch (result) {
            case -1:
                Messages.enemyMiss(getInstance().log, row, column);
                ButtonActions.emptySeaButton(getInstance(), row, column, true);
                getInstance().lock = false;
                break;
            case 1:
                if (!getInstance().ocean.isSunk(row, column)) {
                    Messages.enemyHit(getInstance().log, row, column);
                    ButtonActions.hitButton(getInstance(), row, column, true);
                } else {
                    Ship ship = getInstance().ocean.getShip(row, column);
                    Messages.enemyKill(getInstance().log, row, column);
                    row = ship.getBowRow();
                    column = ship.getBowColumn();
                    for (int i = 0; i < ship.getLength(); i++) {
                        ButtonActions.killButton(getInstance(), row, column, true);
                        if (ship.isHorizontal())
                            ++column;
                        else
                            ++row;
                    }
                    if (getInstance().ocean.isGameOver()) {
                        loadLoseResults(getInstance().opponentOcean, getInstance().ocean);
                        endGame();
                    }
                }
                break;
        }
        return result;
    }

    /**
     * Actions for player's shoot
     */
    public static void shoot(int row, int column) {
        if (getInstance().lock)
            return;
        Button button = getInstance().gameBoard[column][row];
        int result = getInstance().opponentOcean.shootAt(row, column);
        switch (result) {
            case -1:
                Signals.shoot(row, column);
                Messages.miss(getInstance().log, row, column);
                ButtonActions.emptySeaButton(getInstance(), row, column, false);
                getInstance().lock = true;
                break;
            case 0:
                Messages.repeat(getInstance().log, row, column);
                ButtonActions.rotateButton(button);
                break;
            case 1:
                Signals.shoot(row, column);
                if (!getInstance().opponentOcean.isSunk(row, column)) {
                    Messages.hit(getInstance().log, row, column);
                    ButtonActions.hitButton(getInstance(), row, column, false);
                } else {
                    Ship ship = getInstance().opponentOcean.getShip(row, column);
                    Messages.kill(getInstance().log, row, column);
                    row = ship.getBowRow();
                    column = ship.getBowColumn();
                    for (int i = 0; i < ship.getLength(); i++) {
                        ButtonActions.killButton(getInstance(), row, column, false);
                        if (ship.isHorizontal())
                            ++column;
                        else
                            ++row;
                    }
                    if (getInstance().opponentOcean.isGameOver()) {
                        loadWinResults(getInstance().opponentOcean, getInstance().ocean);
                        endGame();
                    }
                }
                break;
        }
    }

    /**
     * Calls shoot function with coordinates after player's ENTER
     */
    public static void preShoot() {
        shoot(getInstance().gameBoardX, getInstance().gameBoardY);
    }

    /**
     * Support function for getRoot
     */
    private VBox buildBox(Ocean ocean, Ocean opponentOcean) {
        VBox ships = buildShipsContainer(gameBoard, SIZE, CELL_SIZE, "Partner");
        VBox opponentShips = buildShipsContainer(opponentBoard, SIZE, CELL_SIZE, "Me");

        log = getLog();

        inputs[0] = getTextField(this, 0);
        inputs[1] = getTextField(this, 1);

        GridPane statPane = new GridPane();
        statPane.setHgap(10);
        statPane.setVgap(10);
        Label cLabel = getLegendLabel("Coordinates for attack:", "#ffffff");
        GridPane.setMargin(cLabel, new Insets(10, 0, 0, 10));
        statPane.add(cLabel, 0, 0);
        statPane.add(getLegendLabel("X coordinate:", "#ffffff"), 1, 1);
        statPane.add(getLegendLabel("Y coordinate:", "#ffffff"), 1, 2);
        statPane.add(inputs[0], 2, 1);
        statPane.add(inputs[1], 2, 2);
        statPane.setAlignment(Pos.TOP_RIGHT);

        HBox mainBox = new HBox(ships, opponentShips);
        mainBox.setAlignment(Pos.TOP_LEFT);
        mainBox.setSpacing(10);

        HBox supportBox = new HBox(log, statPane);
        supportBox.setPadding(new Insets(0, 0, 10, 10));
        supportBox.setAlignment(Pos.TOP_LEFT);
        supportBox.setSpacing(10);

        VBox vBox = new VBox(mainBox, supportBox);
        vBox.setSpacing(10);

        prepareGame(ocean, opponentOcean);

        return vBox;
    }

    /**
     * @param ocean - player's Ocean build on Set Scene
     * @return root object of MultiPlay Scene
     */
    public StackPane getRoot(Stage stage, Ocean ocean, Ocean opponentOcean) {
        Image image = new Image(getClass().getResourceAsStream(GAME));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(GAME_MUSIC);
        //Music.play();

        this.stage = stage;
        gameBoard = buildBoard(false, SIZE, CELL_SIZE, MultiPlayScene::shoot);
        opponentBoard = buildBoard(true, SIZE, CELL_SIZE, MultiPlayScene::shoot);

        return new StackPane(img, buildBox(ocean, opponentOcean));
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
        return opponentBoard[x][y];
    }

    public TextField getInput(int id) {
        return inputs[id];
    }

    public Ocean getOcean() {
        return ocean;
    }
}
