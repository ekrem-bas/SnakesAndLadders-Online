# Snakes and Ladders Game (Client-Server)

This project is a multiplayer, client-server version of the classic Snakes and Ladders game, developed in Java. Players connect to a server, are assigned to a lobby, and can play against each other in real-time.

## ✨ Key Features

* **Multiplayer:** Two players can join a lobby and play simultaneously.
* **Client-Server Architecture:** Game logic and state are managed by the server, while players interact through client interfaces.
* **Graphical User Interface (GUI):** A user-friendly game board and interactive elements are provided on the client-side using Java Swing.
* **Lobby System:** When players connect to the server, they are automatically assigned to a lobby and wait for an opponent.
* **Real-Time Updates:** Player movements, dice rolls, and game status are instantly reflected lágrimas to all clients.
* **Snakes and Ladders:** True to the classic game rules, snakes move players down, and ladders move them up.
* **Endgame Management:** The game ends when a player reaches the 100th square or if an opponent disconnects, and the result is announced.
* **"How to Play?" Screen:** A help screen with game rules is available for players.

## 🛠️ Technologies Used

* **Programming Language:** Java (JDK 21)
* **GUI:** Java Swing
* **Build Tool:** Apache Maven
* **Network:** Java Sockets (TCP/IP)
* **Client GUI Layout:** NetBeans AbsoluteLayout

## 📁 Project Structure

The project primarily consists of two main modules:

1.  **`SnakesAndLadders-Server`**:
    * `Server/`: Contains the logic for server startup (`Server.java`), accepting client connections, and creating a separate thread (`SClient.java`) for each client.
    * `Game/`:
        * `Game.java`: Contains the game board, snakes/ladders, and core game mechanics (position updating, determining the winner, etc.).
        * `GameLobby.java`: Manages game rooms that bring two players together, starts games, tracks player turns, and processes in-game messages.
        * `LobbyManager.java`: Designed with the Singleton pattern, it assigns clients to appropriate lobbies, creates new lobbies, and handles overall lobby management.

2.  **`SnakesAndLadders-Client`**:
    * `Client/`:
        * `SnakesAndLaddersMain.java`: The main entry screen where the player enters their name and logs into the game.
        * `ClientGUI.java`: Manages the main game interface (board, dice, player information, etc.) and visualizes updates from the server.
        * `CClient.java`: Establishes a connection with the server, sends/receives messages, and interacts with `ClientGUI`.
    * `Game/`:
        * `Game.java`: Used on the client-side to visualize player positions on the board.
        * `Dice.java`: Contains logic for rolling dice and loading dice images.
        * `HowToPlay.java`: Contains the interface that displays the game rules.

3.  **`SnakesAndLadders` (Main Directory)**:
    * This directory contains both client and server packages. The `pom.xml` file specifies the main class for the server.


## 🚀 Setup and Running

### Prerequisites

* Java Development Kit (JDK) 21 or higher.
* Apache Maven.

### Running the Server

1.  Open a terminal in the project root directory.
2.  Navigate to the `SnakesAndLadders-Server` directory:
    ```bash
    cd SnakesAndLadders-Server
    ```
3.  Compile and package the project with Maven:
    ```bash
    mvn clean package
    ```
4.  Run the generated JAR file (located in the `target` folder):
    ```bash
    java -jar target/SnakesAndLadders-Server-1.0-SNAPSHOT.jar
    ```
    The server will start on port `12345` by default.

### Running the Client

1.  Open a new terminal in the project root directory.
2.  Navigate to the `SnakesAndLadders-Client` directory:
    ```bash
    cd SnakesAndLadders-Client
    ```
3.  Compile and package the project with dependencies using Maven:
    ```bash
    mvn clean package
    ```
    This command will create a JAR file with all dependencies included, thanks to the `maven-assembly-plugin`.
4.  Run the generated JAR file (located in the `target` folder):
    ```bash
    java -jar target/SnakesAndLadders-Client-1.0-SNAPSHOT-jar-with-dependencies.jar
    ```
5.  Enter a player name in the opened window and click the "Login" button.
6.  Repeat steps 1-5 for a second player.

**Note:** The client attempts to connect to the server at the IP address (`13.48.27.159`) and port (`12345`) specified in the `SnakesAndLadders-Client/src/main/java/Client/ClientGUI.java` file. For local testing, you may need to change this address to `localhost` or enter the IP address where your server is running.

## 🎮 How to Play?

