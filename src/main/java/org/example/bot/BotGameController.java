package org.example.bot;

import org.example.board.Board;
import org.example.gui.GameStateListener;

import java.util.List;

public class BotGameController {

    private final Board plansza;
    private final BotPlayer bot;
    private final GameStateListener interfejs;

    private boolean turaGracza = true;
    private boolean graZakonczona = false;

    private int punktyCzarnego = 0;
    private int punktyBialego = 0;

    private int kolejnePasy = 0;

    public BotGameController(GameStateListener interfejs) {
        this.interfejs = interfejs;
        this.plansza = new Board(19);
        this.bot = new BotPlayer();

        interfejs.onGameStart();
        interfejs.onBoardUpdate(konwertujPlansze());
        interfejs.onYourTurn();
    }

    public void ruchGracza(int wiersz, int kolumna) {
        if (!turaGracza || graZakonczona) return;

        int zbite = plansza.playMove(wiersz, kolumna, 1);
        if (zbite < 0) return;

        punktyCzarnego += zbite;
        interfejs.onScoreUpdate("CZARNY", zbite);

        kolejnePasy = 0;
        interfejs.onBoardUpdate(konwertujPlansze());

        turaGracza = false;
        wykonajRuchBota();
    }

    public void pasGracza() {
        if (!turaGracza || graZakonczona) return;

        kolejnePasy++;
        if (sprawdzKoniecGry()) return;

        turaGracza = false;
        wykonajRuchBota();
    }

    public void poddajSie() {
        if (graZakonczona) return;
        graZakonczona = true;
        interfejs.onGameEnd("Przegrałeś");
    }

    private void wykonajRuchBota() {
        if (graZakonczona) return;

        int[] ruch = bot.wybierzRuch(plansza, 19);

        if (ruch == null) {
            kolejnePasy++;
            if (sprawdzKoniecGry()) return;
        } else {
            int zbite = plansza.playMove(ruch[0], ruch[1], 2);
            punktyBialego += zbite;
            interfejs.onScoreUpdate("BIALY", zbite);
            kolejnePasy = 0;
        }

        interfejs.onBoardUpdate(konwertujPlansze());
        turaGracza = true;
        interfejs.onYourTurn();
    }

    private boolean sprawdzKoniecGry() {
        if (kolejnePasy >= 2) {
            graZakonczona = true;

            if (punktyCzarnego > punktyBialego) {
                interfejs.onGameEnd("Wygrałeś!");
            } else if (punktyBialego > punktyCzarnego) {
                interfejs.onGameEnd("Przegrałeś");
            } else {
                interfejs.onGameEnd("Remis");
            }
            return true;
        }
        return false;
    }

    private char[][] konwertujPlansze() {
        char[][] wynik = new char[19][19];
        List<String> linie = plansza.getBoardLines();

        for (int w = 0; w < 19; w++) {
            String linia = linie.get(w + 1);
            for (int k = 0; k < 19; k++) {
                wynik[w][k] = linia.charAt(3 + k * 2);
            }
        }
        return wynik;
    }
}


