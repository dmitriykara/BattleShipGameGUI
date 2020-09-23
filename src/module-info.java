module BattleShipGUI {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    opens main.java.scenes to javafx.fxml;
    opens main.java.additionals to javafx.media;

    opens main.java;
}