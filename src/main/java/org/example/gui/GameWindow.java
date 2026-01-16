package org.example.gui;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
public class GameWindow {

    private final BorderPane korzen;
    private final Label pasekStatusu;
    private BoardView plansza;

    public GameWindow() {
        korzen = new BorderPane();
        pasekStatusu = new Label("Wybierz rozmiar planszy");

        inicjalizujUklad();
    }

    private void inicjalizujUklad() {
        ComboBox<String> wyborRozmiaru = new ComboBox<>();
        wyborRozmiaru.getItems().addAll("19x19 (domyśln)", "13x13", "9x9");
        wyborRozmiaru.setValue("19x19 (domyśln)");

        wyborRozmiaru.setOnAction(e -> {
            String wybor = wyborRozmiaru.getValue();

            int rozmiar;
            if (wybor.startsWith("9")) {
                rozmiar = 9;
            } else if (wybor.startsWith("13")) {
                rozmiar = 13;
            } else {
                rozmiar = 19;
            }
            utworzNowaPlansze(rozmiar);
            pasekStatusu.setText("Rozmiar planszy: " + rozmiar + "×" + rozmiar);
        });

        HBox gornyPanel = new HBox(10, new Label("Rozmiar planszy:"), wyborRozmiaru);
        gornyPanel.setStyle("-fx-padding: 10;");

        korzen.setTop(gornyPanel);
        korzen.setBottom(pasekStatusu);

        utworzNowaPlansze(19);
    }

    private void utworzNowaPlansze(int rozmiar) {
        plansza = new BoardView(rozmiar, pole -> {
            pasekStatusu.setText(
                "Kliknięto pole: (" +
                pole.getWiersz() + ", " +
                pole.getKolumna() + ")"
            );
        });

        korzen.setCenter(plansza);
    }

    public Parent getKorzen() {
        return korzen;
    }
}
