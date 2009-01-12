/*
 * BiologicalInferencesTaxaNameDupITIS.java
 *
 * Created on November 18, 2008
 */
package org.neptuneinc.cadstat.plots;
import javax.swing.*;
import org.rosuda.JGR.JGR;

/**
 *
 * @author liejun.wu
 * We use this java class to list taxa with multiple ITIS matches; display them to user;
 * user will select one ITIS records for a taxa; and return the select to a function. 
 */
public class BiologicalInferencesTaxaNameDupITIS extends javax.swing.JFrame {

	/** constructor */
	public BiologicalInferencesTaxaNameDupITIS() {

	}

	/** constructor with param; num = how many ITIS records 
	str = array of ITIS records
	 */
	public BiologicalInferencesTaxaNameDupITIS(int num, String str[], double displayNum) {
		numOfTaxa = num;
		taxaName = new String[numOfTaxa];
		taxaName = str;
		messageDisplayNum = displayNum;
		initComponents();
	}

	private void initComponents() {
		title = new javax.swing.JLabel();
		note = new javax.swing.JLabel();
		okButton = new javax.swing.JButton();
		taxaNameScrollPane = new javax.swing.JScrollPane();
		taxaNameList = new javax.swing.JList(taxaName);

		setLocation(200, 200);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Biological Inferences");

		title.setFont(new java.awt.Font("Tahoma", 0, 13));
		if (messageDisplayNum == 0) {
			title.setText("<html>The following taxa match with multiple ITIS records.<br /> Please select the appropriate taxon from list.</html>");
		} else {
			title.setText("<html>You either selected multiple records for one taxon <br />or did not select a record for one taxon.<br />Please select again!</html>");
		}
		note.setText("<html>Left \"column\" is the taxa name.<br /> Select one taxon from each of the duplicated choices.<br />Press Ctrl+ while selecting multiple lines.</html>");
		note.setBorder(javax.swing.BorderFactory.createTitledBorder("Note"));
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButton1ActionPerformed(evt);
			}
		});

		taxaNameScrollPane.setViewportView(taxaNameList);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
			addGroup(layout.createSequentialGroup()
				.addGap(114, 114, 114)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(okButton)
					.addComponent(title)
					.addComponent(note)
					.addComponent(taxaNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addContainerGap(113, Short.MAX_VALUE)));
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(title)
				.addGap(18, 18, 18)
				.addComponent(taxaNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
				.addGap(18, 18, 18).addComponent(okButton)
				.addGap(18, 18, 18)
				.addComponent(note)
				.addGap(19, 19, 19)));

		pack();
	}

	private void okButton1ActionPerformed(java.awt.event.ActionEvent evt) {

		Object string[];
		string = taxaNameList.getSelectedValues();

		String str = "";
		for (int i = 0; i < string.length; i++) {
			if (i == string.length - 1) {
				str = str + "\"" + string[i].toString() + "\"";
			} else {
				str = str + "\"" + string[i].toString() + "\"" + ",";
			}
		}
		// call R function and pass the selected value
		String cmd = "bioinfer3.JGR(tempstr= c(" + str + "),taxonomicFile=\"bcntTax.txt\", otuFile=\"OTU.txt\",mlFile=\"MaxLikEnv.txt\" )";
		JGR.MAINRCONSOLE.execute(cmd, true);
		this.dispose();
	}

	//Method will be called by R to initial the Java interface
	/*displayNum = 1, then display message normally
	displayNum = 2, then dispaly error message "Your selection doesn't follow the 
	instruction below, please select again.*/
	private JFrame getMyGUI(int num, String str[], double displayNum) {

		JFrame jf = new BiologicalInferencesTaxaNameDupITIS(num, str, displayNum);
		jf.setVisible(true);
		return jf;
	}

	private javax.swing.JButton okButton;
	private javax.swing.JLabel title;
	private javax.swing.JLabel note;
	private javax.swing.JList taxaNameList;
	private javax.swing.JScrollPane taxaNameScrollPane;
	private String taxaName[];
	private int numOfTaxa;
	private double messageDisplayNum;
}
