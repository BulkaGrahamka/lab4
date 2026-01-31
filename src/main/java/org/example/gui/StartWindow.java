package org.example.gui;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class StartWindow {

    private final VBox root;
    private Runnable onPlay;

    public StartWindow() {
        Label title = new Label("Gra Go");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Button playButton = new Button("Graj z graczem");
        playButton.setPrefWidth(200);

        playButton.setOnAction(e -> {
            if (onPlay != null) {
                onPlay.run();
            }
        });

        root = new VBox(20, title, playButton);
        root.setAlignment(Pos.CENTER);
    }

    public void setOnPlay(Runnable onPlay) {
        this.onPlay = onPlay;
    }

    public Parent getRoot() {
        return root;
    }
}

