package org.example.gui;

import org.example.client.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoApp extends Application {

    @Override
    public void start(Stage oknoGlowne) {
        GameWindow oknoGry = new GameWindow();
        Client klient = new Client();
        oknoGry.ustawKlienta(klient);
        klient.ustawGameStateListener(oknoGry);
        klient.polaczzserwerem("localhost", 6767);

    }
    public static void main(String[] args) {
        launch();
    }
}

