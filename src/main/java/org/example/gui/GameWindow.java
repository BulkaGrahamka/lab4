package org.example.gui;

import java.util.function.Consumer;

import org.example.bot.BotGameController;
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
    private boolean graRozpoczeta = false;
    private Consumer<Field> obslugaKlikniecia = null;
    private BotGameController kontrolerBota = null;



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
    public void ustawObslugeKlikniecia(Consumer<Field> obsluga) {
        this.obslugaKlikniecia = obsluga;
    }
    public void ustawKontrolerBota(BotGameController kontroler) {
        this.kontrolerBota = kontroler;
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

        

        przyciskPas.setOnAction(e -> {
            if (kontrolerBota != null) {
                kontrolerBota.pasGracza();
                return;
            }
            if (!mojaTura) return;
            klient.pas();
            mojaTura = false;
        });

        przyciskPoddaj.setOnAction(e -> {
            if (kontrolerBota != null) {
                kontrolerBota.poddajSie();
                return;
            }
            klient.poddajSie();
        });

       

        VBox panelPrawy = new VBox(15, panelWynikow, przyciskPas, przyciskPoddaj);
        panelPrawy.setStyle("-fx-padding: 10;");
        korzen.setRight(panelPrawy);
    }

    /**
     * Tworzy i umieszcza na scenie nową planszę o zadanym rozmiarze.
     * @param rozmiar Rozmiar planszy.
     */
    protected void utworzNowaPlansze(int rozmiar) {
        plansza = new BoardView(rozmiar, pole -> {
            if (obslugaKlikniecia != null) {
                obslugaKlikniecia.accept(pole);
                return;
            }
            int wiersz = pole.getWiersz();
            int kolumna = pole.getKolumna();
            if (!mojaTura) {
                pasekStatusu.setText("Czekaj na swoją turę");
                return;
            }
            if (kontrolerBota != null) {
                kontrolerBota.ruchGracza(wiersz, kolumna);
                return;
            }
            klient.wyslijRuch(wiersz, kolumna);
            pasekStatusu.setText("Ruch wysłany, tura przeciwnika");
            mojaTura = false;

                
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
    public void onGameStart() {
        Platform.runLater(() -> {
            if (!graRozpoczeta) {
                utworzNowaPlansze(19);
                graRozpoczeta = true;
                pasekStatusu.setText("Oczekiwanie na ruch...");
            }
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
