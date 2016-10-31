/*
 * To change this template,  choose Tools | Templates
 * and open the template in the editor.
 */
package org.neptuneinc.cadstat.ui;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.neptuneinc.cadstat.utils.GUIUtils;

import org.rosuda.JGR.DataLoader;
import org.rosuda.JGR.JGR;

/**
 *
 * @author  Pasha Minallah
 */
public class DatasetPane extends JPanel
{
  /** Creates new form DataPane */
  public DatasetPane()
  {
    this.initComponents();

    this.refreshDatasetComboBox();
  }

  public String getSelectedDataset()
  {
    return (GUIUtils.getSelectedItem(this.getDatasetComboBox()));
  }

  public void refreshDatasetComboBox()
  {
    GUIUtils.refreshDataset(datasetComboBox);
  }

  public void refreshDatasetComboBoxValidity()
  {
    GUIUtils.refreshDatasetValidity(datasetComboBox);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    datasetLabel = new javax.swing.JLabel();
    datasetComboBox = new javax.swing.JComboBox();
    browseButton = new javax.swing.JButton();

    datasetLabel.setLabelFor(datasetComboBox);
    datasetLabel.setText("Dataset:");

    browseButton.setText("Browse...");
    browseButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
    browseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(datasetLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(datasetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(browseButton))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(datasetLabel)
        .addComponent(datasetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(browseButton))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void browseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browseButtonActionPerformed
  {//GEN-HEADEREND:event_browseButtonActionPerformed
    DataLoader loader = new DataLoader();
    String dataName = loader.getDataName();

    JGR.MAINRCONSOLE.toBack();

    if (dataName != null && dataName.length() > 0)
    {
    }
  }//GEN-LAST:event_browseButtonActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton browseButton;
  private javax.swing.JComboBox datasetComboBox;
  private javax.swing.JLabel datasetLabel;
  // End of variables declaration//GEN-END:variables

  public JComboBox getDatasetComboBox()
  {
    return datasetComboBox;
  }

  public void setDatasetComboBox(JComboBox datasetComboBox)
  {
    this.datasetComboBox = datasetComboBox;
  }

  public void setDatasetLabel(JLabel datasetLabel)
  {
    this.datasetLabel = datasetLabel;
  }

  public JLabel getDatasetLabel()
  {
    return datasetLabel;
  }
}