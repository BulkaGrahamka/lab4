package org.example.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.example.gui.GameStateListener;

public class Client{
    private GameStateListener sluchacz;
    private Socket socket;
    private PrintWriter out;
    private volatile boolean mojatura=false;

    public static void main(String[] args){
        Client klient = new Client();
        klient.polaczzserwerem("localhost", 6767);

    }
    public void ustawGameStateListener(GameStateListener sluchacz) {
        this.sluchacz = sluchacz;
    }

    public void polaczzserwerem(String host, int port){
        try{
            System.out.println("probójemy połączyc się z serwerem " + host + " : " + port);
            socket=new Socket(host, port);
            System.out.println("połączono z serwerem :)");
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            out = new PrintWriter(socket.getOutputStream(), true);
            Thread odbior = new Thread(() -> {
                try{
                    String msg;

                    while ((msg = in.readLine()) != null) {
                        if (msg.equals("TWOJ_RUCH")) {
                            mojatura = true;
                            System.out.println("Twój ruch:");
                            if (sluchacz != null) {
                                sluchacz.onYourTurn();
                            }

                        }
                        else if (msg.equals("SERVER_PELNY")) {
                            System.out.println("Serwer jest pelny :( Rozłączono");
                            socket.close();
                            break;
                        } else if(msg.equals("PLANSZA")){
                            char[][] plansza = new char[19][19];

                            in.readLine();
                            for (int wiersz = 0; wiersz< 19; wiersz++) {
                                String linia = in.readLine();
                                for (int kol=0; kol < 19; kol++) {
                                    char c = linia.charAt(3 + kol * 2);
                                    plansza[wiersz][kol] = c;
                                }
                            }

                            if (sluchacz!= null) {
                                sluchacz.onBoardUpdate(plansza);
                            }
                        }

                    }
                }catch (IOException e){
                    System.out.println("rozłączono z serwerem");

                }
            });
            odbior.start();

            BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in)
                );

            String linia;
            while ((linia = console.readLine()) != null) {
                if (mojatura) {
                    out.println(linia);
                    mojatura = false;
                } else {
                System.out.println("czekaj na swoją kolej...");
                }

            }

        } catch (IOException e){
            System.out.println("błąd połączenia: " + e.getMessage());
        }

    }
    public void wyslijRuch(int wiersz, int kolumna) {
        if (socket == null || socket.isClosed()) {
            System.out.println("błąd połączenia z serwerem – ruch niewysłany");
            return;
        }

        out.println(wiersz + " " + kolumna);
    }
    public boolean czyMojaTura() {
        return mojatura;
    }

    public void ustawMojaTura(boolean wartosc) {
        mojatura = wartosc;
    }


}


            

        
    
