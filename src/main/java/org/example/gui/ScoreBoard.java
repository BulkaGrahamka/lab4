package org.example.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ScoreBoard extends VBox {

    private final Label punktyCzarny;
    private final Label punktyBialy;

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

    public void ustawPunktyCzarnego(int punkty) {
        punktyCzarny.setText("Czarny: " + punkty + " pkt!");
    }

    public void ustawPunktyBialego(int punkty) {
        punktyBialy.setText("Biały: " + punkty + " pkt!");
    }
}

