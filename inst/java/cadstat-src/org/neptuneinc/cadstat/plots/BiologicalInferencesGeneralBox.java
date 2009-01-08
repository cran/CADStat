/*
 * BiologicalInferencesGeneralBox.java
 *
 * Created on November 21, 2008, 5:10 PM
 */

package org.neptuneinc.cadstat.plots;
import javax.swing.*;
/**
 *
 * @author  Liejun.Wu
 * We use this java class as the general popup box; R will call the
 * java class and pass the message to the popup box.
 */
public class BiologicalInferencesGeneralBox extends javax.swing.JFrame {
	
	
	public BiologicalInferencesGeneralBox(){
		
	}
	
	public BiologicalInferencesGeneralBox(String titleStr, String labelStr) {
		initComponents(titleStr,labelStr);
	}                      
	
	private void initComponents(String titleStr, String labelStr) {
		label = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
		
		label.setFont(new java.awt.Font("Tahoma", 0, 13));
		label.setText(labelStr);
		
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
		}
		});
		
		setLocation(300, 300);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(titleStr);//JFrame title
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(label))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(label)
                .addGap(43, 43, 43)
                .addComponent(okButton)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
	}
	
	public JFrame getMyGUI(String titleStr, String labelStr){
		JFrame jf = new BiologicalInferencesGeneralBox(titleStr, labelStr);
		jf.setVisible(true);
		return jf;
	}
	
	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}
	
	private javax.swing.JButton okButton;
    private javax.swing.JLabel label;      
}
