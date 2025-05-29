package Client;

import Game.HowToPlay;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SnakesAndLaddersMain extends javax.swing.JFrame {

    public SnakesAndLaddersMain() {
        initComponents();
        setTitle("Yılanlar ve Merdivenler - Giriş");
        setLocationRelativeTo(null); // Pencereyi ortala
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Login penceresi kapanınca uygulama bitsin
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_islemler = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_isim = new javax.swing.JTextField();
        btn_giris = new javax.swing.JButton();
        lbl_resim = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_help = new javax.swing.JMenu();
        btn_kurallar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("İsminizi Giriniz:");

        btn_giris.setText("Giriş");
        btn_giris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_girisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_islemlerLayout = new javax.swing.GroupLayout(pnl_islemler);
        pnl_islemler.setLayout(pnl_islemlerLayout);
        pnl_islemlerLayout.setHorizontalGroup(
            pnl_islemlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_islemlerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(pnl_islemlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_giris, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_isim, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnl_islemlerLayout.setVerticalGroup(
            pnl_islemlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_islemlerLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(pnl_islemlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_isim, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_giris, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        lbl_resim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/loginPageImage.png"))); // NOI18N

        menu_help.setText("Yardım");

        btn_kurallar.setText("Nasıl Oynanır?");
        btn_kurallar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kurallarActionPerformed(evt);
            }
        });
        menu_help.add(btn_kurallar);

        jMenuBar1.add(menu_help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_resim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(pnl_islemler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lbl_resim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnl_islemler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_girisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_girisActionPerformed
        String playerName = txt_isim.getText().trim();
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen bir oyuncu adı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new ClientGUI(playerName).setVisible(true);
        });
    }//GEN-LAST:event_btn_girisActionPerformed

    private void btn_kurallarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kurallarActionPerformed
        new HowToPlay().setVisible(true);
    }//GEN-LAST:event_btn_kurallarActionPerformed

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SnakesAndLaddersMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SnakesAndLaddersMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SnakesAndLaddersMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SnakesAndLaddersMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SnakesAndLaddersMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_giris;
    private javax.swing.JMenuItem btn_kurallar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lbl_resim;
    private javax.swing.JMenu menu_help;
    private javax.swing.JPanel pnl_islemler;
    private javax.swing.JTextField txt_isim;
    // End of variables declaration//GEN-END:variables

}
