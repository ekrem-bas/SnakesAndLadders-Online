package Game;

import Server.SClient;
import java.util.*;

public class GameLobby {

    private final int lobbyId; // lobby id
    private final List<SClient> clients = new ArrayList<>(); // clients in the lobby
    private final Game game; // game
    private int currentPlayerIndex = 0; // variable necessary for turn control
    private boolean gameActive = false; // activity status of the game

    public GameLobby(int lobbyId) {
        this.lobbyId = lobbyId;
        this.game = new Game();
        System.out.println("GameLobby " + lobbyId + " created.");
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public synchronized boolean isEmpty() {
        return clients.isEmpty();
    }

    public synchronized void addClient(SClient client) {
        if (clients.size() < 2 && !clients.contains(client)) {
            clients.add(client);
            client.setCurrentLobby(this);
            System.out.println("GameLobby " + lobbyId + ": Client " + client.getPlayerName() + " (Player " + client.getPlayerNumberInLobby() + ") added. Total clients: " + clients.size());
        } else {
            if (clients.contains(client)) {
                System.err.println("GameLobby " + lobbyId + ": Client " + client.getClientId() + " already in this lobby.");
            } else {
                System.err.println("GameLobby " + lobbyId + ": Cannot add client " + client.getClientId() + ". Lobby is full (Max 2).");
            }
        }
    }

    public synchronized void removeClient(SClient client) {
        int leavingPlayerNumber = client.getPlayerNumberInLobby();
        String leavingPlayerName = client.getPlayerName();

        boolean removed = clients.remove(client);

        if (removed) {
            System.out.println("GameLobby " + lobbyId + ": Client " + client.getClientId() + " (Player " + leavingPlayerNumber + ", " + leavingPlayerName + ") removed from lobby list.");
            client.setCurrentLobby(null);

            if (gameActive) { // while the game is ongoing

                gameActive = false;
                System.out.println("GameLobby " + lobbyId + ": Game was active. Player " + client.getPlayerNumberInLobby()
                        + " (" + client.getPlayerName() + ") left. Game ended.");

                if (!clients.isEmpty()) { // 'clients' list now only contains the other player
                    SClient winnerByDisconnect = clients.get(0); // The only one left is the winner
                    System.out.println("GameLobby " + lobbyId + ": Announcing " + winnerByDisconnect.getPlayerName()
                            + " as winner due to " + client.getPlayerName() + " disconnecting.");
                    endGame(winnerByDisconnect, client, true); // true for by disconnect
                } else {
                    // This means the game was active, a player disconnected, and now the lobby is empty.
                    // This could happen if the 'winnerByDisconnect' also simultaneously disconnected or was already gone.
                    System.out.println("GameLobby " + lobbyId + ": No players left after Player " + client.getPlayerNumberInLobby()
                            + " (" + client.getPlayerName() + ") disconnected. Game was active.");
                }
            } else {
                System.out.println("GameLobby " + lobbyId + ": Game was not active when Player " + leavingPlayerNumber + " was removed.");
            }
        }
    }

    public synchronized void startGame() {
        if (clients.size() == 2 && !gameActive) {
            this.gameActive = true;
            this.currentPlayerIndex = 0; // Player 1 (at index 0 of 'clients' list) starts
            
            SClient startingPlayer = clients.get(currentPlayerIndex);
            System.out.println("GameLobby " + lobbyId + ": Game is starting. It's " + startingPlayer.getPlayerName()
                + "'s turn (Player " + startingPlayer.getPlayerNumberInLobby() 
                + ", Client ID: " + startingPlayer.getClientId() + ").");
            broadcast("GAME_START:" + Arrays.toString(game.getPositions())); // e.g., "[1, 1]"
            updateTurnAndButtonStates();
        } else {
            if (gameActive) {
                System.err.println("GameLobby " + lobbyId + ": Game start called, but game is already active.");
            } else {
                System.err.println("GameLobby " + lobbyId + ": Cannot start game. Requires 2 players, currently has " + clients.size());
            }
        }
    }

    public synchronized void processMessage(SClient senderClient, String commandAndData) {
        System.out.println("GameLobby " + lobbyId + " processing from client " + senderClient.getClientId()
                + " (" + senderClient.getPlayerName() + " - " + senderClient.getPlayerNumberInLobby() + "): \"" + commandAndData + "\"");

        String[] parts = commandAndData.split(":", 2);
        String command = parts[0];
        String data = parts[1];

        if (command.equals("ROLL")) {
            if (clients.indexOf(senderClient) == currentPlayerIndex) {
                int diceValue = Integer.parseInt(data);
                processRoll(senderClient, diceValue);
            } else {
                System.out.println("GameLobby " + lobbyId + ": Client " + senderClient.getClientId() + " (" + senderClient.getPlayerName() + " - " + senderClient.getPlayerNumberInLobby()
                        + ") tried to roll out of turn.");
                senderClient.sendMessage("LOBBY:" + lobbyId + ":ERROR:It's not your turn.");
            }
        } else {
            System.out.println("GameLobby " + lobbyId + ": Unknown command received: \"" + command + "\" from Player " + senderClient.getPlayerNumberInLobby());
            senderClient.sendMessage("LOBBY:" + lobbyId + ":ERROR:Unknown command '" + command + "'.");
        }
    }

    private void updateTurnAndButtonStates() {
        SClient currentTurnClient = clients.get(currentPlayerIndex);

        String turnUpdateData = currentTurnClient.getPlayerNumberInLobby() + ":" + currentTurnClient.getPlayerName();
        broadcast("TURN_UPDATE:" + turnUpdateData);

        System.out.println("GameLobby " + lobbyId + ": Turn updated. It's " + currentTurnClient.getPlayerName() 
            + "'s turn (Player " + currentTurnClient.getPlayerNumberInLobby() 
            + ", Client ID: " + currentTurnClient.getClientId() + ").");
    }

    private synchronized void processRoll(SClient rollerClient, int diceValue) {
        int playerGameIndex = currentPlayerIndex;
        int rollerPlayerNumber = rollerClient.getPlayerNumberInLobby();

        System.out.println("GameLobby " + lobbyId + ": Player " + rollerPlayerNumber + " (Client ID: " + rollerClient.getClientId() + ") rolled " + diceValue + ".");

        // 1. Get the player's actual (original) position before rolling the dice
        int[] positionsBeforeAnyMove = game.getPositions().clone();
        int originalOldPos = positionsBeforeAnyMove[playerGameIndex];

        // 2. Calculate the raw position with the dice roll
        int newPosAfterDice = Math.min(originalOldPos + diceValue, 100);

        // 3. Temporarily update this raw position in the Game object (for before checkSnakeOrLadder)
        int[] tempPositions = positionsBeforeAnyMove.clone();
        tempPositions[playerGameIndex] = newPosAfterDice;
        game.updatePositions(tempPositions[0], tempPositions[1]); // Update the game with this raw position

        System.out.println("GameLobby " + lobbyId + ": Player " + rollerPlayerNumber + " moved from " + originalOldPos + " to " + newPosAfterDice + " (before S/L).");

        // 4. Check if there is a Snake or Ladder
        game.checkSnakeOrLadder(rollerPlayerNumber);

        // 5. Get the final positions after snake/ladder
        int[] finalPositionsAllPlayers = game.getPositions();
        int finalActualNewPos = finalPositionsAllPlayers[playerGameIndex]; // The final square where the player's move ends

        // 6. Determine the "Old Position" and "New Position" to be displayed in the GUI
        int displayOldPos;
        int displayNewPos = finalActualNewPos; // "New Position" is always the final position.

        if (finalActualNewPos != newPosAfterDice) { // If there is a snake or ladder
            // "Old Position" is where the snake/ladder starts.
            // newPosAfterDice is the square where the player landed on the snake/ladder.
            displayOldPos = newPosAfterDice;
        } else { // If there is no snake or ladder
            // "Old Position" is the original position before rolling the dice.
            displayOldPos = originalOldPos;
        }

        System.out.println("GameLobby " + lobbyId + ": For display: Player " + rollerPlayerNumber
                + " OldPos=" + displayOldPos + ", NewPos=" + displayNewPos);

        // 7. Send only the current player their own move information
        // Data: OldPositionToShow:NewPositionToShow
        String myMoveData = String.format("%d:%d",
                displayOldPos,
                displayNewPos
        );
        rollerClient.sendMessage("LOBBY:" + lobbyId + ":MY_MOVE_UPDATE:" + myMoveData);

        // Send the rolled dice information to all clients
        broadcast("DICE_ROLLED:" + diceValue);

        // 9. General position update to synchronize the final positions of all players on the board
        broadcast("POSITION_UPDATE:" + Arrays.toString(finalPositionsAllPlayers));

        // 10. End game check and turn change
        if (game.getWinner() != 0) {
            int winnerPlayerNumber = game.getWinner();
            SClient winnerClient = null;
            SClient loserClient = null;

            for (SClient c : clients) {
                if (c.getPlayerNumberInLobby() == winnerPlayerNumber) {
                    winnerClient = c;
                } else {
                    loserClient = c;
                }
            }

            System.out.println("GameLobby " + lobbyId + ": Game Over. Player " + winnerPlayerNumber
                    + " (" + winnerClient.getPlayerName() + ") wins. Loser: " + loserClient.getPlayerName());
            endGame(winnerClient, loserClient, false);
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
            updateTurnAndButtonStates();
        }
    }

    private synchronized void endGame(SClient winner, SClient loser, boolean byDisconnect) {
        gameActive = false;

        String winnerName = winner.getPlayerName();
        String loserName = loser.getPlayerName();

        System.out.println("GameLobby " + lobbyId + ": Ending game. Winner: " + winnerName
                + ", Loser: " + loserName + ", By Disconnect: " + byDisconnect);

        if (byDisconnect) {
            winner.sendMessage("LOBBY:" + lobbyId + ":GAME_RESULT:WIN_DISCONNECT:" + loserName);
        } else {
            winner.sendMessage("LOBBY:" + lobbyId + ":GAME_RESULT:WIN:" + loserName); 
            loser.sendMessage("LOBBY:" + lobbyId + ":GAME_RESULT:LOSS:" + winnerName);
        }
    }

    private void broadcast(String lobbySpecificMessage) {
        String fullMessage = "LOBBY:" + lobbyId + ":" + lobbySpecificMessage;
        System.out.println("GameLobby " + lobbyId + " broadcasting: \"" + fullMessage + "\" to " + clients.size() + " clients.");

        for (SClient c : clients) {
            c.sendMessage(fullMessage);
        }
    }
}