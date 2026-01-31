package org.example.bot;

import org.example.board.Board;
import org.example.gui.GameStateListener;
import java.util.List;

/**
 * Kontroler gry lokalnej: gracz (czarny) vs bot (biały).
 * Nie korzysta z serwera.
 */
public class BotGameController {

    private final Board plansza;
    private final BotPlayer bot;
    private final GameStateListener interfejs;
    private boolean turaGracza = true;

    public BotGameController(GameStateListener interfejs) {
        this.interfejs = interfejs;
        this.plansza = new Board(19);
        this.bot = new BotPlayer();

        interfejs.onGameStart();
        interfejs.onYourTurn();
        interfejs.onBoardUpdate(konwertujPlansze());
    }

    /**
     * Obsługa ruchu gracza (czarny).
     */
    public void ruchGracza(int wiersz, int kolumna) {
        if (!turaGracza) return;

        int wynik = plansza.playMove(wiersz, kolumna, 1);
        if (wynik < 0) return;

        interfejs.onBoardUpdate(konwertujPlansze());
        turaGracza = false;

        wykonajRuchBota();
    }

    /**
     * Ruch bota (biały).
     */
    private void wykonajRuchBota() {
        int[] ruchBota = bot.wybierzRuch(plansza, 19);

        if (ruchBota != null) {
            plansza.playMove(ruchBota[0], ruchBota[1], 2);
        }

        interfejs.onBoardUpdate(konwertujPlansze());
        turaGracza = true;
        interfejs.onYourTurn();
    }

    /**
     * Konwersja planszy Board → char[][] dla GUI
     */
    private char[][] konwertujPlansze() {
        char[][] wynik = new char[19][19];
        List<String> linie = plansza.getBoardLines();

        for (int wiersz = 0; wiersz < 19; wiersz++) {
            String linia = linie.get(wiersz + 1);
            for (int kolumna = 0; kolumna < 19; kolumna++) {
                wynik[wiersz][kolumna] = linia.charAt(3 + kolumna * 2);
            }
        }
        return wynik;
    }
}

