package Game;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Game {

    // oyuncu pozisyonlari
    private int player1Position = 1;
    private int player2Position = 1;
    // yilanlarin ve merdivenlerin tutuldugu map
    private final Map<Integer, Integer> snakesAndLadders = new HashMap<>();

    public Game() {
        // oyun baslangicinda merdivenleri ve yilanlari ayarla
        initializeSnakesAndLadders();
    }

    private void initializeSnakesAndLadders() {
        // Yılanlar (Başlangıç -> Bitiş)
        snakesAndLadders.put(57, 19);
        snakesAndLadders.put(37, 5);
        snakesAndLadders.put(33, 10);
        snakesAndLadders.put(70, 31);
        snakesAndLadders.put(92, 55);
        snakesAndLadders.put(97, 56);

        // Merdivenler (Başlangıç -> Bitiş)
        snakesAndLadders.put(7, 45);
        snakesAndLadders.put(40, 77);
        snakesAndLadders.put(34, 66);
        snakesAndLadders.put(48, 91);
        snakesAndLadders.put(74, 96);
        snakesAndLadders.put(63, 81);
    }

    // tahtadaki sayi karelerinin ortasini hesaplama fonksiyonu
    private int[] getCenterCircles(Graphics g, String text, Point cords) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int ascent = metrics.getAscent();
        int descent = metrics.getDescent();

        int centerX = cords.x;
        int centerY = cords.y;

        int textX = centerX - textWidth / 2;
        int textY = centerY + (ascent - descent) / 2;

        int[] coordinates = {textX, textY};
        return coordinates;
    }

    // oyuncular icin daire cizme fonksiyonu
    public void drawPlayers(Graphics g, int currentPlayerId) {
        String p1Text = "";
        String p2Text = "";
        String combinedText = "Both";

        if (currentPlayerId == 1) {
            p1Text = "You";
            p2Text = "Opp";
        } else if (currentPlayerId == 2) {
            p1Text = "Opp";
            p2Text = "You";
        }

        if (player1Position == player2Position && player1Position >= 1 && player1Position <= 100) {
            Point overlapCoords = getCoordinates(player1Position);

            g.setColor(Color.GRAY); // Gri renk
            g.fillOval(overlapCoords.x - 24, overlapCoords.y - 24, 48, 48);

            g.setColor(Color.WHITE); // Beyaz metin
            g.setFont(new Font("Arial", Font.BOLD, 14));

            int[] combinedTextCoords = getCenterCircles(g, combinedText, overlapCoords);
            g.drawString(combinedText, combinedTextCoords[0], combinedTextCoords[1]);

        } else {
            // Oyuncu 1'i çiz
            Point p1Coords = getCoordinates(player1Position);
            g.setColor(new Color(34, 139, 34)); // Orman Yeşili
            g.fillOval(p1Coords.x - 24, p1Coords.y - 24, 48, 48);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            int[] p1TextCoords = getCenterCircles(g, p1Text, p1Coords);
            g.drawString(p1Text, p1TextCoords[0], p1TextCoords[1]);

            // Oyuncu 2'yi çiz
            Point p2Coords = getCoordinates(player2Position);
            g.setColor(new Color(0, 0, 139)); // Koyu Mavi
            g.fillOval(p2Coords.x - 24, p2Coords.y - 24, 48, 48);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            int[] p2TextCoords = getCenterCircles(g, p2Text, p2Coords);
            g.drawString(p2Text, p2TextCoords[0], p2TextCoords[1]);
        }
    }

    // oyuncularin pozisyonlarini guncelle
    public synchronized void updatePositions(int p1Pos, int p2Pos) {
        this.player1Position = Math.max(1, Math.min(p1Pos, 100));
        this.player2Position = Math.max(1, Math.min(p2Pos, 100));
    }

    // oyuncularin pozisyonlarini al
    public synchronized int[] getPositions() {
        return new int[]{player1Position, player2Position};
    }

    // yilan ve merdiven kontrolu
    public synchronized void checkSnakeOrLadder(int playerNumberWhoseTurnItIs) {
        int currentPosition;

        switch (playerNumberWhoseTurnItIs) {
            case 1 -> {
                currentPosition = player1Position;
                if (snakesAndLadders.containsKey(currentPosition)) {
                    int newPos = snakesAndLadders.get(currentPosition);
                    player1Position = newPos;
                }
            }
            case 2 -> {
                currentPosition = player2Position;
                if (snakesAndLadders.containsKey(currentPosition)) {
                    int newPos = snakesAndLadders.get(currentPosition);
                    player2Position = newPos;
                }
            }
            default -> {
                System.err.println("Game: Invalid player number (" + playerNumberWhoseTurnItIs + ") for snake/ladder check.");
            }
        }
    }

    public synchronized int getWinner() {
        if (player1Position == 100) {
            return 1; // Oyun bitti, kazanan 1. oyuncu
        }
        if (player2Position == 100) {
            return 2; // Oyun bitti, kazanan 2. oyuncu
        }
        return 0; // Oyun henüz bitmedi
    }

    private Point getCoordinates(int position) {
        final int NUM_COLS = 10;
        final int NUM_ROWS = 10;

        // ClientGUI pnl_board : 950x900
        final int BOARD_PIXEL_WIDTH = 950;
        final int BOARD_PIXEL_HEIGHT = 900;

        int squareWidth = BOARD_PIXEL_WIDTH / NUM_COLS;   
        int squareHeight = BOARD_PIXEL_HEIGHT / NUM_ROWS; 

        int zeroBasedPos = position - 1; // Convert to 0-99
        int row = zeroBasedPos / NUM_COLS; // Row from bottom (0-indexed: 0 for 1-10, 1 for 11-20, ...)
        int col = zeroBasedPos % NUM_COLS; // Column from left (0-indexed)

        // Adjust column for rows that go right-to-left
        // Rows 0, 2, 4, ... (even) go left-to-right
        // Rows 1, 3, 5, ... (odd) go right-to-left
        if (row % 2 != 0) { // If it's an odd row (1, 3, 5...)
            col = (NUM_COLS - 1) - col; // Reverse column index
        }

        // Calculate center of the square
        // X: (column index * square width) + half square width
        // Y: (total board height) - (row index * square height) - half square height (because Y is inverted in graphics)
        int centerX = (col * squareWidth) + (squareWidth / 2);
        int centerY = (BOARD_PIXEL_HEIGHT - (row * squareHeight)) - (squareHeight / 2);

        return new Point(centerX, centerY);
    }
}