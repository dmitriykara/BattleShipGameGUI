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
import main.java.additionals.ScenesAction;
import main.java.gameEngine.Ocean;
import main.java.gameEngine.Ship;

import static main.java.Main.*;

/**
 * Class creates SinglePlay Scene
 */
public class SinglePlayScene extends GameScene {
    private static SinglePlayScene singlePlayScene;
    public ScrollPane log;
    public Button[][] gameBoard;
    public Ocean ocean;
    public int gameBoardX = 0;
    public int gameBoardY = 0;
    public TextField[] inputs = new TextField[2];
    public Stage stage;
    public int textFieldIndex = 0;
    private Label[] statistics = new Label[5];

    public static SinglePlayScene getInstance() {
        if (singlePlayScene == null)
            singlePlayScene = new SinglePlayScene();
        return singlePlayScene;
    }

    /**
     * Set fields values and set styles for buttons
     */
    public static void prepareGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ButtonActions.resetButton(getInstance(), i, j, false);
            }
        }
        for (int i = 0; i < getInstance().inputs.length; i++) {
            getInstance().inputs[i].setText("0");
        }
        getInstance().statistics[0].setText(Integer.toString(SIZE));
        for (int i = 1; i < getInstance().statistics.length; i++) {
            getInstance().statistics[i].setText("0");
        }
        getInstance().textFieldIndex = 0;
        getInstance().gameBoardX = 0;
        getInstance().gameBoardY = 0;
        TextFlow textFlow = (TextFlow) getInstance().log.getContent();
        textFlow.getChildren().clear();
    }

    /**
     * Update kills counter and label
     */
    private static void kill(boolean oneCellShip) {
        penetration(true);
        Label killed = getInstance().statistics[1];
        int oldKilled = Integer.parseInt(killed.getText());
        killed.setText(Integer.toString(++oldKilled));

        Label alive = getInstance().statistics[0];
        alive.setText(Integer.toString(SIZE - oldKilled));

        if (!oneCellShip) {
            Label hit = getInstance().statistics[2];
            int oldHit = Integer.parseInt(hit.getText());
            hit.setText(Integer.toString(--oldHit));
        }
    }

    /**
     * Update penetrations counter and label
     */
    private static void penetration(boolean alreadyHit) {
        shot();
        Label penetrations = getInstance().statistics[3];
        int oldPenetrations = Integer.parseInt(penetrations.getText());
        penetrations.setText(Integer.toString(++oldPenetrations));

        if (!alreadyHit) {
            Label hit = getInstance().statistics[2];
            int oldHit = Integer.parseInt(hit.getText());
            hit.setText(Integer.toString(++oldHit));
        }
    }

    /**
     * Update shots counter and label
     */
    private static void shot() {
        Label shots = getInstance().statistics[4];
        int oldShots = Integer.parseInt(shots.getText());
        shots.setText(Integer.toString(++oldShots));
    }

    /**
     * Actions for player's shoot
     */
    public static void shoot(int x, int y) {
        Button button = getInstance().gameBoard[y][x];
        int result = getInstance().ocean.shootAt(x, y);
        switch (result) {
            case -1:
                Messages.miss(getInstance().log, x, y);
                shot();
                ButtonActions.emptySeaButton(getInstance(), x, y, false);
                break;
            case 0:
                Messages.repeat(getInstance().log, x, y);
                shot();
                ButtonActions.rotateButton(button);
                break;
            case 1:
                if (!getInstance().ocean.isSunk(x, y)) {
                    Messages.hit(getInstance().log, x, y);
                    ButtonActions.hitButton(getInstance(), x, y, false);
                    penetration(getInstance().ocean.getShip(x, y).isHitTwice());
                } else {
                    Ship ship = getInstance().ocean.getShip(x, y);
                    Messages.kill(getInstance().log, x, y);
                    x = ship.getBowRow();
                    y = ship.getBowColumn();
                    for (int i = 0; i < ship.getLength(); i++) {
                        ButtonActions.killButton(getInstance(), x, y, false);
                        if (ship.isHorizontal())
                            ++y;
                        else
                            ++x;
                    }
                    kill(ship.getLength() == 1);
                    if (getInstance().ocean.isGameOver()) {
                        ScenesAction.loadWinResults(getInstance().ocean, null);
                        ScenesAction.toMenu(getInstance().stage);
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
    private VBox buildBox() {
        VBox ships = ScenesAction.buildShipsContainer(gameBoard, SIZE, CELL_SIZE, "Opponent");

        log = ScenesAction.getLog();

        inputs[0] = ScenesAction.getTextField(this, 0);
        inputs[1] = ScenesAction.getTextField(this, 1);

        GridPane statPane = new GridPane();
        statPane.setHgap(10);
        statPane.setVgap(10);
        statPane.add(ScenesAction.getLegendLabel("Coordinates for attack:", "#ffffff"), 0, 0);
        statPane.add(ScenesAction.getLegendLabel("X coordinate:", "#ffffff"), 1, 1);
        statPane.add(ScenesAction.getLegendLabel("Y coordinate:", "#ffffff"), 1, 2);
        statPane.add(inputs[0], 2, 1);
        statPane.add(inputs[1], 2, 2);
        statPane.setAlignment(Pos.TOP_RIGHT);

        GridPane statisticsPane = new GridPane();
        statisticsPane.setVgap(10);
        statisticsPane.setHgap(10);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Alive: ", "#000000"), 1, 1);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Killed ships: ", "#000000"), 1, 2);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Hit ships: ", "#000000"), 1, 3);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Penetrations: ", "#000000"), 1, 4);
        statisticsPane.add(
                ScenesAction.getLegendLabel("Shots: ", "#000000"), 1, 5);

        statistics[0] = ScenesAction.getLegendLabel(Integer.toString(SIZE), "#000000");
        statistics[1] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[2] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[3] = ScenesAction.getLegendLabel("0", "#000000");
        statistics[4] = ScenesAction.getLegendLabel("0", "#000000");
        for (int i = 0; i < statistics.length; i++) {
            statisticsPane.add(statistics[i], 2, i + 1);
        }

        HBox mainBox = new HBox(ships, statisticsPane);
        mainBox.setAlignment(Pos.TOP_LEFT);
        mainBox.setSpacing(10);

        HBox supportBox = new HBox(log, statPane);
        supportBox.setPadding(new Insets(0, 0, 10, 10));
        supportBox.setAlignment(Pos.TOP_LEFT);
        supportBox.setSpacing(10);

        VBox vBox = new VBox(mainBox, supportBox);
        vBox.setSpacing(10);

        prepareGame();

        return vBox;
    }

    /**
     * @param ocean - player's Ocean build on Set Scene
     * @return root object of SinglePlay Scene
     */
    public StackPane getRoot(Stage stage, Ocean ocean) {
        Image image = new Image(getClass().getResourceAsStream(GAME));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(GAME_MUSIC);
        //Music.play();

        this.stage = stage;
        gameBoard = ScenesAction.buildBoard(false, SIZE, CELL_SIZE, SinglePlayScene::shoot);

        if (ocean == null) {
            this.ocean = Ocean.createGame();
            this.ocean.placeAllShipsRandomly();
        } else
            this.ocean = ocean;

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
