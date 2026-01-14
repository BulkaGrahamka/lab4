package org.example.gui;

import javafx.scene.layout.StackPane;

public class Field extends StackPane {

    private final int wiersz;
    private final int kolumna;

    public Field(int wiersz, int kolumna) {
        this.wiersz = wiersz;
        this.kolumna = kolumna;

        setPrefSize(30, 30);
        setStyle(
            "-fx-border-color: black;" +
            "-fx-background-color: pink;"
        );
    }

    public int getWiersz() {
        return wiersz;
    }

    public int getKolumna() {
        return kolumna;
    }

    public void zaznacz() {
        setStyle(
            "-fx-border-color: black;" +
            "-fx-background-color: black;"
        );
    }
}
