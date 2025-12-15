--- INSTRUKCJA URUCHOMIENIA PROJEKTU ---

    1. uruchomienie serwera (java Server.java)
    2. uruchomienie obu klientow (java Client.java) - pierwszy gracz jest czarny, drugi biały

--- SCHEMAT PROJEKTU ---

    | board
    |--- Board.java
    | client
    |--- Client.java
    | server
    |--- Clienthadler.java
    |--- Server.java

--- ZASADY GRASP ---

    Information Expert - cała logika jest w klasie Board
    Controller - Clienthandler
    Pure Fabrication - Clienthandler
    Indirection - Client -> ClientHandler -> Board
