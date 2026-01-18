package org.example.server;

import org.example.board.Board;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Board board;
    private final int mojKolor;
    private ClientHandler przeciwnik;
    private boolean poprzedniPas = false;
    private static int aktualnyGracz = 1;
    private static int kolejnePasy = 0;



    public ClientHandler(Socket socket, Board board, int mojKolor) throws IOException {
        this.socket = socket;
        this.board = board;
        this.mojKolor = mojKolor;
        this.in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void wyslij(String wiadomosc) {
        out.println(wiadomosc);
    }
    public void ustawprzeciwnika(ClientHandler przeciwnik) {
        this.przeciwnik = przeciwnik;
    }

    @Override
    public void run() {
        try {
            String linia;
            while ((linia = in.readLine()) != null) {
                if (linia.equals("PASS")) {

                    if (mojKolor != aktualnyGracz) {
                        wyslij("NIE_TWOJA_TURA");
                        continue;
                    }

                    kolejnePasy++;

                    if (kolejnePasy == 2) {
                        wyslij("KONIEC_GRY");
                        if (przeciwnik != null) {
                            przeciwnik.wyslij("KONIEC_GRY");
                        }
                        break;
                    }

                    aktualnyGracz = (aktualnyGracz == 1) ? 2 : 1;

                    if (przeciwnik != null) {
                        przeciwnik.wyslij("PRZECIWNIK_PAS");
                        przeciwnik.wyslij("TWOJ_RUCH");
                    }

                    continue;
                }


                if (linia.equals("RESIGN")) {
                    wyslij("PRZEGRALES");
                    if (przeciwnik != null) {
                        przeciwnik.wyslij("WYGRANA");
                    }
                    break;
                }
                System.out.println("[OD KLIENTA " + socket.getInetAddress() + "] " + linia);

                String[] parts = linia.trim().split("\\s+");
                if (parts.length != 2) {
                    wyslij("błąd zły format ruchu (podaj: wiersz kolumna)");
                    wyslij("TWOJ_RUCH");
                    continue;
                }

                int row;
                int col;
                try {
                    row = Integer.parseInt(parts[0]);
                    col = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    wyslij("błąd wspołrzędne muszą być liczbami");
                    wyslij("TWOJ_RUCH");
                    continue;
                }
                int wynik = board.playMove(row, col, mojKolor);

                if (wynik >= 0){
                    kolejnePasy = 0;
                    String wiadomosc;
                    if (this.mojKolor == 1) {
                        wiadomosc = "PUNKTY CZARNY " + wynik;
                    }
                    else {
                        wiadomosc = "PUNKTY BIALY " + wynik;
                    }

                    this.wyslij(wiadomosc);
                    if (przeciwnik != null) {
                        przeciwnik.wyslij(wiadomosc);
                    }

                    aktualnyGracz = (aktualnyGracz == 1) ? 2 : 1;
                    wyslij("PLANSZA");
                    for (String l : board.getBoardLines()) {
                        wyslij(l);
                    }

                    if (przeciwnik != null) {
                        przeciwnik.wyslij("PLANSZA");
                        for (String l : board.getBoardLines()) {
                            przeciwnik.wyslij(l);
                        }

                        przeciwnik.wyslij("TWOJ_RUCH");
                    }

                } else {
                    wyslij("błąd niepoprawny ruch");
                    wyslij("TWOJ_RUCH");
                }
            }
        } catch (IOException e) {
            System.out.println("Klient rozłączony: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) { }
        }
    }
}

