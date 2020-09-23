package main.java.additionals;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import main.java.gameEngine.Battleship;
import main.java.gameEngine.Cruiser;
import main.java.gameEngine.Destroyer;
import main.java.gameEngine.Submarine;
import main.java.scenes.*;
import main.java.web.Client;
import main.java.web.Server;

import static main.java.additionals.ScenesAction.*;
import static main.java.scenes.SetScene.*;

public class KeyEvents {
    // Key actions filter for LoadScene
    public static EventHandler loadSceneFilter = (EventHandler<KeyEvent>) event -> {
        if (event.getCode() == KeyCode.ESCAPE)
            LoadScene.exit(event);
        if (event.getCode() == KeyCode.ENTER)
            toMenu(LoadScene.getInstance().stage);
        keyActions(event);
        event.consume();
    };
    // Key actions filter for MenuScene
    public static EventHandler menuSceneFilter = (EventHandler<KeyEvent>) event -> {
        KeyCombination multiKC = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);
        if (multiKC.match(event))
            MenuScene.toMultiPlay(MenuScene.getInstance().stage);
        else if (event.getCode() == KeyCode.ENTER)
            MenuScene.toSinglePlay(MenuScene.getInstance().stage);
        if (event.getCode() == KeyCode.ESCAPE)
            MenuScene.toLoadScene(MenuScene.getInstance().stage);
        keyActions(event);
        event.consume();
    };
    // Key actions filter for SetScene
    public static EventHandler setSceneFilter = (EventHandler<KeyEvent>) event -> {
        SetScene scene = getInstance();

        if (event.getCode() == KeyCode.ESCAPE)
            toMenu(scene.stage);
        if (event.getCode() == KeyCode.ENTER)
            preSet();
        if (event.getCode() == RESET_KEY)
            resetBoard();
        if (event.getCode() == AUTOPLACE_KEY)
            autoPlaceShips();
        if (event.getCode() == KeyCode.SPACE)
            toGame(scene.stage, scene.ocean);
        if (event.getCode() == SUBMARINE_KEY) {
            if (scene.submarines > 0) {
                ButtonActions.shipButtonExited(scene, scene.gameBoardX, scene.gameBoardY);
                scene.ship = new Submarine();
                ButtonActions.shipButtonEntered(scene, scene.gameBoardX, scene.gameBoardY);
            }
        }
        if (event.getCode() == DESTROYER_KEY) {
            if (scene.destroyers > 0) {
                ButtonActions.shipButtonExited(scene, scene.gameBoardX, scene.gameBoardY);
                scene.ship = new Destroyer();
                ButtonActions.shipButtonEntered(scene, scene.gameBoardX, scene.gameBoardY);
            }
        }
        if (event.getCode() == CRUISER_KEY) {
            if (scene.cruisers > 0) {
                ButtonActions.shipButtonExited(scene, scene.gameBoardX, scene.gameBoardY);
                scene.ship = new Cruiser();
                ButtonActions.shipButtonEntered(scene, scene.gameBoardX, scene.gameBoardY);
            }
        }
        if (event.getCode() == BATTLESHIP_KEY) {
            if (scene.battleships > 0) {
                ButtonActions.shipButtonExited(scene, scene.gameBoardX, scene.gameBoardY);
                scene.ship = new Battleship();
                ButtonActions.shipButtonEntered(scene, scene.gameBoardX, scene.gameBoardY);
            }
        }
        if (event.getCode() == TRANSPOSE_KEY && scene.ship != null)
            rotateShip();
        if (event.getCode() == KeyCode.TAB) {
            scene.inputs[scene.textFieldIndex++].requestFocus();
            if (scene.textFieldIndex == scene.inputs.length)
                scene.textFieldIndex = 0;
        }

        keyActions(event);
        setControls(getInstance(), event);
        event.consume();
    };
    // Key actions filter for clientScene
    public static EventHandler clientSceneFilter = (EventHandler<KeyEvent>) event -> {
        ClientScene scene = ClientScene.getInstance();

        if (event.getCode() == KeyCode.ESCAPE) {
            Client.stop();
            toMenu(scene.stage);
        }
        if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)
            for (int i = 0; i < scene.inputs.length; i++) {
                if (scene.inputs[i].isFocused()) {
                    scene.inputs[i].deletePreviousChar();
                    scene.textFieldIndex = i;
                }
            }
        if (event.getCode() == KeyCode.TAB) {
            scene.inputs[scene.textFieldIndex++].requestFocus();
            if (scene.textFieldIndex == scene.inputs.length)
                scene.textFieldIndex = 0;
        }

        event.consume();
    };
    // Key actions filter for ServerScene
    public static EventHandler serverSceneFilter = (EventHandler<KeyEvent>) event -> {
        ServerScene scene = ServerScene.getInstance();

        if (event.getCode() == KeyCode.ESCAPE) {
            Server.stop();
            toMenu(scene.stage);
        }
        if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)
            for (int i = 0; i < scene.inputs.length; i++) {
                if (scene.inputs[i].isFocused()) {
                    scene.inputs[i].deletePreviousChar();
                    scene.textFieldIndex = i;
                }
            }
        if (event.getCode() == KeyCode.TAB) {
            scene.inputs[scene.textFieldIndex++].requestFocus();
            if (scene.textFieldIndex == scene.inputs.length)
                scene.textFieldIndex = 0;
        }

        event.consume();
    };
    // Key actions filter for SinglePlayScene
    public static EventHandler singlePlaySceneFilter = (EventHandler<KeyEvent>) event -> {
        SinglePlayScene scene = SinglePlayScene.getInstance();

        if (event.getCode() == KeyCode.ESCAPE)
            toMenu(scene.stage);
        if (event.getCode() == KeyCode.ENTER)
            SinglePlayScene.preShoot();
        if (event.getCode() == KeyCode.TAB) {
            scene.inputs[scene.textFieldIndex++].requestFocus();
            if (scene.textFieldIndex == scene.inputs.length)
                scene.textFieldIndex = 0;
        }

        keyActions(event);
        setControls(scene, event);
        event.consume();
    };
    // Key actions filter for MultiPlayScene
    public static EventHandler multiPlaySceneFilter = (EventHandler<KeyEvent>) event -> {
        MultiPlayScene scene = MultiPlayScene.getInstance();

        if (event.getCode() == KeyCode.ESCAPE)
            if (leaveGameAlert()) {
                MultiPlayScene.endGame();
                toMenu(scene.stage);
            }
        if (event.getCode() == KeyCode.ENTER)
            MultiPlayScene.preShoot();
        if (event.getCode() == KeyCode.TAB) {
            scene.inputs[scene.textFieldIndex++].requestFocus();
            if (scene.textFieldIndex == scene.inputs.length)
                scene.textFieldIndex = 0;
        }

        keyActions(event);
        setControls(scene, event);
        event.consume();
    };
}
