package main.java.web;

import javafx.application.Platform;
import main.java.gameEngine.Ocean;
import main.java.scenes.ClientScene;
import main.java.scenes.MenuScene;
import main.java.scenes.MultiPlayScene;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static main.java.Main.NAME;
import static main.java.Main.OPPONENT;
import static main.java.additionals.ScenesAction.*;

public class Client implements Runnable {
    public static Client client;
    ObjectInputStream istream;
    ObjectOutputStream ostream;
    private Socket server;
    private boolean active;


    public Client(String ip, int port) throws IOException {
        server = new Socket(ip.trim(), port);
        active = true;
    }

    public static boolean join(String ip, int port) {
        try {
            client = new Client(ip, port);
        } catch (Exception ex) {
            return false;
        }
        new Thread(client).start();
        return true;
    }

    public static void stop() {
        Signals.leave();
        if (ClientScene.getInstance().play != null)
            ClientScene.getInstance().play.setDisable(false);
        if (client != null) {
            client.active = false;
            if (client.istream == null)
                try {
                    client.server.close();
                    Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
                    System.out.println("Client turned off");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            else {
                try {
                    client.istream.close();
                    client.ostream.close();
                    client.server.close();
                    client = null;
                    Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
                    System.out.println("Client turned off");
                } catch (Exception ex) {
                }
            }
        }
    }

    public static boolean write(String data) {
        if (client != null && client.active && client.server != null && client.server.isConnected()) {
            try {
                client.ostream.writeObject(data);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected");
            ostream = new ObjectOutputStream(server.getOutputStream());
            istream = new ObjectInputStream(server.getInputStream());

            OPPONENT = (String) istream.readObject();
            ostream.writeObject(NAME);

            try {
                ClientScene.getInstance().opponentOcean = (Ocean) istream.readObject();
                ostream.writeObject(ClientScene.getInstance().ocean);
                Platform.runLater(() -> toOnlineGame(ClientScene.getInstance().stage, ClientScene.getInstance().ocean,
                        ClientScene.getInstance().opponentOcean));
            } catch (EOFException ex) {
                active = false;
            }

            while (active) {
                try {
                    String data = (String) istream.readObject();
                    if (data.startsWith("shoot")) {
                        int x = Character.getNumericValue(data.charAt(6));
                        int y = Character.getNumericValue(data.charAt(8));
                        Platform.runLater(() -> MultiPlayScene.opponentShoot(x, y));
                    } else if (data.equals("leave"))
                        Platform.runLater(() -> {
                            active = false;
                            earlyVictory();
                            MultiPlayScene.endGame();
                        });
                    if (server.isClosed())
                        active = false;
                } catch (IOException ex) {
                    active = false;
                }
            }

            ClientScene.getInstance().play.setDisable(false);
            Signals.leave();
            istream.close();
            ostream.close();
            server.close();
        } catch (Exception ex) {
            Client.stop();
            Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
        }
    }
}
