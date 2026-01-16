package org.example.gui;

import javafx.scene.layout.GridPane;
import java.util.function.Consumer;

public class BoardView extends GridPane {

    private final int rozmiar;
    private final Consumer<Field> obslugaKlikniecia;
    private StoneType nastepnyKamien = StoneType.BLACK;

    public BoardView(int rozmiar, Consumer<Field> obslugaKlikniecia) {
        this.obslugaKlikniecia = obslugaKlikniecia;
        this.rozmiar =rozmiar;
        inicjalizujPlansze();
    }

    private void inicjalizujPlansze() {
        getChildren().clear();

        for (int wiersz = 0; wiersz < rozmiar; wiersz++) {
            for (int kolumna = 0; kolumna <rozmiar; kolumna++) {
                Field pole = new Field(wiersz, kolumna);

                pole.setOnMouseClicked(e -> {
                    pole.ustawKamien(nastepnyKamien);
                    nastepnyKamien =
                        (nastepnyKamien == StoneType.BLACK)
                            ? StoneType.WHITE
                            : StoneType.BLACK;

                    obslugaKlikniecia.accept(pole);
                });

                add(pole, kolumna, wiersz);
            }
        }
    }
}

