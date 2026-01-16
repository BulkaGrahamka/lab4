package org.example.gui;

import javafx.scene.layout.GridPane;
import java.util.function.Consumer;

public class BoardView extends GridPane {

    public static final int ROZMIAR_PLANSZY = 19;

    private final Consumer<Field> obslugaKlikniecia;
    private StoneType nastepnyKamien = StoneType.BLACK;

    public BoardView(Consumer<Field> obslugaKlikniecia) {
        this.obslugaKlikniecia = obslugaKlikniecia;
        inicjalizujPlansze();
    }

    private void inicjalizujPlansze() {
        for (int wiersz = 0; wiersz < ROZMIAR_PLANSZY; wiersz++) {
            for (int kolumna = 0; kolumna < ROZMIAR_PLANSZY; kolumna++) {
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

