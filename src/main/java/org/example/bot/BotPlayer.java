package org.example.bot;

import org.example.board.Board;
import java.util.Random;

/**
 * Prosty bot wykonujący legalne ruchy.
 * Aktualnie: losowy poprawny ruch.
 */
public class BotPlayer {

    private final Random losowanie = new Random();

    /**
     * Wybiera ruch dla bota (biały).
     *
     * @param plansza aktualna plansza gry
     * @param rozmiar rozmiar planszy (np. 19)
     * @return tablica {wiersz, kolumna} lub null jeśli bot pasuje
     */
    public int[] wybierzRuch(Board plansza, int rozmiar) {
        for (int proba = 0; proba < 500; proba++) {
            int wiersz = losowanie.nextInt(rozmiar);
            int kolumna = losowanie.nextInt(rozmiar);

            // UWAGA: tylko sprawdzamy poprawność
            if (plansza.playMove(wiersz, kolumna, 2) >= 0) {
                return new int[]{wiersz, kolumna};
            }
        }
        return null; // b
    }
}

