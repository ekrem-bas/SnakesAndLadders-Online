

package Game;


public class HowToPlay extends javax.swing.JFrame {

    
    String instructionsText = "Snakes and Ladders - How to Play?\n\n" +
                                  "Objective: The first player to reach the 100th square wins!\n\n" +
                                  "Rules:\n" +
                                  "1. Player Name: Enter a name and click 'Login'.\n" +
                                  "2. Matchmaking: The system will match you with an opponent.\n" +
                                  "3. Turn: When it's your turn, press the 'Roll Dice' button.\n" +
                                  "4. Movement: You advance as many squares as the dice you rolled.\n" +
                                  "5. Ladders (🪜): If you land on the bottom of a ladder, you climb up.\n" +
                                  "6. Snakes (🐍): If you land on a snake's head, you slide down.\n" +
                                  "7. Winning: The first to reach the 100th square wins! 🎉\n\n" +
                                  "Your own pawn is shown as 'You', the opponent as 'Opp'.\n\n" +
                                  "Have Fun!";
    public HowToPlay() {
        initComponents();
        setLocationRelativeTo(null);
        txt_gameplay.setFocusable(false);
        txt_gameplay.setEditable(false);
        txt_gameplay.setText(instructionsText);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txt_gameplay = new javax.swing.JTextArea();
        btn_close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txt_gameplay.setColumns(20);
        txt_gameplay.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txt_gameplay.setRows(5);
        jScrollPane1.setViewportView(txt_gameplay);

        btn_close.setText("Close");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txt_gameplay;
    // End of variables declaration//GEN-END:variables

}
