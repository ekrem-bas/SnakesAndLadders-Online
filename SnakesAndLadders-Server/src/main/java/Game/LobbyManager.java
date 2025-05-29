package Game;

import Server.SClient;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Singleton LobbyManager sinifi
    Singleton olmasinin sebebi ise server icin sadece ama sadece bir tane lobby manager olusturulması
 */
public class LobbyManager {

    private static LobbyManager instance;
    private final AtomicInteger lobbyIdCounter = new AtomicInteger(1);
    private final Map<Integer, GameLobby> activeLobbies = new HashMap<>();
    private final List<SClient> waitingClients = new ArrayList<>();

    public static synchronized LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    public synchronized void addClient(SClient client) {
        System.out.println("LobbyManager: Adding client " + client.getPlayerName() + " with id " + client.getClientId());

        if (waitingClients.isEmpty()) {
            waitingClients.add(client);
            client.sendMessage("WAIT:Waiting for another player...");
            System.out.println("LobbyManager: Client '" + client.getPlayerName() + "' (ID: " + client.getClientId() + ") is waiting.");
        } else {
            SClient waitingPlayer1 = waitingClients.remove(0);
            SClient newPlayer2 = client;

            int lobbyId = lobbyIdCounter.getAndIncrement();
            GameLobby newLobby = new GameLobby(lobbyId);

            waitingPlayer1.setPlayerNumberInLobby(1);
            newLobby.addClient(waitingPlayer1);

            newPlayer2.setPlayerNumberInLobby(2);
            newLobby.addClient(newPlayer2);

            activeLobbies.put(lobbyId, newLobby);

            waitingPlayer1.sendMessage("LOBBY:" + lobbyId + ":LOBBY_JOINED:1");
            newPlayer2.sendMessage("LOBBY:" + lobbyId + ":LOBBY_JOINED:2");

            System.out.println("LobbyManager: Lobby " + lobbyId + " created for clients " + waitingPlayer1.getClientId() + " (P1) and " + newPlayer2.getClientId() + " (P2)");
            newLobby.startGame();
        }
    }

    public synchronized void removeClient(SClient client) {
        System.out.println("LobbyManager: Attempting to remove client " + client.getClientId() + " (" + client.getPlayerName() + ")");

        boolean removedFromWaiting = waitingClients.remove(client);
        if (removedFromWaiting) {
            System.out.println("LobbyManager: Client " + client.getPlayerName() + " removed from waiting list.");
        }

        GameLobby clientLobby = client.getCurrentLobby();

        if (clientLobby != null) {
            System.out.println("LobbyManager: Client " + client.getClientId() + " is in lobby " + clientLobby.getLobbyId() + ". Forwarding removal.");
            clientLobby.removeClient(client);
            if (clientLobby.isEmpty()) {
                System.out.println("LobbyManager: Lobby " + clientLobby.getLobbyId() + " is now empty. Removing from active lobbies map.");
                activeLobbies.remove(clientLobby.getLobbyId());
            }
        } else {
            System.out.println("LobbyManager: Client " + client.getClientId() + " was not associated with any active lobby upon removal request.");
        }
    }

    public synchronized void processMessage(SClient client, String message) {
        System.out.println("LobbyManager received from SClient " + client.getPlayerName() + " (ID: " + client.getClientId() + ", P" + client.getPlayerNumberInLobby() + "): " + message);
        if (message.startsWith("LOBBY:")) {
            String[] parts = message.split(":", 3);
            int lobbyIdFromMessage = Integer.parseInt(parts[1]);
            GameLobby targetLobby = activeLobbies.get(lobbyIdFromMessage);
            targetLobby.processMessage(client, parts[2]);
        } else {
            System.out.println("LobbyManager: Received non-LOBBY prefixed message from SClient " + client.getClientId() + ": \"" + message + "\" - This manager only handles LOBBY-prefixed messages.");
        }
    }
}