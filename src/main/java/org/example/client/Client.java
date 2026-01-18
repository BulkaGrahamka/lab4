package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.example.gui.GameStateListener;

/**
 * Odpowiada za całą logikę sieciową po stronie klienta.
 * Nawiązuje połączenie z serwerem, wysyła ruchy gracza
 * oraz nasłuchuje na wiadomości od serwera w osobnym wątku.
 * Działa jako "kurier" - odbiera komunikaty i przekazuje je do słuchacza
 * (np. obiektu GUI), który wie, jak na nie zareagować.
 */
public class Client {
    private GameStateListener sluchacz;
    private Socket socket;
    private PrintWriter out;
    private volatile boolean mojatura = false;

    /**
     * Rejestruje obiekt słuchacza, który będzie informowany o zmianach stanu gry.
     *
     * @param sluchacz Obiekt (zazwyczaj komponent GUI) implementujący interfejs GameStateListener.
     */
    public void ustawGameStateListener(GameStateListener sluchacz) {
        this.sluchacz = sluchacz;
    }

    /**
     * Łączy się z serwerem gry na podanym hoście i porcie.
     * Po nawiązaniu połączenia, uruchamia w tle nowy wątek, który
     * nasłuchuje na przychodzące wiadomości od serwera.
     *
     * @param host Adres IP lub nazwa hosta serwera.
     * @param port Port, na którym nasłuchuje serwer.
     */
    public void polaczzserwerem(String host, int port) {
        try {
            System.out.println("probójemy połączyc się z serwerem " + host + " : " + port);
            socket = new Socket(host, port);
            System.out.println("połączono z serwerem :)");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            out = new PrintWriter(socket.getOutputStream(), true);
            Thread odbior = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        // Sprawdzaj wszystkie wiadomości, nawet jeśli są puste
                        final String finalMsg = msg.trim();

                        if (finalMsg.equals("TWOJ_RUCH")) {
                            mojatura = true;
                            System.out.println("Twój ruch:");
                            if (sluchacz != null) {
                                sluchacz.onYourTurn();
                            }
                        } else if (finalMsg.equals("SERVER_PELNY")) {
                            System.out.println("Serwer jest pelny :( Rozłączono");
                            socket.close();
                            break;
                        } else if (finalMsg.equals("PLANSZA")) {
                            // Ta logika odczytu planszy jest podatna na błędy,
                            // serwer powinien wysyłać rozmiar planszy lub zakończyć wiadomość specjalnym tokenem.
                            // Zakładamy stały rozmiar 19x19.
                            char[][] plansza = new char[19][19];
                            in.readLine(); // Pomiń linię z numerami kolumn
                            for (int wiersz = 0; wiersz < 19; wiersz++) {
                                String linia = in.readLine();
                                for (int kol = 0; kol < 19; kol++) {
                                    char c = linia.charAt(3 + kol * 2);
                                    plansza[wiersz][kol] = c;
                                }
                            }
                            if (sluchacz != null) {
                                sluchacz.onBoardUpdate(plansza);
                            }
                        } else if (finalMsg.equals("WYGRANA")) {
                            if (sluchacz != null) {
                                sluchacz.onGameEnd("Wygrałeś!");
                            }
                        } else if (finalMsg.equals("PRZEGRALES")) {
                            if (sluchacz != null) {
                                sluchacz.onGameEnd("Przegrałeś");
                            }
                        } else if (finalMsg.startsWith("PUNKTY")) {
                            String[] czesci = finalMsg.split(" ");
                            String kolor = czesci[1];
                            int punkty = Integer.parseInt(czesci[2]);
                            if (sluchacz != null) {
                                sluchacz.onScoreUpdate(kolor, punkty);
                            }
                        }
                        else if (msg.equals("REMIS")) {
                            if (sluchacz!= null) {
                                sluchacz.onGameEnd("Remis");
                            }
                        }

                    }
                } catch (IOException e) {
                    System.out.println("rozłączono z serwerem");
                }
            });
            odbior.start();
        } catch (IOException e) {
            System.out.println("błąd połączenia: " + e.getMessage());
        }
    }

    /**
     * Wysyła do serwera ruch gracza w postaci współrzędnych.
     *
     * @param wiersz  Wiersz, w którym postawiono kamień.
     * @param kolumna Kolumna, w której postawiono kamień.
     */
    public void wyslijRuch(int wiersz, int kolumna) {
        if (socket == null || socket.isClosed()) {
            System.out.println("błąd połączenia z serwerem – ruch niewysłany");
            return;
        }
        out.println(wiersz + " " + kolumna);
    }

    /**
     * Sprawdza, czy aktualnie jest tura tego gracza.
     *
     * @return {@code true} jeśli jest tura gracza, w przeciwnym razie {@code false}.
     */
    public boolean czyMojaTura() {
        return mojatura;
    }

    /**
     * Ustawia stan tury gracza. Używane przez GUI do blokowania/odblokowywania planszy.
     *
     * @param wartosc Nowa wartość stanu tury.
     */
    public void ustawMojaTura(boolean wartosc) {
        mojatura = wartosc;
    }

    /**
     * Wysyła do serwera komunikat o spasowaniu tury.
     */
    public void pas() {
        if (out == null) {
            System.out.println("Brak połączenia – nie można spasować");
            return;
        }
        out.println("PASS");
    }

    /**
     * Wysyła do serwera komunikat o poddaniu gry.
     */
    public void poddajSie() {
        if (out == null) {
            System.out.println("Brak połączenia – nie można się poddać");
            return;
        }
        out.println("RESIGN");
    }
}
