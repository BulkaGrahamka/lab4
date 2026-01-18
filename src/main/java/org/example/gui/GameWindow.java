package org.example.gui;

import org.example.client.Client;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Główny komponent okna gry. Zarządza układem interfejsu,
 * zawiera planszę, panel wyników i przyciski. Implementuje
 * {@link GameStateListener}, aby reagować na komunikaty od serwera
 * przekazywane przez klasę {@link Client}.
 */
public class GameWindow implements GameStateListener {
    private final BorderPane korzen;
    private final Label pasekStatusu;
    private BoardView plansza;
    private Client klient;
    private boolean mojaTura = false;
    private final Button przyciskPas;
    private final Button przyciskPoddaj;
    private final ScoreBoard panelWynikow;

    /**
     * Tworzy nowy obiekt okna gry i inicjalizuje jego układ.
     */
    public GameWindow() {
        korzen = new BorderPane();
        pasekStatusu = new Label("Łączenie z serwerem...");
        panelWynikow = new ScoreBoard();
        przyciskPas = new Button("Pas");
        przyciskPoddaj = new Button("Poddaj się");
        inicjalizujUklad();
    }

    /**
     * Ustawia referencję do obiektu klienta, aby GUI mogło wysyłać ruchy.
     * @param klient Obiekt klienta sieciowego.
     */
    public void ustawKlienta(Client klient) {
        this.klient = klient;
    }

    /**
     * Buduje i konfiguruje wszystkie elementy interfejsu graficznego.
     */
    private void inicjalizujUklad() {
        HBox gornyPanel = new HBox(10, new Label("Plansza 19x19"));
        gornyPanel.setStyle("-fx-padding: 10;");

        korzen.setTop(gornyPanel);
        korzen.setBottom(pasekStatusu);

        utworzNowaPlansze(19);

        przyciskPas.setOnAction(e -> {
            if (!mojaTura) {
                pasekStatusu.setText("Nie możesz spasować, to nie Twoja tura");
                return;
            }
            if (klient != null) {
                klient.pas();
                mojaTura = false;
                pasekStatusu.setText("Wykonano pas - tura przeciwnika");
            }
        });

        przyciskPoddaj.setOnAction(e -> {
            if (klient != null) {
                klient.poddajSie();
                pasekStatusu.setText("Poddano grę");
            }
        });

        VBox panelPrawy = new VBox(15, panelWynikow, przyciskPas, przyciskPoddaj);
        panelPrawy.setStyle("-fx-padding: 10;");
        korzen.setRight(panelPrawy);
    }

    /**
     * Tworzy i umieszcza na scenie nową planszę o zadanym rozmiarze.
     * @param rozmiar Rozmiar planszy.
     */
    private void utworzNowaPlansze(int rozmiar) {
        plansza = new BoardView(rozmiar, pole -> {
            int wiersz = pole.getWiersz();
            int kolumna = pole.getKolumna();
            if (!mojaTura) {
                pasekStatusu.setText("Czekaj na swoją turę");
                return;
            }
            if (klient != null) {
                klient.wyslijRuch(wiersz, kolumna);
                mojaTura = false;
                pasekStatusu.setText("Ruch wysłany, tura przeciwnika");
            }
        });
        korzen.setCenter(plansza);
    }

    @Override
    public void onBoardUpdate(char[][] planszaZSerwera) {
        Platform.runLater(() -> {
            plansza.wyswietlPlansze(planszaZSerwera);
            pasekStatusu.setText("Plansza zsynchronizowana z serwerem");
        });
    }

    @Override
    public void onYourTurn() {
        Platform.runLater(() -> {
            mojaTura = true;
            pasekStatusu.setText("Twoja tura");
        });
    }

    @Override
    public void onGameEnd(String komunikat) {
        Platform.runLater(() -> {
            mojaTura = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec gry");
            alert.setHeaderText(null);
            alert.setContentText(komunikat);
            alert.showAndWait();
            pasekStatusu.setText(komunikat);
        });
    }

    @Override
    public void onScoreUpdate(String kolor, int punkty) {
        Platform.runLater(() -> {
            if (kolor.equals("CZARNY")) {
                panelWynikow.dodajPunktyCzarnego(punkty);
            } else if (kolor.equals("BIALY")) {
                panelWynikow.dodajPunktyBialego(punkty);
            }
        });
    }

    /**
     * Zwraca główny kontener (korzeń) interfejsu graficznego.
     * @return Główny kontener typu {@link Parent}.
     */
    public Parent getKorzen() {
        return korzen;
    }
}
