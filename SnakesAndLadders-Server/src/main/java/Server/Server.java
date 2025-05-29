package Server;

import Game.LobbyManager;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    Socket socket;
    private static final int PORT = 12345;

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + ". Waiting for clients...");

            while (!serverSocket.isClosed()) {
                
                try {
                    socket = serverSocket.accept();
                    System.out.println("Client attempting to connect from: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

                    SClient newClient = new SClient(socket);

                    new Thread(newClient).start();
                } catch (IOException e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server could not start on port " + PORT + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}