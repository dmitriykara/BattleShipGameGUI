package main.java.additionals;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * Music MediaPlayer
 */
public class Music {
    private static Music music;
    private static double DELTA = 0.05;
    private MediaPlayer player;
    private boolean pause;

    public static Music getInstance() {
        if (Music.music == null)
            Music.music = new Music();
        return music;
    }

    public void setSong(String path) {
        Media media = new Media(getClass().getClassLoader().getResource(path).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        player.setVolume(0.5);
        getInstance().pause = false;
        getInstance().player = player;
    }

    public static void play() {
        getInstance().player.play();
    }

    public static void pause() {
        if (getInstance().pause) {
            getInstance().pause = false;
            getInstance().player.play();
        }
        else {
            getInstance().pause = true;
            getInstance().player.stop();
        }
    }

    public static void stop() {
        getInstance().player.stop();
    }

    public static void volumeUp() {
        getInstance().player.setVolume(getInstance().player.getVolume() + DELTA);
    }

    public static void volumeDown() {
        getInstance().player.setVolume(getInstance().player.getVolume() - DELTA);
    }
}
