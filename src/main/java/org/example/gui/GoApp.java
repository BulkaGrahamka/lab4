package org.example.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoApp extends Application {

    @Override
    public void start(Stage oknoGlowne) {
        GameWindow oknoGry = new GameWindow();
        Scene scena = new Scene(oknoGry.getKorzen(), 700, 700);
        oknoGlowne.setTitle("gra Go – iteracja 2");
        oknoGlowne.setScene(scena);
        oknoGlowne.show();
    }
    public static void main(String[] args) {
        launch();
    }
}

