

package Game;


public class HowToPlay extends javax.swing.JFrame {

    
    String instructionsText = "Yılanlar ve Merdivenler - Nasıl Oynanır?\n\n" +
                                  "Amaç: 100. kareye ilk ulaşan oyuncu kazanır!\n\n" +
                                  "Kurallar:\n" +
                                  "1. Oyuncu Adı: Bir isim girip 'Giriş' yapın.\n" +
                                  "2. Eşleşme: Sistem sizi bir rakiple eşleştirecektir.\n" +
                                  "3. Sıra: Sıranız geldiğinde 'Zar At' butonuna basın.\n" +
                                  "4. Hareket: Attığınız zar kadar ilerlersiniz.\n" +
                                  "5. Merdivenler (🪜): Merdiven dibine gelirseniz yukarı çıkarsınız.\n" +
                                  "6. Yılanlar (🐍): Yılan başına gelirseniz aşağı kayarsınız.\n" +
                                  "7. Kazanma: 100. kareye ilk ulaşan kazanır! 🎉\n\n" +
                                  "Kendi piyonunuz 'You', rakip 'Opp' olarak gösterilir.\n\n" +
                                  "İyi Eğlenceler!";
    public HowToPlay() {
        initComponents();
        setLocationRelativeTo(null);
        txt_oynanis.setFocusable(false);
        txt_oynanis.setEditable(false);
        txt_oynanis.setText(instructionsText);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txt_oynanis = new javax.swing.JTextArea();
        btn_kapat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txt_oynanis.setColumns(20);
        txt_oynanis.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txt_oynanis.setRows(5);
        jScrollPane1.setViewportView(txt_oynanis);

        btn_kapat.setText("Kapat");
        btn_kapat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kapatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_kapat, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_kapat, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_kapatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kapatActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_kapatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_kapat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txt_oynanis;
    // End of variables declaration//GEN-END:variables

}