1.  After starting the client application, enter a player name.
2.  The server will automatically match you with an opponent or wait for an opponent to connect.
3.  When the game starts, if it's your turn, the "Roll Dice" button will become active.
4.  Roll your dice and advance on the board.
5.  If you land at the bottom of a ladder, you will climb up.
6.  If you land on the head of a snake, you will slide down.
7.  The first player to reach the final square (100) wins the game!
8.  If your opponent leaves the game, you automatically win.

For detailed rules, you can use the "Help" -> "How to Play?" menu in the client application.

## 💬 Communication Protocol (Client ↔ Server Interaction Flow)

This section explains the basic messaging between the client and server, and the server-side events that trigger this messaging. The client receives and processes messages from the server prefixed with `LOBBY:` or `WAIT:`.

### Client → Server

* `NAME:<playerName>`: When the client first connects to the server, it sends its player name and connection request. This message is received by the `SClient` on the server.
* `LOBBY:<lobbyId>:ROLL:<diceValue>`: When the player rolls the dice while in an active lobby (`GameLobby`), this message is sent to the server. The message is relayed via `SClient` to `LobbyManager`, and then to the respective `GameLobby`.

### Server-Side Events and Resulting Messages to Client

The following lists server-side events observed in server logs and the messages clients receive as a result of these events:

1.  **First Client Connects and Waits for an Opponent:**
    * Server Log Sequence:
        * `SClient <Player 1 Name> (ID: 1) listening for messages....`
        * `LobbyManager: Adding client <Player 1 Name> with id 1`
        * `LobbyManager: Client <Player 1 Name> (ID: 1) is waiting.`
    * **Client (PlayerName1) Receives:** `WAIT:Waiting for another player...`

2.  **Second Client Connects, Lobby is Created, and Players are Informed:**
    * Server Log Sequence:
        * `SClient <Player 2 Name> (ID: 2) listening for messages....`
        * `LobbyManager: Adding client <Player 2 Name> with id 2`
        * `GameLobby 1 created.`
        * `GameLobby 1: Client <Player 1 Name> (Player 1) added. Total clients: 1`
        * `GameLobby 1: Client <Player 2 Name> (Player 2) added. Total clients: 2`
        * `LobbyManager: Lobby 1 created for clients 1 (P1) and 2 (P2)`
    * **Client (<Player 1 Name>) Receives:** `LOBBY:1:LOBBY_JOINED:1`
    * **Client (<Player 2 Name>) Receives:** `LOBBY:1:LOBBY_JOINED:2`

3.  **Game Starts and Initial Turn Information is Sent:**
    * Server Log Sequence:
        * `GameLobby 1: Game is starting. It's <Player 1 Name>'s turn (Player 1, Client ID: 1).`
        * `GameLobby 1 broadcasting: "LOBBY:1:GAME_START:[1, 1]" to 2 clients.`
        * `GameLobby 1 broadcasting: "LOBBY:1:TURN_UPDATE:1:<Player 1 Name>" to 2 clients.`
        * `GameLobby 1: Turn updated. It's <Player 1 Name>'s turn (Player 1, Client ID: 1).`
    * **Both Clients Receive:** `LOBBY:1:GAME_START:[1, 1]`
    * **Both Clients Receive:** `LOBBY:1:TURN_UPDATE:1:<Player 1 Name>`

4.  **Other In-Game Messages (Managed and Broadcast by GameLobby):**

    * `LOBBY:<lobbyId>:POSITION_UPDATE:<p1Pos,p2Pos>`: After a player moves, `GameLobby` broadcasts the new positions of both players on the board to all clients.
    * `LOBBY:<lobbyId>:DICE_ROLLED:<diceValue>`: When a player rolls the dice, `GameLobby` broadcasts the value of the dice roll to all clients.
    * `LOBBY:<lobbyId>:MY_MOVE_UPDATE:<displayOldPos:displayNewPos>`: `GameLobby` specifically informs the client who made the move about the "to be displayed" old and new positions after snake/ladder interactions. This is used to clarify the move outcome on the client's interface.
    * `LOBBY:<lobbyId>:GAME_RESULT:WIN:<loserName>`: When `GameLobby` determines a winner, it sends this message to the winning client (with the loser's name).
    * `LOBBY:<lobbyId>:GAME_RESULT:LOSS:<winnerName>`: When `GameLobby` determines a winner, it sends this message to the losing client (with the winner's name).
    * `LOBBY:<lobbyId>:GAME_RESULT:WIN_DISCONNECT:<disconnectedPlayerName>`: When a player leaves the game, `GameLobby` sends this message to the remaining player, informing them they have won.
    * `LOBBY:<lobbyId>:ERROR:<errorMessage>`: If `GameLobby` detects an invalid move (e.g., rolling dice out of turn), it sends an error message to the relevant client.
