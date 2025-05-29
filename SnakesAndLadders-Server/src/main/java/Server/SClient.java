package Server;

import Game.GameLobby;
import Game.LobbyManager;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SClient implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameLobby currentLobby;
    private final int clientId;
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private int playerNumberInLobby = 0;
    private String playerName = "NoName";

    public SClient(Socket socket) {
        this.socket = socket;
        this.clientId = idCounter.getAndIncrement();
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("SClient " + clientId + " connection error: " + e.getMessage());
        }
    }

    public void setCurrentLobby(GameLobby lobby) {
        this.currentLobby = lobby;
    }

    public GameLobby getCurrentLobby() {
        return currentLobby;
    }

    public int getClientId() {
        return clientId;
    }

    public void setPlayerNumberInLobby(int playerNumber) {
        this.playerNumberInLobby = playerNumber;
    }

    public int getPlayerNumberInLobby() {
        return playerNumberInLobby;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // SClient baslar baslamaz alacagi ilk mesaj NAME ve NAME'in direkt islenmesi lazim
                if (inputLine.startsWith("NAME:")) {
                    String[] parts = inputLine.split(":", 2);
                    this.setPlayerName(parts[1].trim());
                    System.out.println("SClient " + this.playerName + " (ID: " + getClientId() + ") listening for messages....");
                    LobbyManager.getInstance().addClient(this);
                } else {
                    System.out.println("SClient " + clientId + " (" + this.playerName + ") received: " + inputLine);
                    LobbyManager.getInstance().processMessage(this, inputLine);
                }
            }
        } catch (IOException e) {
            System.out.println("SClient " + clientId + " (" + this.playerName + " - Player " + playerNumberInLobby + ") disconnected.");
        } finally {
            System.out.println("SClient " + clientId + " (" + this.playerName + " - Player " + playerNumberInLobby + ") cleaning up and closing run method...");
            LobbyManager.getInstance().removeClient(this);
            close();
        }
    }

    public void close() {
        System.out.println("SClient " + clientId + " (" + this.playerName + " - Player " + playerNumberInLobby + ") attempting to close resources.");
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("SClient " + clientId + " socket closed successfully.");
        } catch (IOException e) {
            System.err.println("SClient: Exception while closing resources: " + e.getMessage());
        }
    }
}