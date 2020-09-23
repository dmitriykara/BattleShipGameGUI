package main.java.additionals;

import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.util.Duration;
import main.java.gameEngine.Ship;
import main.java.scenes.GameScene;
import main.java.scenes.SetScene;

import static main.java.Main.SIZE;

public class ButtonActions {
    public static String defaultColor = "-fx-background-color: #104e8b; ";
    public static String defaultBorder = "-fx-border-color: white; ";
    public static String onEnteredBorder = "-fx-border-color: #bc0c1a";
    public static String onExitedBorder = "-fx-border-color: white";
    public static String emptySeaColor = "-fx-background-color: #00ecff; ";
    public static String emptySeaColorEntered = "-fx-background-color: #076d9f; ";
    public static String hitColor = "-fx-background-color: #ffd700; ";
    public static String hitColorEntered = "-fx-background-color: #ca9502; ";
    public static String deadShipColor = "-fx-background-color: #bc0c1a; ";
    public static String deadShipColorEntered = "-fx-background-color: #b23c4e; ";
    public static String successColor = "-fx-background-color: #1cbd4c; ";
    public static String successColorEntered = "-fx-background-color: #147832; ";

    /**
     * update scene's gameBoardX and gameBoardY values if button get focus
     *
     * @param scene Scene object
     */
    public static void updateFields(GameScene scene, final int x, final int y) {
        scene.setGameBoardX(x);
        scene.setGameBoardY(y);
        scene.getInput(0).setText(Integer.toString(y));
        scene.getInput(1).setText(Integer.toString(x));
    }

