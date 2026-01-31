package org.example.gui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StartWindow {

    private final VBox root;
    private Runnable onPlayHuman;

    public StartWindow() {
        Label title = new Label("Gra Go");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");

        Button playHumanBtn = new Button("Graj z graczem");
        Button playBotBtn = new Button("Graj z botem");
        Button replayBtn = new Button("Odtwórz poprzednie gry");

        playHumanBtn.setPrefWidth(220);
        playBotBtn.setPrefWidth(220);
        replayBtn.setPrefWidth(220);

        playHumanBtn.setOnAction(e -> {
            if (onPlayHuman != null) {
                onPlayHuman.run();
            }
        });

        playBotBtn.setOnAction(e -> {
        });

        replayBtn.setOnAction(e -> {
        });

        root = new VBox(15,
                title,
                playHumanBtn,
                playBotBtn,
                replayBtn
        );
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");
    }

    public void setOnPlayHuman(Runnable onPlayHuman) {
        this.onPlayHuman = onPlayHuman;
    }

    public Parent getRoot() {
        return root;
    }
}

