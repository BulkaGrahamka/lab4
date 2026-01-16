package org.example.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Field extends StackPane {

    private final int wiersz;
    private final int kolumna;

    private StoneType kamien;
    private Circle widokKamienia;

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

 
    public void ustawKamien(StoneType typ) {
        if (kamien != null) {
            return;
        }

        kamien = typ;
        widokKamienia = new Circle(12);
        widokKamienia.setFill(
            typ == StoneType.BLACK ? Color.BLACK : Color.WHITE
        );

        getChildren().add(widokKamienia);
    }
}
