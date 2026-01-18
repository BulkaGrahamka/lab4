package org.example.gui;

import org.example.client.Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GoApp extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("START GUI");

        GameWindow okno = new GameWindow();
        Scene scena = new Scene(okno.getKorzen(), 900, 800);

        stage.setTitle("Gra Go");
        stage.setScene(scena);
        stage.show();

        new Thread(() -> {
            Client klient = new Client();
            klient.ustawGameStateListener(okno);
            okno.ustawKlienta(klient);
            klient.polaczzserwerem("localhost", 6767);
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
