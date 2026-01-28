package org.example.server;

public interface Player {

    void wyslij(String wiadomosc);
    void wykonajRuch();
    int getKolor();

    void ustawprzeciwnika(Player przeciwnik);
}

