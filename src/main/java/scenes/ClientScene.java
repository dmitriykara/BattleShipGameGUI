package main.java.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.java.additionals.ScenesAction;
import main.java.gameEngine.Ocean;
import main.java.web.Client;

import static main.java.Main.NAME;
import static main.java.Main.WEB;
import static main.java.additionals.ScenesAction.makeAlert;
import static main.java.web.Client.join;

public class ClientScene {
    private static ClientScene clientScene;
    public Stage stage;
    public Ocean ocean, opponentOcean;
    public TextField[] inputs = new TextField[3];
    public int textFieldIndex = 0;
    public Button play;

    public static ClientScene getInstance() {
        if (clientScene == null)
            clientScene = new ClientScene();
        return clientScene;
    }

    /**
     * @return root object of Server Scene
     */
    public StackPane getRoot(Stage stage, Ocean ocean) {
        Image image = new Image(getClass().getResourceAsStream(WEB));
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(stage.widthProperty());
        img.fitHeightProperty().bind(stage.heightProperty());

        //Music.getInstance().setSong(THEME_MUSIC);
        //Music.play();

        this.stage = stage;
        this.ocean = ocean;

        Button menu = new Button("Menu");
        menu.setPrefSize(150, 30);
        menu.setAlignment(Pos.CENTER);
        menu.setOnAction(e -> {
            Client.stop();
            ScenesAction.toMenu(stage);
        });

        play = new Button("Play");
        play.setPrefSize(150, 30);
        play.setAlignment(Pos.CENTER);
        play.setOnAction(e -> {
            String text = inputs[2].getText();
            if (text.length() < 3) {
                makeAlert("Nickname is too short");
                return;
            }
            NAME = text;

            String ip = inputs[0].getText();

            text = inputs[1].getText();
            int port = 0;
            try {
                port = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                makeAlert("Invalid port");
                return;
            }

            if (!join(ip, port))
                makeAlert("Server doesn't response");
            else
                play.setDisable(true);
        });

        VBox vBox = new VBox(menu, play);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        TextField input = new TextField("9000");
        input.setAlignment(Pos.CENTER);
        input.setPrefSize(150, 30);
        input.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (event.getCharacter().matches("[0-9]")) {
                String text = input.getText();
                if (text.length() < 5)
                    input.setText(text + event.getCharacter());
                input.positionCaret(text.length() + 1);
            }
            event.consume();
        });
        inputs[1] = input;

        TextField sinput = new TextField("localhost");
        sinput.setAlignment(Pos.CENTER);
        sinput.setPrefSize(150, 30);
        sinput.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String text = sinput.getText();
            sinput.setText(text + event.getCharacter());
            sinput.positionCaret(text.length() + 1);
            event.consume();
        });
        inputs[0] = sinput;

        TextField inputNick = new TextField("MrClient");
        inputNick.setAlignment(Pos.CENTER);
        inputNick.setPrefSize(150, 30);
        inputNick.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String text = inputNick.getText();
            if (text.length() < 8)
                inputNick.setText(text + event.getCharacter());
            inputNick.positionCaret(text.length() + 1);
            event.consume();
        });
        inputs[2] = inputNick;

        Label label = new Label("Enter server IP");
        label.setPrefSize(150, 30);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        Label slabel = new Label("Enter server port");
        slabel.setPrefSize(150, 30);
        slabel.setAlignment(Pos.CENTER);
        slabel.setTextAlignment(TextAlignment.CENTER);

        Label labelNick = new Label("Enter nickname");
        labelNick.setPrefSize(150, 30);
        labelNick.setAlignment(Pos.CENTER);
        labelNick.setTextAlignment(TextAlignment.CENTER);

        VBox webParams = new VBox(label, input);
        webParams.setAlignment(Pos.CENTER);
        webParams.setSpacing(20);

        VBox sParams = new VBox(slabel, sinput);
        sParams.setAlignment(Pos.CENTER);
        sParams.setSpacing(20);

        VBox persParams = new VBox(labelNick, inputNick);
        persParams.setAlignment(Pos.CENTER);
        persParams.setSpacing(20);

        HBox hBox = new HBox(sParams, webParams, persParams, vBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);

        return new StackPane(img, hBox);
    }
}
