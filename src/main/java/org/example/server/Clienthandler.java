package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private ClientHandler przeciwnik;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
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
                System.out.println("[OD KLIENTA " + socket.getInetAddress() + "] " + linia);

                if(przeciwnik != null){
                    przeciwnik.wyslij("RUCH PRZECIWNIKA " + linia);
                    przeciwnik.wyslij("TWOJ_RUCH");
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

