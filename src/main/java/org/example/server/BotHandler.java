package org.example.server;

import org.example.board.Board;

public class BotHandler implements Player {

    private final Board board;
    private final int mojKolor;
    private Player przeciwnik;

    public BotHandler(Board board, int mojKolor) {
        this.board = board;
        this.mojKolor = mojKolor;
    }
    @Override
    public void ustawPrzeciwnika(Player przeciwnik) {
        this.przeciwnik = przeciwnik;
    }
    @Override
    public int getKolor() {
        return mojKolor;
    }

    @Override
    public void wyslij(String wiadomosc) {
    }
    @Override
    public void wykonajRuch() {
        for (int r = 0; r < 19; r++) {
            for (int c = 0; c < 19; c++) {
                int wynik = board.playMove(r, c, mojKolor);
                if (wynik >= 0) {
                    przeciwnik.wyslij("PLANSZA");
                    for (String l : board.getBoardLines()) {
                        przeciwnik.wyslij(l);
                    }
                    przeciwnik.wyslij("TWOJ_RUCH");
                    return;
                }
            }
        }
        przeciwnik.wyslij("PRZECIWNIK_PAS");
        przeciwnik.wyslij("TWOJ_RUCH");
    }
}
