package Game;

import javax.swing.ImageIcon;

public class Dice {
    
    // zar atma fonksiyonu
    public static int rollDice() {
        return (int) (Math.random() * 6) + 1;
    }

    // zara gore zar resmi getir
    public static ImageIcon getDiceIcon(int number) {
        String diceImageName;
        switch (number) {
            case 1 ->
                diceImageName = "dice_1.png";
            case 2 ->
                diceImageName = "dice_2.png";
            case 3 ->
                diceImageName = "dice_3.png";
            case 4 ->
                diceImageName = "dice_4.png";
            case 5 ->
                diceImageName = "dice_5.png";
            case 6 ->
                diceImageName = "dice_6.png";
            default ->
                throw new IllegalArgumentException("Invalid dice number: " + number);
        }
        try {
            java.net.URL imageUrl = Dice.class.getResource("/assets/dice-images/" + diceImageName);
            if (imageUrl == null) {
                System.err.println("Dice image not found: " + diceImageName);
                return null; 
            }
            return new ImageIcon(imageUrl);
        } catch (Exception e) {
            System.err.println("Error loading dice image: " + e.getMessage());
            return null;
        }
    }
}