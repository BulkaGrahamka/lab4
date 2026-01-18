package org.example.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Komponent GUI wyświetlający aktualne wyniki (liczbę zbitych kamieni)
 * dla obu graczy.
 */
public class ScoreBoard extends VBox {
    private final Label punktyCzarnyLabel;
    private final Label punktyBialyLabel;
    private int punktyCzarnego = 0;
    private int punktyBialego = 0;

    /**
     * Tworzy nowy panel wyników i inicjalizuje jego wygląd.
     */
    public ScoreBoard() {
        setSpacing(10);
        setStyle("-fx-padding: 10; -fx-border-color: black; -fx-min-width: 160;");

        Label tytul = new Label("Wyniki");
        tytul.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        punktyCzarnyLabel = new Label("Czarny: 0 pkt");
        punktyBialyLabel = new Label("Biały: 0 pkt");

        getChildren().addAll(tytul, punktyCzarnyLabel, punktyBialyLabel);
    }

    /**
     * Dodaje punkty do wyniku czarnego gracza i aktualizuje etykietę.
     *
     * @param punkty Liczba punktów do dodania.
     */
    public void dodajPunktyCzarnego(int punkty) {
        punktyCzarnego += punkty;
        punktyCzarnyLabel.setText("Czarny: " + punktyCzarnego + " pkt");
    }

    /**
     * Dodaje punkty do wyniku białego gracza i aktualizuje etykietę.
     *
     * @param punkty Liczba punktów do dodania.
     */
    public void dodajPunktyBialego(int punkty) {
        punktyBialego += punkty;
        punktyBialyLabel.setText("Biały: " + punktyBialego + " pkt");
    }
}