package org.example.gui;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameWindow {

    private final BorderPane korzen;
    private final Label pasekStatusu;
    private final BoardView plansza;

    public GameWindow() {
        korzen=new BorderPane();
        pasekStatusu=new Label("status: GUI z pusta plansza");
        plansza=new BoardView();

        inicjalizujUklad();
    }
    private void inicjalizujUklad() {
        pasekStatusu.setStyle(
            "-fx-border-color: pink;" +
            "-fx-padding: 10;"
        );

        korzen.setCenter(plansza);
        korzen.setBottom(pasekStatusu);
    }
    public Parent getKorzen() {
        return korzen;
    }
}
