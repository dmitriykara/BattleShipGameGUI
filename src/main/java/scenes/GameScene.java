package main.java.scenes;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.gameEngine.Ocean;


/**
 * Abstract class to union SetScene, SinglePlayScene and MultiPlayScene
 */
public abstract class GameScene {
    public abstract int getGameBoardX();

    public abstract void setGameBoardX(int value);

    public abstract int getGameBoardY();

    public abstract void setGameBoardY(int value);

    public abstract Button getButton(int x, int y);

    public abstract Button getOpponentButton(int x, int y);

    public abstract TextField getInput(int id);

    public abstract Ocean getOcean();
}
