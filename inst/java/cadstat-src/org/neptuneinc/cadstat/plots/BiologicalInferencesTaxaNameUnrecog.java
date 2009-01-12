/*
 * BiologicalInferencesTaxaNameUnrecog.java
 *
 * Created on November 13, 2008, 11:10 AM
 */

package org.neptuneinc.cadstat.plots;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.rosuda.JGR.JGR;
/**
 *
 * @author  Liejun.Wu
 * We use this java class to get the unrecognized taxa name from R; display them to user;
 * user will input the correct taxa name; finally pass the user input to a R function. 
 */
public class BiologicalInferencesTaxaNameUnrecog extends javax.swing.JFrame implements ListSelectionListener, DocumentListener{
	
	/** constructor */
	public BiologicalInferencesTaxaNameUnrecog() {
		//initComponents();
	}
	/** constructor with param; num = how many of unrecognized taxon names 
		str = one taxa names
	 */
	public BiologicalInferencesTaxaNameUnrecog(int num, String str){
		numOfTaxa = num;
		taxaName = new String[numOfTaxa];
		taxaName[0] = str;
		initComponents();
	}
	/** constructor with param; num = how many of unrecognized taxon names 
		str = array of taxa names
	 */
	public BiologicalInferencesTaxaNameUnrecog(int num, String str[]) {
		numOfTaxa = num;
		taxaName = new String[numOfTaxa];
		taxaName = str;
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 */
    private void initComponents() {
	
		//create object references of JLabel and JTextField
		taxaNameOrigLabel = new JLabel[numOfTaxa];
		taxaNameCorrTextField = new JTextField[numOfTaxa];
		//allocate memory of each of object - JLable and JTextField
		for(int i=0; i<numOfTaxa; i++){
			taxaNameOrigLabel[i] = new javax.swing.JLabel();
			//set TextField length to 20
			taxaNameCorrTextField[i] = new javax.swing.JTextField(20);
		}
		for(int i=0; i<numOfTaxa; i++){
			taxaNameOrigLabel[i].setText(taxaName[i]);
			taxaNameCorrTextField[i].setText(taxaName[i]);
		}
		
        title = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
		taxaNamePanel = new javax.swing.JPanel();
		taxaNameScrollPane = new javax.swing.JScrollPane();
		
		title.setFont(new java.awt.Font("Tahoma", 0, 13));
		title.setText("Pleas correct the following unrecognized taxon names:");
		
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
		}
		});
		
        cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
		}
		});
		
		setLocation(200, 200);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Biological Inferences");//JFrame title
		
		//put the JLabel and JTextField in a JPanel and set springLayout on the panel 
        javax.swing.SpringLayout taxaNamePanelLayout = new javax.swing.SpringLayout();
        taxaNamePanel.setLayout(taxaNamePanelLayout);
		for (int i=0; i<numOfTaxa; i++){
			taxaNamePanel.add(taxaNameOrigLabel[i]);
			taxaNamePanel.add(taxaNameCorrTextField[i]);
		}
		SpringUtilities.makeCompactGrid(taxaNamePanel,
										numOfTaxa,2,  //rows, cols
										6,6,  //initX, initY
										6,6); //xPad, yPad
		
		/*If set the height of taxaNameCorrTextField to 25, then taxaNameScrollPane with
		 * height 300 will hold up to 12(300/25=12) textField. If numOfTaxaName<12, we have to set 
		 * the scrollPane's hight to a smaller number, so we keep the hight of each textField as 25.
		 */
		int preferredHigh;
		if(numOfTaxa <12)
			preferredHigh = numOfTaxa * 25 + 30; //+30:need some space for gap
		else
			preferredHigh = 300;
		
		taxaNameScrollPane = new javax.swing.JScrollPane(taxaNamePanel);
		taxaNameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		taxaNameScrollPane.setPreferredSize(new Dimension(400, preferredHigh));
		taxaNameScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//GroupLayout on JFrame, includes the title, panel, and button
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(taxaNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addGap(52, 52, 52)
                        .addComponent(cancelButton))
                    .addComponent(title))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(title)
                .addGap(18, 18, 18)
                .addComponent(taxaNameScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
	
	private JFrame getMyGUI(int num, String str){
		JFrame jf = new BiologicalInferencesTaxaNameUnrecog(num,str);
		jf.setVisible(true);
		return jf;
	}
	
	//Method will be called by R to initial the Java interface
	private JFrame getMyGUI(int num, String str[]){
		
		JFrame jf = new BiologicalInferencesTaxaNameUnrecog(num,str);
		jf.setVisible(true);
		return jf;
	}
	
	//User click OK, Java will call the R function and pass the user input taxa name(as vector data type). 
	private void okButtonActionPerformed(java.awt.event.ActionEvent evt)                                              
	{    
		//Validate the JTextField, if any field is empty, user will get a popup box.
		int j = 0;  
		for(int i=0; i<numOfTaxa; i++){
			if (taxaNameCorrTextField[i].getText().length()==0)
				j = j+1;
		}
		if (j>0){
			System.out.println("No empty taxa name allowed");
			JOptionPane.showMessageDialog(null,"Empty taxa name aren't allowed!");
		}
		else{
			System.out.println("OK buttom is pushed");
			
			String str = "";
			for(int i=0; i<numOfTaxa; i++){
				if(i==numOfTaxa-1)
					str =  str +  "\"" + taxaNameCorrTextField[i].getText() + "\"";
				else 
					str =  str +  "\"" + taxaNameCorrTextField[i].getText() + "\"" + ",";
			}
			
			String cmd = "bioinfer2.JGR(tmiss1=c(" + str + "),taxonomicFile=\"bcntTax.txt\", otuFile=\"OTU.txt\",mlFile=\"MaxLikEnv.txt\" )" ;
			JGR.MAINRCONSOLE.execute(cmd, true);
			this.dispose();
		}
		
	}  
	
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt){
		this.dispose();
	}
	
    // Variables declaration - do not modify
    private javax.swing.JLabel title;
	private javax.swing.JButton okButton;
    private javax.swing.JButton cancelButton;
 	private javax.swing.JPanel taxaNamePanel;
	private javax.swing.JScrollPane taxaNameScrollPane;
	private javax.swing.JLabel   taxaNameOrigLabel[];
	private javax.swing.JTextField taxaNameCorrTextField[];
	private int numOfTaxa;
	private String taxaName[];

	public void valueChanged(ListSelectionEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void insertUpdate(DocumentEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void removeUpdate(DocumentEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void changedUpdate(DocumentEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
