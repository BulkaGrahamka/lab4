package org.example.gui;

/**
 * Interfejs definiujący zestaw metod, które obiekt GUI musi zaimplementować,
 * aby móc reagować na komunikaty o stanie gry przychodzące z serwera.
 * Działa jako kontrakt między warstwą sieciową ({@link org.example.client.Client})
 * a warstwą prezentacji (np. {@link GameWindow}).
 */
public interface GameStateListener {
    /** Wywoływane, gdy serwer prześle zaktualizowany stan planszy. */
    void onBoardUpdate(char[][] plansza);
    /** Wywoływane, gdy serwer poinformuje, że teraz jest tura tego klienta. */
    void onYourTurn();
    /** Wywoływane, gdy gra się zakończy. */
    void onGameEnd(String komunikat);
    /** Wywoływane, gdy jeden z graczy zdobędzie punkty. */
    void onScoreUpdate(String kolor, int punkty);
}