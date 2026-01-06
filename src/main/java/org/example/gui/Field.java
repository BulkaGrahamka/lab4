package org.example.gui;

import javafx.scene.layout.StackPane;

public class Field extends StackPane {

    public Field() {
        setPrefSize(30, 30);
        setStyle(
            "-fx-border-color: black;" +
            "-fx-background-color: pink;"
        );
    }
}

