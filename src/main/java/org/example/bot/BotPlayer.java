package org.example.bot;

import org.example.board.Board;
import java.util.*;

public class BotPlayer {

    private static final int ROZMIAR = 19;
    private final Random los = new Random();
    private static final int PROG_PASU = 5;


    public int[] wybierzRuch(Board plansza, int rozmiar) {

        List<Ruch> ruchy = new ArrayList<>();

        for (int w = 0; w < rozmiar; w++) {
            for (int k = 0; k < rozmiar; k++) {

                Board kopia = skopiuj(plansza, rozmiar);
                int zbite = kopia.playMove(w, k, 2);
                if (zbite < 0) continue;

                int punkty = 0;

                punkty += zbite * 300;

                if (maSasiada(plansza, w, k, 1)) {
                    punkty += 40;
                }

                if (wystawiaWAtari(plansza, w, k)) {
                    punkty += 120;
                }

                int wielkosc = wielkoscWlasnejGrupy(plansza, w, k);
                punkty -= wielkosc * 4;

                int srodek = rozmiar / 2;
                punkty += Math.max(0, 12 - dystans(w, k, srodek));

                if (w == 0 || k == 0 || w == rozmiar - 1 || k == rozmiar - 1) {
                    punkty -= 8;
                }

                ruchy.add(new Ruch(w, k, punkty));
            }
        }

        if (ruchy.isEmpty()) return null;

        ruchy.sort((a, b) -> Integer.compare(b.punkty, a.punkty));

        int najlepszy = ruchy.get(0).punkty;
        if (najlepszy <= PROG_PASU) {
            return null;
        }
        List<Ruch> najlepsze = new ArrayList<>();

        for (Ruch r : ruchy) {
            if (r.punkty == najlepszy) {
                najlepsze.add(r);
            }
        }

        Ruch wybrany = najlepsze.get(los.nextInt(najlepsze.size()));
        return new int[]{wybrany.wiersz, wybrany.kolumna};
    }

    private static class Ruch {
        int wiersz;
        int kolumna;
        int punkty;
        Ruch(int w, int k, int p) {
            wiersz = w;
            kolumna = k;
            punkty = p;
        }
    }

    private Board skopiuj(Board oryginal, int rozmiar) {
        Board nowa = new Board(rozmiar);
        var linie = oryginal.getBoardLines();
        for (int w = 0; w < rozmiar; w++) {
            String linia = linie.get(w + 1);
            for (int k = 0; k < rozmiar; k++) {
                char c = linia.charAt(3 + k * 2);
                if (c == 'C') nowa.playMove(w, k, 1);
                if (c == 'B') nowa.playMove(w, k, 2);
            }
        }
        return nowa;
    }

    private boolean maSasiada(Board plansza, int w, int k, int kolor) {
        int[][] d = {{1,0},{-1,0},{0,1},{0,-1}};
        var linie = plansza.getBoardLines();
        for (int[] dir : d) {
            int nw = w + dir[0];
            int nk = k + dir[1];
            if (nw >= 0 && nk >= 0 && nw < ROZMIAR && nk < ROZMIAR) {
                char c = linie.get(nw + 1).charAt(3 + nk * 2);
                if (kolor == 1 && c == 'C') return true;
                if (kolor == 2 && c == 'B') return true;
            }
        }
        return false;
    }

    private boolean wystawiaWAtari(Board plansza, int w, int k) {
        return maSasiada(plansza, w, k, 1);
    }

    private int wielkoscWlasnejGrupy(Board plansza, int w, int k) {
        int licznik = 0;
        int[][] d = {{1,0},{-1,0},{0,1},{0,-1}};
        var linie = plansza.getBoardLines();
        for (int[] dir : d) {
            int nw = w + dir[0];
            int nk = k + dir[1];
            if (nw >= 0 && nk >= 0 && nw < ROZMIAR && nk < ROZMIAR) {
                char c = linie.get(nw + 1).charAt(3 + nk * 2);
                if (c == 'B') licznik++;
            }
        }
        return licznik;
    }

    private int dystans(int w, int k, int s) {
        return Math.abs(w - s) + Math.abs(k - s);
    }
}





