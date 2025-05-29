package Client;

import Game.Dice;
import Game.Game;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends javax.swing.JFrame {

    private Game game;
    private CClient client;
    private int playerId = -1;
    private String playerName;

    public ClientGUI(String playerName) {
        initComponents();
        setLocationRelativeTo(null); // Center the window
        pnl_status.setBorder(new EmptyBorder(10, 10, 10, 20));
        pack();
        SwingUtilities.invokeLater(this::setBackgroundImage);
        this.btn_rollDice.setEnabled(false);
        this.lbl_turn.setText("Connecting to server...");
        this.playerName = playerName;
        this.setTitle("Snakes & Ladders - " + playerName);
        try {
            this.client = new CClient("localhost", 12345, this);
        } catch (IOException e) {
            setStatus("Offline. Could not connect to server.");
        }
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void startGame(int p1StartPos, int p2StartPos) {
        this.game = new Game();
        updatePositions(p1StartPos, p2StartPos);
        clearMyMoveInfo();
    }

    public void updatePositions(int p1Pos, int p2Pos) {
        game.updatePositions(p1Pos, p2Pos);
        lbl_board.repaint();
    }

    private void setBackgroundImage() {
        try {
            URL boardImageUrl = getClass().getResource("/assets/board2.jpg");
            if (boardImageUrl == null) {
                System.err.println("ClientGUI: CRITICAL - Background image /assets/board2.jpg NOT FOUND in classpath.");
                lbl_board.setText("Game board image not found");
                return;
            }
            ImageIcon boardIcon = new ImageIcon(boardImageUrl);

            int panelWidth = pnl_board.getWidth() > 0 ? pnl_board.getWidth() : boardIcon.getIconWidth();
            int panelHeight = pnl_board.getHeight() > 0 ? pnl_board.getHeight() : boardIcon.getIconHeight();

            if (panelWidth > 0 && panelHeight > 0) {
                Image scaledImage = boardIcon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
                lbl_board.setIcon(new ImageIcon(scaledImage));
            } else {
                System.out.println("ClientGUI: Panel dimensions for board not ready for scaling or image has no dimensions. Setting unscaled icon.");
                lbl_board.setIcon(boardIcon);
            }
        } catch (Exception e) {
            System.err.println("ClientGUI: Exception during setBackgroundImage: " + e.getMessage());
            e.printStackTrace();
            lbl_board.setText("Error loading board image");
        }
    }

    public void setStatus(String status) {
        lbl_turn.setText(status.replace("\n", ""));
    }

    public void enableRollButton(boolean enable) {
        btn_rollDice.setEnabled(enable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_board = new javax.swing.JPanel();
        lbl_board = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // First, do the normal drawing of the label
                if (game != null) {
                    game.drawPlayers(g, playerId);
                }
            }
        };
        pnl_status = new javax.swing.JPanel();
        btn_rollDice = new javax.swing.JButton();
        lbl_dice = new javax.swing.JLabel();
        lbl_turn = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_currentPosition = new javax.swing.JLabel();
        lbl_oldPosition = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout pnl_boardLayout = new javax.swing.GroupLayout(pnl_board);
        pnl_board.setLayout(pnl_boardLayout);
        pnl_boardLayout.setHorizontalGroup(
            pnl_boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_boardLayout.createSequentialGroup()
                .addComponent(lbl_board, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_boardLayout.setVerticalGroup(
            pnl_boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_board, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );

        getContentPane().add(pnl_board, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, -1));

        pnl_status.setPreferredSize(new java.awt.Dimension(240, 950));

        btn_rollDice.setText("Roll Dice");
        btn_rollDice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rollDiceActionPerformed(evt);
            }
        });

        lbl_turn.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        lbl_turn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_turn.setText("Opponents Turn");
        lbl_turn.setAutoscrolls(true);

        jLabel1.setText("Old Position: ");

        jLabel2.setText("New Position:");

        javax.swing.GroupLayout pnl_statusLayout = new javax.swing.GroupLayout(pnl_status);
        pnl_status.setLayout(pnl_statusLayout);
        pnl_statusLayout.setHorizontalGroup(
            pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_statusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_turn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_dice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_statusLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_rollDice, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnl_statusLayout.createSequentialGroup()
                        .addGroup(pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_statusLayout.createSequentialGroup()
                                .addComponent(lbl_currentPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(186, 186, 186))
                            .addComponent(lbl_oldPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnl_statusLayout.setVerticalGroup(
            pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_statusLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_oldPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_statusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_statusLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                        .addComponent(lbl_turn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(lbl_dice, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)
                        .addComponent(btn_rollDice, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(pnl_statusLayout.createSequentialGroup()
                        .addComponent(lbl_currentPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(pnl_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 0, 380, 900));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateDiceImage(int diceValue) {
        ImageIcon diceIcon = Dice.getDiceIcon(diceValue);
        if (diceIcon != null && diceIcon.getIconWidth() > 0) {
            // Scale the dice image according to the lbl_dice size
            int lblWidth = lbl_dice.getWidth() > 0 ? lbl_dice.getWidth() : 100;
            int lblHeight = lbl_dice.getHeight() > 0 ? lbl_dice.getHeight() : 100;
            Image scaledDiceImage = diceIcon.getImage().getScaledInstance(lblWidth, lblHeight, Image.SCALE_SMOOTH);
            lbl_dice.setIcon(new ImageIcon(scaledDiceImage));
        } else {
            lbl_dice.setIcon(null);
            System.err.println("ClientGUI: Dice image (" + diceValue + ") could not be loaded in updateDiceImage or Dice.getDiceIcon returned null.");
        }
    }

    private void btn_rollDiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rollDiceActionPerformed
        int diceResult = Dice.rollDice();
        client.sendRoll(diceResult);
        btn_rollDice.setEnabled(false);
    }//GEN-LAST:event_btn_rollDiceActionPerformed

    public void updateMyMoveInfo(int oldPosition, int newPosition) {
        lbl_oldPosition.setText(String.valueOf(oldPosition));
        lbl_currentPosition.setText(String.valueOf(newPosition));
    }

    public void clearMyMoveInfo() {
        lbl_oldPosition.setText("-"); // Default value
        lbl_currentPosition.setText("-"); // Default value
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (client != null) {
            System.out.println("ClientGUI: Window closing. Shutting down client connection.");
            client.close();
        } else {
            System.exit(1);
        }
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_rollDice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lbl_board;
    private javax.swing.JLabel lbl_currentPosition;
    private javax.swing.JLabel lbl_dice;
    private javax.swing.JLabel lbl_oldPosition;
    private javax.swing.JLabel lbl_turn;
    private javax.swing.JPanel pnl_board;
    private javax.swing.JPanel pnl_status;
    // End of variables declaration//GEN-END:variables

}
