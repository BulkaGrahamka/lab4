package org.example.gui;

public interface GameStateListener {
    
    void onBoardUpdate(char[][] plansza);
    void onYourTurn();
    void onGameEnd(String komunikat);
}




