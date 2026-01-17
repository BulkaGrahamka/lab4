package org.example.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ScoreBoard extends VBox {

    private static final ScoreBoard INSTANCE = new ScoreBoard();

    private final Label punktyCzarnyLabel;
    private final Label punktyBialyLabel;
    private int punktyCzarny = 0;
    private int punktyBialy = 0;


    public ScoreBoard() {
        setSpacing(10);
        setStyle(
            "-fx-padding: 10;" +
            "-fx-border-color: black;" +
            "-fx-min-width: 160;"
        );

        Label tytul = new Label("Wyniki");
        tytul.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        punktyCzarnyLabel = new Label("Czarny: 0 pkt");
        punktyBialyLabel = new Label("Biały: 0 pkt");

        getChildren().addAll(tytul, punktyCzarnyLabel, punktyBialyLabel);
    }

    public void ustawPunktyCzarnego(int punkty) {
        this.punktyCzarny += punkty;
        punktyCzarnyLabel.setText("Czarny: " + this.punktyCzarny + " pkt");
    }

    public void ustawPunktyBialego(int punkty) {
        this.punktyBialy += punkty;
        punktyBialyLabel.setText("Biały: " + this.punktyBialy + " pkt");
    }

    public static ScoreBoard getInstance() {
        return INSTANCE;
    }
}

