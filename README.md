--- INSTRUKCJA URUCHOMIENIA PROJEKTU ---

    0. wchodzimy w folder z pom.xml
    1. javac -d target/classes src/main/java/org/example/server/Server.java src/main/java/org/example/server/ClientHandler.java src/main/java/org/example/board/Board.java (kompilacja serwera)
    2. java -cp target/classes org.example.server.Server (uruchomienie serwera)
    3. mvn clean javafx:run (uruchomienie klientów) - pierwszy gracz jest czarny, a drugi biały

--- SCHEMAT PROJEKTU ---

    | board
    |--- Board.java
    | client
    |--- Client.java
    | gui
    |--- BoardView.java
    |--- Field.java
    |--- GameStateListener.java
    |--- GameWindow.java
    |--- GoApp
    |--- ScoreBoard
    |--- StoneType
    | server
    |--- Clienthadler.java
    |--- Server.java

--- ZASADY GRASP ---

    Information Expert - cała logika jest w klasie Board
    Controller - Clienthandler
    Pure Fabrication - Clienthandler
    Indirection - Client -> ClientHandler -> Board
