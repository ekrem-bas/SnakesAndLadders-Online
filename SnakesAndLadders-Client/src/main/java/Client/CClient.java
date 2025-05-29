package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class CClient {

    // socket
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    // arayuzde gerceklestirilecek degisiklikler icin arayuz degiskeni
    private ClientGUI gui;
    // bu client'in bulundugu lobby'nin id'si
    private int lobbyId = -1;
    // lobby icindeki player number'i 
    private int playerNumber = -1;

    public CClient(String serverAddress, int port, ClientGUI gui) throws IOException {
        this.gui = gui;
        try {
            this.socket = new Socket(serverAddress, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("CClient: Connected to server " + serverAddress + ":" + port);
            new Thread(this::listenToServer).start();
            // client baslar baslamaz server'a ismini gonderiyor.
            out.println("NAME:" + gui.getPlayerName());
        } catch (IOException e) {
            System.err.println("CClient: Connection to server failed: " + e.getMessage());
            JOptionPane.showMessageDialog(this.gui, "Sunucuya bağlanılamadı: " + e.getMessage(), "Bağlantı Hatası", JOptionPane.ERROR_MESSAGE);
            this.gui.dispose();
            System.exit(0);
            throw e;
        }
    }

    // serveri dinle
    private void listenToServer() {
        try {
            String serverMessage; // Sunucudan gelen ham mesaj (örn: "LOBBY:1:DICE_ROLLED:5")
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("CClient received from server: " + serverMessage);
                final String finalMessage = serverMessage;
                if (finalMessage.startsWith("LOBBY:")) {
                    String messageContent = finalMessage.substring(6);
                    processLobbyMessage(messageContent);
                } else if (finalMessage.startsWith("WAIT:")) {
                    gui.setStatus("Rakip oyuncu araniyor...");
                } else {
                    System.out.println("CClient: Received unhandled message type: " + finalMessage);
                    gui.setStatus("Sunucu: " + finalMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("CClient: Lost connection to server: " + e.getMessage());
            JOptionPane.showMessageDialog(gui, "Sunucuyla bağlantı koptu! Lütfen yeniden başlatın.", "Ağ Hatası", JOptionPane.ERROR_MESSAGE);
            gui.enableRollButton(false);
            gui.setStatus("Bağlantı kesildi. Lütfen yeniden başlatın.");
        } finally {
            close();
            JOptionPane.showMessageDialog(gui,
                    "Sunucuyla bağlantı koptu veya sunucu kapatıldı.\nUygulama şimdi kapanacak.",
                    "Bağlantı Hatası",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // messageContent : <lobbyId>:<COMMAND>:<DATA>
    private void processLobbyMessage(String messageContent) {
        String[] parts = messageContent.split(":", 2);

        int parsedLobbyId = Integer.parseInt(parts[0]);

        this.lobbyId = Integer.parseInt(parts[0]);

        String commandAndData = parts[1];
        String[] commandParts = commandAndData.split(":", 2);
        String command = commandParts[0];
        String data = commandParts[1];

        int[] positions;

        switch (command) {
            case "LOBBY_JOINED":
                this.playerNumber = Integer.parseInt(data);
                gui.setPlayerId(this.playerNumber);
                gui.setStatus(this.lobbyId + " numaralı lobiye Oyuncu " + this.playerNumber + " olarak katıldınız.");
                System.out.println("CClient: Successfully joined lobby " + this.lobbyId + " as Player " + this.playerNumber);
                break;
            case "GAME_START":
                positions = parsePositionData(data);
                gui.startGame(positions[0], positions[1]);
                break;
            case "POSITION_UPDATE":
                positions = parsePositionData(data);
                gui.updatePositions(positions[0], positions[1]);
                break;
            case "TURN_UPDATE":
                // Data: "playerNumber:playerName" 
                String[] turnInfo = data.split(":", 2);
                int currentTurnPlayerNumber = Integer.parseInt(turnInfo[0]);
                String currentTurnPlayerName = turnInfo[1];
                if (this.playerNumber == currentTurnPlayerNumber) {
                    gui.setStatus("Sıra Sende, " + currentTurnPlayerName + "!");
                    gui.enableRollButton(true);
                } else {
                    gui.setStatus("Rakibin (" + currentTurnPlayerName + ") Sırası!");
                    gui.enableRollButton(false);
                }
                break;
            case "GAME_RESULT":
                // Data: RESULT_TYPE:PLAYER_NAME
                String[] resultParts = data.split(":", 2);
                String resultType = resultParts[0];
                String relevantPlayerName = resultParts[1];
                this.handleGameResult(resultType, relevantPlayerName);
                break;
            case "MY_MOVE_UPDATE":
                // Data: GosterilecekEskiKonum:GosterilecekYeniKonum
                String[] moveInfo = data.split(":");
                int displayOldP = Integer.parseInt(moveInfo[0]);
                int displayNewP = Integer.parseInt(moveInfo[1]);
                gui.updateMyMoveInfo(displayOldP, displayNewP);
                break;
            case "DICE_ROLLED":
                // Data: AtılanZarDegeri
                int diceRolledValue = Integer.parseInt(data);
                gui.updateDiceImage(diceRolledValue);
                break;
            default:
                System.out.println("CClient: Received unknown LOBBY command: " + command + " with payload: " + data);
                break;
        }
    }

    private int[] parsePositionData(String payload) {
        String cleanedPayload = payload.replaceAll("[\\[\\]\\s]", "");
        String[] posArray = cleanedPayload.split(",");
        int p1Pos = Integer.parseInt(posArray[0]);
        int p2Pos = Integer.parseInt(posArray[1]);
        return new int[]{p1Pos, p2Pos};
    }

    public void handleGameResult(String resultType, String relevantPlayerName) {
        gui.enableRollButton(false);
        String dialogTitle = "Oyun Bitti";
        String dialogMessage;
        String statusMessage = "";

        switch (resultType) {
            case "WIN":
                // 'relevantPlayerName' : kaybeden kisi
                dialogMessage = "Tebrikler, " + this.gui.getPlayerName() + "! Kazandın!";
                statusMessage = "KAZANDIN!";
                JOptionPane.showMessageDialog(this.gui, dialogMessage, dialogTitle, JOptionPane.INFORMATION_MESSAGE);
                break;
            case "LOSS":
                // 'relevantPlayerName' : kazanan kisi
                dialogMessage = "Üzgünüm, " + this.gui.getPlayerName() + ". Kaybettin.\nKazanan: " + relevantPlayerName + ".";
                statusMessage = "KAYBETTİN! Kazanan: " + relevantPlayerName;
                JOptionPane.showMessageDialog(this.gui, dialogMessage, dialogTitle, JOptionPane.WARNING_MESSAGE); // Or INFORMATION_MESSAGE
                break;
            case "WIN_DISCONNECT":
                // 'relevantPlayerName' : disconnected olan kisi
                dialogMessage = relevantPlayerName + " oyundan ayrıldı. Kazandın, " + this.gui.getPlayerName() + "!";
                statusMessage = "KAZANDIN! (Rakip Ayrıldı)";
                JOptionPane.showMessageDialog(this.gui, dialogMessage, dialogTitle, JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        gui.setStatus(statusMessage);

        // kullanici pencereyi kapatmazsa 45 saniye sonra otomatik kapat
        int delayInMilliseconds = 45000;

        Timer autoCloseTimer = new Timer(delayInMilliseconds, (e) -> {
            JOptionPane.showMessageDialog(this.gui,
                    "Uygulama 45 saniyedir açık. Bağlantı sonlandırılıyor.",
                    "Otomatik Kapanış", JOptionPane.INFORMATION_MESSAGE);

            close();

            System.exit(0);
        });

        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();
    }

    public void sendRoll(int diceValue) {
        String message = "LOBBY:" + this.lobbyId + ":ROLL:" + diceValue;
        out.println(message);
    }

    public void close() {
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
            System.out.println("CClient: " + this.gui.getPlayerName() + " Connection resources closed.");
        } catch (IOException e) {
            System.err.println("CClient: Exception while closing resources: " + e.getMessage());
        }
    }
}