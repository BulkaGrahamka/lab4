package org.example.board;

import java.util.*;

/**
 * Reprezentuje i zarządza stanem planszy do gry w Go.
 * Klasa jest odpowiedzialna za logikę umieszczania kamieni,
 * sprawdzanie poprawności ruchów, zliczanie zbitych kamieni (punktów)
 * oraz weryfikację zasad, takich jak zakaz ruchów samobójczych.
 * Metody modyfikujące stan planszy są synchronizowane, aby zapewnić bezpieczeństwo
 * w środowisku wielowątkowym (na serwerze).
 */
public class Board {
    private final int size;
    private final int[][] grid;
    private final int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /**
     * Tworzy nową, pustą planszę o zadanym rozmiarze.
     *
     * @param size Rozmiar boku planszy (np. 19 dla planszy 19x19).
     */
    public Board(int size) {
        this.size = size;
        grid = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], 0);
        }
    }

    /**
     * Wykonuje ruch gracza na podanych współrzędnych.
     * Metoda jest synchronizowana, aby zapobiec jednoczesnej modyfikacji planszy przez dwóch graczy.
     * Sprawdza, czy ruch jest dozwolony, umieszcza kamień, oblicza liczbę zbitych kamieni
     * przeciwnika i weryfikuje, czy ruch nie jest samobójstwem.
     *
     * @param row    Wiersz, w którym gracz chce umieścić kamień.
     * @param col    Kolumna, w której gracz chce umieścić kamień.
     * @param player Identyfikator gracza (1 dla czarnego, 2 dla białego).
     * @return Liczba zbitych kamieni przeciwnika (>= 0) w przypadku poprawnego ruchu.
     *         -1, jeśli pole jest zajęte lub poza planszą.
     *         -2, jeśli ruch jest ruchem samobójczym.
     */
    public synchronized int playMove(int row, int col, int player) {
        if (!inBounds(row, col) || grid[row][col] != 0) return -1;

        int enemy = (player == 1) ? 2 : 1;
        grid[row][col] = player;
        int captured = 0;
        // Sprawdzenie i zbicie grup przeciwnika
        for (int[] dir : dirs) {
            int nRow = row + dir[0];
            int nCol = col + dir[1];
            if (!inBounds(nRow, nCol)) continue;
            if (grid[nRow][nCol] == enemy) {
                if (!hasLiberties(nRow, nCol, grid)) {
                    captured += removeGroup(nRow, nCol, enemy);
                }
            }
        }

        // Weryfikacja ruchu samobójczego
        if (!hasLiberties(row, col, grid)) {
            if (captured == 0) { // Tylko jeśli ruch nie prowadzi do zbicia
                grid[row][col] = 0; // Wycofaj ruch
                return -2;
            }
        }
        return captured;
    }

    /**
     * Usuwa grupę połączonych kamieni danego gracza, zaczynając od podanego pola.
     * Używa algorytmu przeszukiwania w głąb (DFS) na stosie.
     *
     * @param row    Wiersz startowy grupy do usunięcia.
     * @param col    Kolumna startowa grupy do usunięcia.
     * @param player Kolor kamieni do usunięcia.
     * @return Liczba usuniętych kamieni.
     */
    private int removeGroup(int row, int col, int player) {
        if (!inBounds(row, col) || grid[row][col] != player) return 0;

        int removed = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{row, col});
        // Tymczasowe oznaczenie, aby uniknąć ponownego dodawania na stos
        grid[row][col] = -player;

        while (!stack.isEmpty()) {
            int[] pop = stack.pop();
            int pRow = pop[0];
            int pCol = pop[1];

            for (int[] dir : dirs) {
                int nRow = pRow + dir[0];
                int nCol = pCol + dir[1];
                if (inBounds(nRow, nCol) && grid[nRow][nCol] == player) {
                    stack.push(new int[]{nRow, nCol});
                    grid[nRow][nCol] = -player;
                }
            }
        }

        // Finalne usunięcie kamieni (zmiana na 0) - pętla jest nieefektywna, ale działa
        // Można by to zoptymalizować, zmieniając na 0 w pętli while.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == -player) {
                    grid[i][j] = 0;
                    removed++; // Zlicz usunięty kamień
                }
            }
        }

        return removed;
    }

    /**
     * Sprawdza, czy grupa kamieni, do której należy podane pole, ma jakiekolwiek "oddechy" (puste sąsiednie pola).
     * Używa algorytmu przeszukiwania w głąb (DFS), aby znaleźć połączone kamienie i sprawdzić ich sąsiadów.
     *
     * @param row       Wiersz, od którego zaczyna się sprawdzanie.
     * @param col       Kolumna, od której zaczyna się sprawdzanie.
     * @param boardCopy Kopia planszy do analizy (może być tożsama z główną planszą).
     * @return {@code true}, jeśli grupa ma co najmniej jeden oddech, w przeciwnym razie {@code false}.
     */
    private boolean hasLiberties(int row, int col, int[][] boardCopy) {
        if (!inBounds(row, col)) return false;
        int color = boardCopy[row][col];
        if (color == 0) return true; // Pojedyncze puste pole ma oddechy

        boolean[][] visited = new boolean[size][size];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{row, col});
        visited[row][col] = true;

        while (!stack.isEmpty()) {
            int[] pop = stack.pop();
            int pRow = pop[0];
            int pCol = pop[1];
            for (int[] dir : dirs) {
                int nRow = pRow + dir[0];
                int nCol = pCol + dir[1];
                if (!inBounds(nRow, nCol)) continue;
                // Znaleziono oddech!
                if (boardCopy[nRow][nCol] == 0) return true;
                // Kontynuuj przeszukiwanie w obrębie tej samej grupy kolorów
                if (!visited[nRow][nCol] && boardCopy[nRow][nCol] == color) {
                    visited[nRow][nCol] = true;
                    stack.push(new int[]{nRow, nCol});
                }
            }
        }
        // Nie znaleziono żadnego oddechu dla całej grupy
        return false;
    }

    /**
     * Sprawdza, czy podane współrzędne mieszczą się w granicach planszy.
     *
     * @param row Wiersz do sprawdzenia.
     * @param col Kolumna do sprawdzenia.
     * @return {@code true}, jeśli współrzędne są poprawne, w przeciwnym razie {@code false}.
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < size && col < size;
    }

    /**
     * Generuje tekstową reprezentację aktualnego stanu planszy.
     * Przydatne do wysyłania stanu planszy do klientów w formie tekstowej.
     * Metoda jest synchronizowana, aby zapewnić spójny odczyt stanu planszy.
     *
     * @return Lista stringów, gdzie każdy string to jeden wiersz planszy.
     */
    public synchronized List<String> getBoardLines() {
        List<String> lines = new ArrayList<>();
        StringBuilder line0 = new StringBuilder("  ");
        for (int col = 0; col < size; col++) {
            line0.append(String.format("%2d", col));
        }
        lines.add(line0.toString());
        for (int row = 0; row < size; row++) {
            StringBuilder line = new StringBuilder(String.format("%2d", row));
            for (int col = 0; col < size; col++) {
                char ch = '.';
                if (grid[row][col] == 1) ch = 'C'; // Czarny
                if (grid[row][col] == 2) ch = 'B'; // Biały
                line.append(" ").append(ch);
            }
            lines.add(line.toString());
        }
        return lines;
    }
}
