package org.example.gui;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameWindow {

    private final BorderPane korzen;
    private final Label pasekStatusu;
    private final Label miejsceNaPlansze;

    public GameWindow() {
        korzen=new BorderPane();
        pasekStatusu=new Label("status: GUI uruchomione");
        miejsceNaPlansze= new Label("plansza gry (do zaimplementowania)");
        inicjalizujUklad();
    }
    private void inicjalizujUklad() {
        miejsceNaPlansze.setStyle(
            "-fx-border-color: pink;" +
            "-fx-alignment: center;" +
            "-fx-font-size: 18px;"
        );
        pasekStatusu.setStyle(
            "-fx-padding: 10;" +
            "-fx-border-color: pink;"
        );

        korzen.setCenter(miejsceNaPlansze);
        korzen.setBottom(pasekStatusu);
    }
    public Parent getKorzen() {
        return korzen;
    }
}
