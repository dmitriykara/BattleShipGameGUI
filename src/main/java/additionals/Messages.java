package main.java.additionals;

import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static main.java.Main.OPPONENT;

public class Messages {
    /**
     * Prints message when player hit a ship
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void hit(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> Nice shot at (%d; %d), Admiral!\n", x, y));
        text.setStyle("-fx-fill: #147832; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when player kill a ship
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void kill(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> Due to getting into (%d; %d),\n " +
                "the enemy went to feed the fish, Admiral!\n", x, y));
        text.setStyle("-fx-fill: #1cbd4c; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when player shot emptySea
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void miss(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> We shot at (%d; %d), Admiral! What an annoying miss\n", x, y));
        text.setStyle("-fx-fill: black; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when player shot the same position
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void repeat(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> Lightning doesn't hit 2 times in 1 place.\n" +
                "And you don't beat at (%d; %d), Admiral!\n", x, y));
        text.setStyle("-fx-fill: black; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when opposite player hit a player's ship
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void enemyHit(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> %s shot at (%d; %d), Admiral! It hurts\n", OPPONENT, x, y));
        text.setStyle("-fx-fill: #b23c4e; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when opposite player kill a player's ship
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void enemyKill(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> %s shot at (%d; %d).\n " +
                "We went to feed the fish, Admiral!\n", OPPONENT, x, y));
        text.setStyle("-fx-fill: #bc0c1a; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }

    /**
     * Prints message when opposite player shot a player's emptySea
     *
     * @param log - ScrollPane with TextFlow to print message
     * @params x, y - coordinates where player shot
     */
    public static void enemyMiss(ScrollPane log, int x, int y) {
        Text text = new Text(String.format("> %s shot at (%d; %d). We are lucky\n", OPPONENT, x, y));
        text.setStyle("-fx-fill: black; -fx-font-weight:bold;");

        ((TextFlow) log.getContent()).getChildren().add(text);
        log.setVvalue(1);
    }
}
