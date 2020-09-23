package main.java.web;

import javafx.application.Platform;
import main.java.gameEngine.Ocean;
import main.java.scenes.MenuScene;
import main.java.scenes.MultiPlayScene;
import main.java.scenes.ServerScene;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static main.java.Main.NAME;
import static main.java.Main.OPPONENT;
import static main.java.additionals.ScenesAction.*;

public class Server implements Runnable {
    private static Server server;
    private ServerSocket serverSocket;
    private Socket client;
    private ObjectInputStream istream;
    private ObjectOutputStream ostream;
    private boolean active;

    private Server(int port, int timeout) throws IOException {
        serverSocket = new ServerSocket(port, 1);
        serverSocket.setSoTimeout(timeout);
        System.out.println("Server created on " + InetAddress.getLocalHost().getHostAddress() +
                ":" + serverSocket.getLocalPort());
        active = true;
    }

    public static boolean startServer(int port) {
        try {
            server = new Server(port, 0);
        } catch (Exception ex) {
            return false;
        }
        new Thread(server).start();
        return true;
    }

    public static void stop() {
        Signals.leave();
        if (ServerScene.getInstance().play != null)
            ServerScene.getInstance().play.setDisable(false);
        if (server != null) {
            server.active = false;
            if (server.istream == null)
                try {
                    server.serverSocket.close();
                    Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
                    System.out.println("Server turned off");
                } catch (Exception ex) {
                }
            else {
                try {
                    server.istream.close();
                    server.ostream.close();
                    server.client.close();
                    server.serverSocket.close();
                    server = null;
                    Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
                    System.out.println("Server turned off");
                } catch (Exception ex) {
                }
            }
        }
    }

    public static boolean write(String data) {
        if (server != null && server.active && server.client != null && server.client.isConnected()) {
            try {
                server.ostream.writeObject(data);
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
            client = serverSocket.accept();
            System.out.println("Client connected");
            ostream = new ObjectOutputStream(client.getOutputStream());
            istream = new ObjectInputStream(client.getInputStream());

            ostream.writeObject(NAME);
            OPPONENT = (String) istream.readObject();

            try {
                ostream.writeObject(ServerScene.getInstance().ocean);
                ServerScene.getInstance().opponentOcean = (Ocean) istream.readObject();
                Platform.runLater(() -> toOnlineGame(ServerScene.getInstance().stage, ServerScene.getInstance().ocean,
                        ServerScene.getInstance().opponentOcean));
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
                    } else if (data.equals("leave")) {
                        Platform.runLater(() -> {
                            active = false;
                            earlyVictory();
                            MultiPlayScene.endGame();
                        });
                    }
                    if (client.isClosed())
                        active = false;
                } catch (IOException ex) {
                    active = false;
                }
            }

            ServerScene.getInstance().play.setDisable(false);
            Signals.leave();
            istream.close();
            ostream.close();
            client.close();
            serverSocket.close();
        } catch (Exception ex) {
            Server.stop();
            Platform.runLater(() -> toMenu(MenuScene.getInstance().stage));
        }
    }
}
