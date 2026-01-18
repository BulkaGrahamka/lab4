package org.example.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ScoreBoard extends VBox {

    private final Label punktyCzarny;
    private final Label punktyBialy;
    private int punktyCzarnego;
    private int punktyBialego;

    public ScoreBoard() {
        setSpacing(10);
        setStyle(
            "-fx-padding: 10;" +
            "-fx-border-color: black;" +
            "-fx-min-width: 160;"
        );

        Label tytul = new Label("Wyniki");
        tytul.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        punktyCzarny = new Label("Czarny: 0 pkt");
        punktyBialy = new Label("Biały: 0 pkt");

        getChildren().addAll(tytul, punktyCzarny, punktyBialy);
    }

    public void dodajPunktyCzarnego(int punkty) {
        punktyCzarnego += punkty;
        punktyCzarny.setText("Czarny: " + punktyCzarnego + " pkt");
    }

    public void dodajPunktyBialego(int punkty) {
        punktyBialego += punkty;
        punktyBialy.setText("Biały: " + punktyBialego + " pkt");
    }
}

