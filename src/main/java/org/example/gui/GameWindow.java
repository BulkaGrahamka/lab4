package org.example.gui;

import org.example.client.Client;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.control.Alert;



public class GameWindow implements GameStateListener {
    @Override
    public void onBoardUpdate(char[][] planszaZSerwera) {
        javafx.application.Platform.runLater(() -> {
            plansza.wyswietlPlansze(planszaZSerwera);
            pasekStatusu.setText("Plansza zsynchronizowana z serwerem");
        });
    }
    private final BorderPane korzen;
    private final Label pasekStatusu;
    private BoardView plansza;
    private Client klient;
    private boolean mojaTura = false;
    private Button przyciskPas;
    private Button przyciskPoddaj;
    private final ScoreBoard panelWynikow;


    public void ustawKlienta(Client klient) {
        this.klient = klient;
    }

    public GameWindow() {
        korzen = new BorderPane();
        pasekStatusu = new Label("Wybierz rozmiar planszy");
        panelWynikow = new ScoreBoard();
        przyciskPas = new Button("Pas");
        przyciskPoddaj = new Button("Poddaj się :(");

        inicjalizujUklad();
    }

    private void inicjalizujUklad() {
        //ComboBox<String> wyborRozmiaru = new ComboBox<>();
        //wyborRozmiaru.getItems().addAll("19x19 (domyśln)", "13x13", "9x9");
        //wyborRozmiaru.setValue("19x19 (domyśln)");

        //wyborRozmiaru.setOnAction(e -> {
        //String wybor = wyborRozmiaru.getValue();

        //int rozmiar;
        //if (wybor.startsWith("9")) {
        //rozmiar = 9;
        //} else if (wybor.startsWith("13")) {
        //rozmiar = 13;
        //} else {
        //rozmiar = 19;
        //}
        //utworzNowaPlansze(rozmiar);
        //pasekStatusu.setText("Rozmiar planszy: " + rozmiar + "×" + rozmiar);
        //});

        //HBox gornyPanel = new HBox(10, new Label("Rozmiar planszy:"), wyborRozmiaru);
        HBox gornyPanel = new HBox(10, new Label("Plansza 19x19"));

        gornyPanel.setStyle("-fx-padding: 10;");

        korzen.setTop(gornyPanel);
        korzen.setBottom(pasekStatusu);

        utworzNowaPlansze(19);
        przyciskPas = new Button("Pas");
        przyciskPoddaj = new Button("Poddaj się");

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
    public void onYourTurn() {
        System.out.println("DEBUG: onYourTurn() w GUI");
        javafx.application.Platform.runLater(() -> {
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

    public Parent getKorzen() {
        return korzen;
    }

    public void onScoreUpdate(String kolor, int punkty) {
        Platform.runLater(() -> {
            if (kolor.equals("CZARNY")) {
                panelWynikow.dodajPunktyCzarnego(punkty);
            }
            else if (kolor.equals("BIALY")) {
                panelWynikow.dodajPunktyBialego(punkty);
            }
        });
    }

}