    /**
     * Default styles for button
     *
     * @param opponent option for MultiPlay Scene
     */
    public static void resetButton(GameScene scene, final int x, final int y, boolean opponent) {
        Button button;
        if (!opponent)
            button = scene.getButton(y, x);
        else
            button = scene.getOpponentButton(y, x);

        button.setStyle(defaultColor + defaultBorder);
        button.setOnMouseEntered(event -> {
            button.setStyle(defaultColor + onEnteredBorder);
            updateFields(scene, x, y);
        });
        button.setOnMouseExited(event -> button.setStyle(defaultColor + onExitedBorder));
        button.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    button.setStyle(defaultColor + onEnteredBorder);
                    updateFields(scene, x, y);
                } else
                    button.setStyle(defaultColor + onExitedBorder);
            }
        });
    }

    /**
     * Styles for emptySea button
     *
     * @param opponent option for MultiPlay Scene
     */
    public static void emptySeaButton(GameScene scene, final int x, final int y, boolean opponent) {
        Button button;
        if (!opponent)
            button = scene.getButton(y, x);
        else
            button = scene.getOpponentButton(y, x);
        button.setStyle(emptySeaColor + defaultBorder);
        button.setOnMouseEntered(event -> {
            button.setStyle(emptySeaColorEntered + onEnteredBorder);
            updateFields(scene, x, y);
        });
        button.setOnMouseExited(event -> button.setStyle(emptySeaColor + onExitedBorder));
        button.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                button.setStyle(emptySeaColorEntered + onEnteredBorder);
                updateFields(scene, x, y);
            } else
                button.setStyle(emptySeaColor + onExitedBorder);
        });
    }

    /**
     * Styles for hit ship button
     *
     * @param opponent option for MultiPlay Scene
     */
    public static void hitButton(GameScene scene, final int x, final int y, boolean opponent) {
        Button button;
        if (!opponent)
            button = scene.getButton(y, x);
        else
            button = scene.getOpponentButton(y, x);
        button.setStyle(hitColor + defaultBorder);
        button.setOnMouseEntered(event -> {
            button.setStyle(hitColorEntered + onEnteredBorder);
            updateFields(scene, x, y);
        });
        button.setOnMouseExited(event -> button.setStyle(hitColor + onExitedBorder));
        button.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    button.setStyle(hitColorEntered + onEnteredBorder);
                    updateFields(scene, x, y);
                } else
                    button.setStyle(hitColor + onExitedBorder);
            }
        });
    }

    /**
     * Styles for killed ship button
     *
     * @param opponent option for MultiPlay Scene
     */
    public static void killButton(GameScene scene, final int x, final int y, boolean opponent) {
        Button button;
        if (!opponent)
            button = scene.getButton(y, x);
        else
            button = scene.getOpponentButton(y, x);
        button.setStyle(deadShipColor + defaultBorder);
        button.setOnMouseEntered(event -> {
            button.setStyle(deadShipColorEntered + onEnteredBorder);
            updateFields(scene, x, y);
        });
        button.setOnMouseExited(event -> button.setStyle(deadShipColor + onExitedBorder));
        button.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    button.setStyle(deadShipColorEntered + onEnteredBorder);
                    updateFields(scene, x, y);
                } else
                    button.setStyle(deadShipColor + onExitedBorder);
            }
        });
    }

    /**
     * Styles for enter button action
     */
    public static void shipButtonEntered(SetScene scene, final int x, final int y) {
        updateFields(scene, x, y);

        Ship ship = scene.ship;
        if (ship == null) {
            if (scene.getOcean().getShip(x, y).isEmptySea())
                scene.getButton(y, x).setStyle(defaultColor + defaultBorder);
            else
                scene.getButton(y, x).setStyle(successColorEntered + defaultBorder);
            return;
        }

        boolean ok = ship.okToPlaceShipAt(x, y, ship.isHorizontal(), scene.ocean);
        int tmpX = x;
        int tmpY = y;

        if (ship.isHorizontal())
            ok &= y + ship.getLength() <= SIZE;
        else
            ok &= x + ship.getLength() <= SIZE;

        for (int i = 0; i < ship.getLength(); i++) {
            Button tmpButton = scene.getButton(tmpY, tmpX);
            if (ok)
                tmpButton.setStyle(successColorEntered + defaultBorder);
            else
                tmpButton.setStyle(deadShipColorEntered + defaultBorder);

            if (ship.isHorizontal())
                ++tmpY;
            else
                ++tmpX;
            if (tmpX == SIZE || tmpY == SIZE)
                break;
        }
    }

    /**
     * Styles for exit button action
     */
    public static void shipButtonExited(SetScene scene, final int x, final int y) {
        Ship ship = scene.ship;
        if (ship == null) {
            if (scene.getOcean().getShip(x, y).isEmptySea())
                scene.getButton(y, x).setStyle(defaultColor + defaultBorder);
            else
                scene.getButton(y, x).setStyle(successColor + defaultBorder);
            return;
        }

        int tmpX = x;
        int tmpY = y;

        for (int i = 0; i < ship.getLength(); i++) {
            Button tmpButton = scene.getButton(tmpY, tmpX);
            if (scene.getOcean().getShip(tmpX, tmpY).isEmptySea())
                tmpButton.setStyle(defaultColor + defaultBorder);
            else
                tmpButton.setStyle(successColor + defaultBorder);

            if (ship.isHorizontal())
                ++tmpY;
            else
                ++tmpX;
            if (tmpX == SIZE || tmpY == SIZE)
                break;
        }
    }

    /**
     * Styles for successfully placed ship button
     */
    public static void successButton(SetScene scene, final int x, final int y) {
        Button button = scene.getButton(y, x);
        button.setStyle(successColor + defaultBorder);
        button.setOnMouseEntered(event -> shipButtonEntered(scene, x, y));
        button.setOnMouseExited(event -> shipButtonExited(scene, x, y));
        button.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue)
                    shipButtonEntered(scene, x, y);
                else
                    shipButtonExited(scene, x, y);
            }
        });
    }

    /**
     * Styles for ship button
     */
    public static void shipButton(SetScene scene, final int x, final int y) {
        Button button = scene.getButton(y, x);
        button.setStyle(defaultColor + defaultBorder);
        button.setOnMouseEntered(mouseEvent -> shipButtonEntered(scene, x, y));
        button.setOnMouseExited(mouseEvent -> shipButtonExited(scene, x, y));

        button.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue)
                    shipButtonEntered(scene, x, y);
                else
                    shipButtonExited(scene, x, y);
            }
        });
    }

    /**
     * Animation for rotate button
     */
    public static void rotateButton(Button button) {
        if (button.getRotate() != 0) {
            return;
        }
        new Thread(() -> {
            RotateTransition rotation = new RotateTransition(Duration.seconds(0.25), button);
            rotation.setByAngle(45);
            rotation.setCycleCount(4);
            rotation.setAutoReverse(true);
            rotation.play();
            button.setOnRotationFinished(e -> button.setRotate(0));
        }).start();
    }
}
