package org.example.gui;

import javafx.scene.layout.GridPane;

public class BoardView extends GridPane {

    public static final int ROZMIAR_PLANSZY = 19;

    public BoardView() {
        inicjalizujPlansze();
    }
    private void inicjalizujPlansze() {
        for (int wiersz = 0; wiersz < ROZMIAR_PLANSZY; wiersz++) {
            for (int kolumna = 0; kolumna < ROZMIAR_PLANSZY; kolumna++) {
                Field pole = new Field();
                add(pole, kolumna, wiersz);
            }
        }
    }
}
