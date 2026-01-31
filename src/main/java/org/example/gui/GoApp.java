package org.example.gui;

import org.example.client.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Główna klasa aplikacji klienta, odpowiedzialna za uruchomienie
 * interfejsu graficznego JavaFX i zainicjowanie połączenia z serwerem.
 */
public class GoApp extends Application {

    /**
     * Metoda startowa dla aplikacji JavaFX. Tworzy główne okno gry,
     * inicjuje klienta sieciowego w osobnym wątku i łączy logikę
     * sieciową z interfejsem graficznym.
     *
     * @param stage Główna scena (okno) aplikacji.
     */
    @Override
    public void start(Stage stage) {
        StartWindow startWindow = new StartWindow();
        Scene scene = new Scene(startWindow.getRoot(), 900, 800);

        stage.setTitle("Gra Go");
        stage.setScene(scene);
        stage.show();

        startWindow.setOnPlay(() -> {
            GameWindow gameWindow = new GameWindow();
            scene.setRoot(gameWindow.getKorzen());

            new Thread(() -> {
                Client klient = new Client();
                klient.ustawGameStateListener(gameWindow);
                gameWindow.ustawKlienta(klient);
                klient.polaczzserwerem("localhost", 6767);
            }).start();
        });
    }


}