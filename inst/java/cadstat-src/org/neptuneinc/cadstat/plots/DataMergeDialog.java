/*
 * DataMergeDialog.java
 *
 * Created on September 20, 2005, 8:05 PM
 */

package org.neptuneinc.cadstat.plots;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;

import org.neptuneinc.cadstat.utils.RUtils;

import org.rosuda.JGR.JGR;
import org.rosuda.JGR.JGRDataFileOpenDialog;

/**
 *
 * @author Mark Fitzgerald
 * @author Pasha Minallah
 */
public class DataMergeDialog extends JDialog
{

  public DataMergeDialog()
  {
    this.initComponents();

    this.getRootPane().setDefaultButton(submitButton);
    
    initLists();
    refreshDatasetLists(0,"","");
    if( isDatasetListValid(1) )
    {
      refreshBy1List();
      refreshBy2List();
    }
     
    updateValidity();
    
    pack();
  }
  
  private void initLists()
  {
    getDataset1List().setModel(new DefaultComboBoxModel());
    getDataset2List().setModel(new DefaultComboBoxModel());
    getBy11List().setModel(new DefaultComboBoxModel());
    getBy11List().insertItemAt("",0);
    getBy11List().setSelectedIndex(0);
    getBy12List().setModel(new DefaultComboBoxModel());
    getBy12List().insertItemAt("",0);
    getBy12List().setSelectedIndex(0);
    getBy13List().setModel(new DefaultComboBoxModel());
    getBy13List().insertItemAt("",0);
    getBy13List().setSelectedIndex(0);
    getBy21List().setModel(new DefaultComboBoxModel());
    getBy21List().insertItemAt("",0);
    getBy21List().setSelectedIndex(0);
    getBy22List().setModel(new DefaultComboBoxModel());
    getBy22List().insertItemAt("",0);
    getBy22List().setSelectedIndex(0);
    getBy23List().setModel(new DefaultComboBoxModel());
    getBy23List().insertItemAt("",0);
    getBy23List().setSelectedIndex(0);
  }
  
  private void updateValidity()
  {
    dataset1List.setEnabled(isDatasetListValid(1));
    dataset2List.setEnabled(isDatasetListValid(2));
   
    boolean isValid = ( getDataset1() != null ) || ( getDataset2() != null );
    if( isValid )
    {
      if( ( getSelectBy11List().equals("") ) ^ ( getSelectBy21List().equals("") ) )
      {
        isValid = false;
      }
      if( ( getSelectBy12List().equals("") ) ^ ( getSelectBy22List().equals("") ) )
      {
        isValid = false;
      }
      if( ( getSelectBy13List().equals("") ) ^ ( getSelectBy23List().equals("") ) )
      {
        isValid = false;
      }
    }
    
    submitButton.setEnabled(isValid);
  }
  
  public boolean isDatasetListValid(int i)
  {
    switch( i )
    {
      case 1:
        return (dataset1List.getItemCount() > 1);
      case 2:
        return (dataset2List.getItemCount() > 1);
    }
    return true;
  }
  
  
  private void refreshDatasetLists(int i, String keep1, String keep2)
  {
    String dataset;
    Object[] datasets = RUtils.getDatasetList();
    List extdata = new ArrayList();
    extdata.add("");
    for (int k=0; k < datasets.length; ++k)
    {
      extdata.add(datasets[k]);
    }
    Object[] dataModel = extdata.toArray();
    
    getDataset1List().removeAllItems();
    getDataset1List().setModel(new DefaultComboBoxModel(dataModel));
    getDataset2List().removeAllItems();
    getDataset2List().setModel(new DefaultComboBoxModel(dataModel));
  
    for( int j = 0, n = dataModel.length; j < n; ++j )
    {
      dataset = dataModel[j].toString();
      if( j==0 ){
        getDataset1List().setSelectedIndex(0);
        getDataset2List().setSelectedIndex(0);
      }
      else
      {
        if( dataset.equals(keep1) )
        {
          getDataset1List().setSelectedIndex(j);
        }
        if( dataset.equals(keep2) )
        {
          getDataset2List().setSelectedIndex(j);
        }
      }
    }
    
    switch( i )
    {
      case 1:
        refreshBy1List();
        break;
      case 2:
        refreshBy2List();
        break;
    }

    updateValidity();

  }
  

  private void refreshBy1List()
  {
    JComboBox[] byLists = { getBy11List(), getBy12List(), getBy13List() };
    JComboBox list;
    Vector varList;

    if ( isDatasetListValid(1) && !( getDataset1().equals("") ) )
    {
      String dataset = getDataset1();
      String[] colnames = RUtils.colnames(dataset);
      int colLength = colnames.length;
      for (int j = 0, n = byLists.length; j < n; ++j)
      {
        list = byLists[j];
        for (int k=1, m=list.getItemCount(); k < m; ++k)
        {
          list.removeItemAt(1);
        }
        for (int k=0; k < colLength; ++k)
        {
          list.insertItemAt(colnames[k],k+1);
        }
        list.setSelectedIndex(0);
      }
    }
   
    updateValidity();
  }
    
  private void refreshBy2List()
  {
    JComboBox[] byLists = { getBy21List(), getBy22List(), getBy23List() };
    JComboBox list;
    Vector varList;

    if ( isDatasetListValid(2) && !( getDataset2().equals("") ) )
    {
      String dataset = getDataset2();
      String[] colnames = RUtils.colnames(dataset);
      int colLength = colnames.length;
      for (int j = 0, n = byLists.length; j < n; ++j)
      {
        list = byLists[j];
        for (int k=1, m=list.getItemCount(); k < m; ++k)
        {
          list.removeItemAt(1);
        }
        for (int k=0; k < colLength; ++k)
        {
          list.insertItemAt(colnames[k],k+1);
        }
        list.setSelectedIndex(0);
      }
    }
   
    updateValidity();
  }

  public static String getSelectedItem(JComboBox cBox)
  {
    if (cBox != null && cBox.getItemCount() > 0)
    {
      return((String) cBox.getSelectedItem());
    }
    
    else
    {
      return (null);
    }
  }
  

  public static Object[] getSelectedItems(JList list)
  {
    if (list != null)
    {
      return(list.getSelectedValues());
    }
    
    else
    {
      return (null);
    }
  }
  

  public javax.swing.JPanel getDataset1Pane()
  {
    return dataset1Pane;
  }

  public void setDataset1Pane(javax.swing.JPanel dataset1Pane)
  {
    this.dataset1Pane = dataset1Pane;
  }
  
  public javax.swing.JPanel getDataset2Pane()
  {
    return dataset1Pane;
  }

  public void setDataset2Pane(javax.swing.JPanel dataset2Pane)
  {
    this.dataset2Pane = dataset2Pane;
  }
    
  public javax.swing.JPanel getSavePane()
  {
    return savePane;
  }
  
  public void setSavePane(javax.swing.JPanel savePane)
  {
    this.savePane = savePane;
  }
  
  public javax.swing.JComboBox getDataset1List()
  {
    return dataset1List;
  }
  
  public String getDataset1()
  {
    return (getSelectedItem(dataset1List));
  }

  public javax.swing.JComboBox getDataset2List()
  {
    return dataset2List;
  }
  
  public String getDataset2()
  {
    return (getSelectedItem(dataset2List));
  }

  public javax.swing.JComboBox getBy11List()
  {
    return by11List;
  }
  
  public String getSelectBy11List()
  {
    return (getSelectedItem(by11List));
  }
  
  public javax.swing.JComboBox getBy12List()
  {
    return by12List;
  }

  public String getSelectBy12List()
  {
    return (getSelectedItem(by12List));
  }

  public javax.swing.JComboBox getBy13List()
  {
    return by13List;
  }

  public String getSelectBy13List()
  {
    return (getSelectedItem(by13List));
  }
  
  public javax.swing.JComboBox getBy21List()
  {
    return by21List;
  }

  public String getSelectBy21List()
  {
    return (getSelectedItem(by21List));
  }
  
  public javax.swing.JComboBox getBy22List()
  {
    return by22List;
  }

  public String getSelectBy22List()
  {
    return (getSelectedItem(by22List));
  }
  
  public javax.swing.JComboBox getBy23List()
  {
    return by23List;
  }

  public String getSelectBy23List()
  {
    return (getSelectedItem(by23List));
  }
  
  private int setTextUsingFileChooser(JTextField textField, int mode)
  {
    String workingDir = RUtils.evalAsString("getwd()");
    JFileChooser fc = new JFileChooser(workingDir);
    fc.setMultiSelectionEnabled(false);
    fc.setFileSelectionMode(mode);

    int fcReturnState = fc.showOpenDialog(this);

    if (fcReturnState == JFileChooser.APPROVE_OPTION)
    {
      textField.setText(fc.getSelectedFile().toString().replace('\\', '/'));
    }

    return fcReturnState;
  }

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    datasetPane = new javax.swing.JPanel();
    dataset1Pane = new javax.swing.JPanel();
    dataset1Label = new javax.swing.JLabel();
    dataset1List = new javax.swing.JComboBox();
    browse1Button = new javax.swing.JButton();
    by11Label = new javax.swing.JLabel();
    by11List = new javax.swing.JComboBox();
    by12Label = new javax.swing.JLabel();
    by12List = new javax.swing.JComboBox();
    by13Label = new javax.swing.JLabel();
    by13List = new javax.swing.JComboBox();
    byall1Check = new javax.swing.JCheckBox();
    suffix1Label = new javax.swing.JLabel();
    suffix1Text = new javax.swing.JTextField();
    dataset2Pane = new javax.swing.JPanel();
    dataset2Label = new javax.swing.JLabel();
    dataset2List = new javax.swing.JComboBox();
    browse2Button = new javax.swing.JButton();
    by21Label = new javax.swing.JLabel();
    by21List = new javax.swing.JComboBox();
    by22Label = new javax.swing.JLabel();
    by22List = new javax.swing.JComboBox();
    by23Label = new javax.swing.JLabel();
    by23List = new javax.swing.JComboBox();
    byall2Check = new javax.swing.JCheckBox();
    suffix2Label = new javax.swing.JLabel();
    suffix2Text = new javax.swing.JTextField();
    savePane = new javax.swing.JPanel();
    sortCheck = new javax.swing.JCheckBox();
    rSaveLabel = new javax.swing.JLabel();
    rNameText = new javax.swing.JTextField();
    saveCheck = new javax.swing.JCheckBox();
    saveLabel = new javax.swing.JLabel();
    nameText = new javax.swing.JTextField();
    outputBrowse = new javax.swing.JButton();
    buttonPane = new javax.swing.JPanel();
    cancelButton = new javax.swing.JButton();
    submitButton = new javax.swing.JButton();
    helpButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Data Merge");
    setMinimumSize(new java.awt.Dimension(30, 30));

    dataset1Pane.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset 1"));
    dataset1Pane.setMinimumSize(new java.awt.Dimension(30, 30));

    dataset1Label.setLabelFor(dataset1List);
    dataset1Label.setText("Dataset 1:");

    dataset1List.setMinimumSize(new java.awt.Dimension(120, 24));
    dataset1List.setPreferredSize(new java.awt.Dimension(120, 24));
    dataset1List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dataset1ListActionPerformed(evt);
      }
    });

    browse1Button.setText("Browse...");
    browse1Button.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browse1ButtonActionPerformed(evt);
      }
    });

    by11Label.setLabelFor(by11List);
    by11Label.setText("Variable 1:");
    by11Label.setToolTipText("Pick variable from Dataset 1 that corresponds to Variable 1 in Dataset 2.  Necessary if column names do not match.");

    by11List.setPreferredSize(new java.awt.Dimension(120, 24));
    by11List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by11ListActionPerformed(evt);
      }
    });

    by12Label.setLabelFor(by12List);
    by12Label.setText("Variable 2:");
    by12Label.setToolTipText("Pick variable from Dataset 1 that corresponds to Variable 2 in Dataset 2.  Necessary if column names do not match.");

    by12List.setPreferredSize(new java.awt.Dimension(120, 24));
    by12List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by12ListActionPerformed(evt);
      }
    });

    by13Label.setLabelFor(by13List);
    by13Label.setText("Variable 3:");
    by13Label.setToolTipText("Pick variable from Dataset 1 that corresponds to Variable 3 in Dataset 2.  Necessary if column names do not match.");

    by13List.setPreferredSize(new java.awt.Dimension(120, 24));
    by13List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by13ListActionPerformed(evt);
      }
    });

    byall1Check.setSelected(true);
    byall1Check.setText("By All");
    byall1Check.setToolTipText("If selected, then all rows in Dataset 1 will be included, even if there is no corresponding row in Dataset 2.");
    byall1Check.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    byall1Check.setMargin(new java.awt.Insets(0, 0, 0, 0));

    suffix1Label.setText("Suffix: ");
    suffix1Label.setToolTipText("If column names match in Dataset 1 and 2, then this suffix will be added to the column name for Dataset 1.");

    suffix1Text.setText(".1");

    org.jdesktop.layout.GroupLayout dataset1PaneLayout = new org.jdesktop.layout.GroupLayout(dataset1Pane);
    dataset1Pane.setLayout(dataset1PaneLayout);
    dataset1PaneLayout.setHorizontalGroup(
      dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(dataset1PaneLayout.createSequentialGroup()
        .addContainerGap()
        .add(dataset1Label)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset1List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(browse1Button)
        .addContainerGap())
      .add(dataset1PaneLayout.createSequentialGroup()
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
          .add(dataset1PaneLayout.createSequentialGroup()
            .add(47, 47, 47)
            .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(by11Label)
              .add(by12Label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .add(by13Label, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .add(dataset1PaneLayout.createSequentialGroup()
            .addContainerGap()
            .add(suffix1Label)))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(dataset1PaneLayout.createSequentialGroup()
            .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(by13List, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .add(by12List, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .add(by11List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
              .add(dataset1PaneLayout.createSequentialGroup()
                .add(byall1Check)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(70, 70, 70))
          .add(dataset1PaneLayout.createSequentialGroup()
            .add(suffix1Text, 0, 0, Short.MAX_VALUE)
            .addContainerGap())))
    );

    dataset1PaneLayout.linkSize(new java.awt.Component[] {by11Label, by12Label, by13Label}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

    dataset1PaneLayout.linkSize(new java.awt.Component[] {by11List, by12List, by13List}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

    dataset1PaneLayout.linkSize(new java.awt.Component[] {byall1Check, suffix1Text}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

    dataset1PaneLayout.setVerticalGroup(
      dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(dataset1PaneLayout.createSequentialGroup()
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(dataset1Label)
          .add(dataset1List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(browse1Button))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by11Label)
          .add(by11List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(7, 7, 7)
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by12Label)
          .add(by12List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by13List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(by13Label))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(byall1Check)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset1PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(suffix1Text, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(suffix1Label))
        .addContainerGap(25, Short.MAX_VALUE))
    );

    dataset2Pane.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset 2"));
    dataset2Pane.setMinimumSize(new java.awt.Dimension(30, 30));

    dataset2Label.setText("Dataset 2:");

    dataset2List.setPreferredSize(new java.awt.Dimension(120, 24));
    dataset2List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dataset2ListActionPerformed(evt);
      }
    });

    browse2Button.setText("Browse...");
    browse2Button.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browse2ButtonActionPerformed(evt);
      }
    });

    by21Label.setLabelFor(by21List);
    by21Label.setText("Variable 1:");
    by21Label.setToolTipText("Pick variable from Dataset 2 that corresponds to Variable 1 in Dataset 1.  Necessary if column names do not match.");

    by21List.setPreferredSize(new java.awt.Dimension(120, 24));
    by21List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by21ListActionPerformed(evt);
      }
    });

    by22Label.setLabelFor(by22List);
    by22Label.setText("Variable 2:");
    by22Label.setToolTipText("Pick Variable from Dataset 2 that corresponds to Variable2 from Dataset 1.  Necessary if column names do not match.");

    by22List.setPreferredSize(new java.awt.Dimension(120, 24));
    by22List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by22ListActionPerformed(evt);
      }
    });

    by23Label.setLabelFor(by23List);
    by23Label.setText("Variable 3:");
    by23Label.setToolTipText("Pick variable from Dataset 2 that corresponds to Variable 3 in Dataset 1.  Necessary if column names do not match.");

    by23List.setPreferredSize(new java.awt.Dimension(120, 24));
    by23List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        by23ListActionPerformed(evt);
      }
    });

    byall2Check.setSelected(true);
    byall2Check.setText("By All");
    byall2Check.setToolTipText("If selected, then all rows in Dataset 2 will be included, even if there is no corresponding row in Dataset 1.");
    byall2Check.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    byall2Check.setMargin(new java.awt.Insets(0, 0, 0, 0));

    suffix2Label.setText("Suffix:");
    suffix2Label.setToolTipText("If column names match in Dataset 1 and 2, then this suffix will be added to the column name for Dataset 2.");

    suffix2Text.setText(".2");

    org.jdesktop.layout.GroupLayout dataset2PaneLayout = new org.jdesktop.layout.GroupLayout(dataset2Pane);
    dataset2Pane.setLayout(dataset2PaneLayout);
    dataset2PaneLayout.setHorizontalGroup(
      dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(dataset2PaneLayout.createSequentialGroup()
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(dataset2PaneLayout.createSequentialGroup()
            .addContainerGap()
            .add(dataset2Label)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(dataset2List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(browse2Button))
          .add(dataset2PaneLayout.createSequentialGroup()
            .add(51, 51, 51)
            .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, suffix2Label)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, by23Label)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, by22Label)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, by21Label))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(suffix2Text, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
              .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(by23List, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(by21List, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(dataset2PaneLayout.createSequentialGroup()
                  .add(byall2Check)
                  .add(67, 67, 67))
                .add(by22List, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    dataset2PaneLayout.linkSize(new java.awt.Component[] {byall2Check, suffix2Text}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

    dataset2PaneLayout.setVerticalGroup(
      dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(dataset2PaneLayout.createSequentialGroup()
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(dataset2Label)
          .add(dataset2List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(browse2Button))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by21List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(by21Label))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by22Label)
          .add(by22List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(by23List, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(by23Label))
        .add(7, 7, 7)
        .add(byall2Check)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset2PaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(suffix2Label)
          .add(suffix2Text, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(26, Short.MAX_VALUE))
    );

    org.jdesktop.layout.GroupLayout datasetPaneLayout = new org.jdesktop.layout.GroupLayout(datasetPane);
    datasetPane.setLayout(datasetPaneLayout);
    datasetPaneLayout.setHorizontalGroup(
      datasetPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(datasetPaneLayout.createSequentialGroup()
        .add(dataset1Pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(dataset2Pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );
    datasetPaneLayout.setVerticalGroup(
      datasetPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(dataset2Pane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .add(dataset1Pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
    );

    savePane.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Options"));

    sortCheck.setText("Sort Output Data");
    sortCheck.setToolTipText("If selected, merged data will be sorted based on the columns used for merging.  If not selected, rows remain in order of dataset 1, with non-intersecting rows coming at the end, dataset 1 before dataset 2.");
    sortCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    sortCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));

    rSaveLabel.setText("Local Name:");
    rSaveLabel.setToolTipText("This sets the variable name in R (and thus how the name will appear in the dataset lists).");

    rNameText.setText("mergedData");
    rNameText.setPreferredSize(new java.awt.Dimension(120, 20));

    saveCheck.setSelected(true);
    saveCheck.setText("Save to File");
    saveCheck.setToolTipText("If checked, the merged dataset will be written out as a tab-delimited text file to the location specified below.");
    saveCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    saveCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
    saveCheck.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveCheckActionPerformed(evt);
      }
    });

    saveLabel.setText("Save filename:");
    saveLabel.setToolTipText("Name (and path) under which to save the merged file.  A .txt extension is recommended, since the output will be tab-delimited text.");

    nameText.setText("mergedData.txt");
    nameText.setPreferredSize(new java.awt.Dimension(120, 20));

    outputBrowse.setText("Browse...");
    outputBrowse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        outputBrowseActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout savePaneLayout = new org.jdesktop.layout.GroupLayout(savePane);
    savePane.setLayout(savePaneLayout);
    savePaneLayout.setHorizontalGroup(
      savePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(savePaneLayout.createSequentialGroup()
        .addContainerGap()
        .add(savePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(savePaneLayout.createSequentialGroup()
            .add(17, 17, 17)
            .add(saveLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(nameText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(outputBrowse)
            .addContainerGap())
          .add(savePaneLayout.createSequentialGroup()
            .add(rSaveLabel)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(rNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
          .add(savePaneLayout.createSequentialGroup()
            .add(sortCheck, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .add(218, 218, 218))
          .add(savePaneLayout.createSequentialGroup()
            .add(saveCheck)
            .addContainerGap(306, Short.MAX_VALUE))))
    );
    savePaneLayout.setVerticalGroup(
      savePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(savePaneLayout.createSequentialGroup()
        .add(savePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(rSaveLabel)
          .add(rNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sortCheck)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(saveCheck)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(savePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(saveLabel)
          .add(outputBrowse)
          .add(nameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(19, Short.MAX_VALUE))
    );

    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    submitButton.setText("Submit");
    submitButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        submitButtonActionPerformed(evt);
      }
    });

    helpButton.setText("Help");
    helpButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        helpButtonActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout buttonPaneLayout = new org.jdesktop.layout.GroupLayout(buttonPane);
    buttonPane.setLayout(buttonPaneLayout);
    buttonPaneLayout.setHorizontalGroup(
      buttonPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(buttonPaneLayout.createSequentialGroup()
        .add(210, 210, 210)
        .add(cancelButton)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(submitButton)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(helpButton)
        .addContainerGap(217, Short.MAX_VALUE))
    );
    buttonPaneLayout.setVerticalGroup(
      buttonPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(buttonPaneLayout.createSequentialGroup()
        .addContainerGap()
        .add(buttonPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(submitButton)
          .add(helpButton)
          .add(cancelButton))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(datasetPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
          .add(layout.createSequentialGroup()
            .add(128, 128, 128)
            .add(savePane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .add(buttonPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(datasetPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(savePane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
        .add(buttonPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void saveCheckActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveCheckActionPerformed
  {//GEN-HEADEREND:event_saveCheckActionPerformed
     saveLabel.setEnabled(saveCheck.isSelected());
     nameText.setEnabled(saveCheck.isSelected());
     outputBrowse.setEnabled(saveCheck.isSelected());
  }//GEN-LAST:event_saveCheckActionPerformed

  private void outputBrowseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_outputBrowseActionPerformed
  {//GEN-HEADEREND:event_outputBrowseActionPerformed
    setTextUsingFileChooser(nameText, JFileChooser.FILES_ONLY);
  }//GEN-LAST:event_outputBrowseActionPerformed

  private void by23ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by23ListActionPerformed
  {//GEN-HEADEREND:event_by23ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by23ListActionPerformed

  private void by22ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by22ListActionPerformed
  {//GEN-HEADEREND:event_by22ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by22ListActionPerformed

  private void by21ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by21ListActionPerformed
  {//GEN-HEADEREND:event_by21ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by21ListActionPerformed

  private void by13ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by13ListActionPerformed
  {//GEN-HEADEREND:event_by13ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by13ListActionPerformed

  private void by12ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by12ListActionPerformed
  {//GEN-HEADEREND:event_by12ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by12ListActionPerformed

  private void by11ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_by11ListActionPerformed
  {//GEN-HEADEREND:event_by11ListActionPerformed
    updateValidity();
  }//GEN-LAST:event_by11ListActionPerformed

  private void dataset2ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dataset2ListActionPerformed
  {//GEN-HEADEREND:event_dataset2ListActionPerformed
    refreshBy2List();
  }//GEN-LAST:event_dataset2ListActionPerformed

  private void browse2ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browse2ButtonActionPerformed
  {//GEN-HEADEREND:event_browse2ButtonActionPerformed
    int dataLength = getDataset2List().getItemCount();
    String[] datasets = new String[dataLength];
    for (int i=0, n=getDataset2List().getItemCount(); i < n; ++i)
    {
      datasets[i] = getDataset2List().getItemAt(i).toString();
    }
    String workingDir = RUtils.evalAsString("getwd()");
    JGRDataFileOpenDialog fileDialog = new JGRDataFileOpenDialog(JGR.MAINRCONSOLE, workingDir);
    fileDialog.grabFocus();
    
    String keep2 = "", pkeep;
    Boolean newone;
    Object[] newdatasets = RUtils.getDatasetList();
    for (int i=0, n=newdatasets.length; i < n; ++i)
    {
      newone = true;
      pkeep = newdatasets[i].toString();
      for (int j=0; j < dataLength; j++)
      {
        if (pkeep.equals(datasets[j]))
        {
          newone = false;
          break;
        }
      }
      if ( newone )
      {
        keep2 = pkeep;
        break;
      }
    }
    refreshDatasetLists(2, (isDatasetListValid(1)? getDataset1(): ""), keep2);
    this.dataset2List.grabFocus();
  }//GEN-LAST:event_browse2ButtonActionPerformed

    private void browse1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browse1ButtonActionPerformed
    {//GEN-HEADEREND:event_browse1ButtonActionPerformed
      int dataLength = getDataset1List().getItemCount();
      String[] datasets = new String[dataLength];
      for (int i=0, n=getDataset1List().getItemCount(); i < n; ++i)
      {
        datasets[i] = getDataset1List().getItemAt(i).toString();
      }
      String workingDir = RUtils.evalAsString("getwd()");
      JGRDataFileOpenDialog fileDialog = new JGRDataFileOpenDialog(JGR.MAINRCONSOLE, workingDir);
      fileDialog.grabFocus();

      String keep1 = "", pkeep;
      Boolean newone;
      Object[] newdatasets = RUtils.getDatasetList();
      for (int i=0, n=newdatasets.length; i < n; ++i)
      {
        newone = true;
        pkeep = newdatasets[i].toString();
        for (int j=0; j < dataLength; j++)
        {
          if (pkeep.equals(datasets[j]))
          {
            newone = false;
            break;
          }
        }
        if ( newone )
        {
          keep1 = pkeep;
          break;
        }
      }
      refreshDatasetLists(1, keep1, (isDatasetListValid(2)? getDataset2(): ""));
      this.dataset1List.grabFocus();
    }//GEN-LAST:event_browse1ButtonActionPerformed
  
  private void dataset1ListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dataset1ListActionPerformed
  {//GEN-HEADEREND:event_dataset1ListActionPerformed
    refreshBy1List();
  }//GEN-LAST:event_dataset1ListActionPerformed

private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
  String cmd = "datamerge.JGR("
    + "x=" + getDataset1()
    + ", y=" + getDataset2()
    + ", by11=\"" + getSelectBy11List() + "\""
    + ", by12=\"" + getSelectBy12List() + "\""
    + ", by13=\"" + getSelectBy13List() + "\""
    + ", by21=\"" + getSelectBy21List() + "\""
    + ", by22=\"" + getSelectBy22List() + "\""
    + ", by23=\"" + getSelectBy23List() + "\""
    + ", all.x=" + String.valueOf(byall1Check.isSelected()).toUpperCase()
    + ", all.y=" + String.valueOf(byall2Check.isSelected()).toUpperCase()
    + ", suffixes=c(" + suffix1Text.getText() + "," + suffix2Text.getText() + ")"
    + ", rName=\"" + rNameText.getText() + "\""
    + ", sort=" + String.valueOf(sortCheck.isSelected()).toUpperCase()
    + ", writeout=" + String.valueOf(saveCheck.isSelected()).toUpperCase()
    + ", outName=\"" + nameText.getText() + "\"" + ")";
  JGR.MAINRCONSOLE.execute(cmd, true);
}//GEN-LAST:event_submitButtonActionPerformed

private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
  String cmd = "CADStat.help('datamerge.JGR')";
  JGR.MAINRCONSOLE.execute(cmd, true);
}//GEN-LAST:event_helpButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
  this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton browse1Button;
  private javax.swing.JButton browse2Button;
  private javax.swing.JPanel buttonPane;
  private javax.swing.JLabel by11Label;
  private javax.swing.JComboBox by11List;
  private javax.swing.JLabel by12Label;
  private javax.swing.JComboBox by12List;
  private javax.swing.JLabel by13Label;
  private javax.swing.JComboBox by13List;
  private javax.swing.JLabel by21Label;
  private javax.swing.JComboBox by21List;
  private javax.swing.JLabel by22Label;
  private javax.swing.JComboBox by22List;
  private javax.swing.JLabel by23Label;
  private javax.swing.JComboBox by23List;
  private javax.swing.JCheckBox byall1Check;
  private javax.swing.JCheckBox byall2Check;
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel dataset1Label;
  private javax.swing.JComboBox dataset1List;
  private javax.swing.JPanel dataset1Pane;
  private javax.swing.JLabel dataset2Label;
  private javax.swing.JComboBox dataset2List;
  private javax.swing.JPanel dataset2Pane;
  private javax.swing.JPanel datasetPane;
  private javax.swing.JButton helpButton;
  private javax.swing.JTextField nameText;
  private javax.swing.JButton outputBrowse;
  private javax.swing.JTextField rNameText;
  private javax.swing.JLabel rSaveLabel;
  private javax.swing.JCheckBox saveCheck;
  private javax.swing.JLabel saveLabel;
  private javax.swing.JPanel savePane;
  private javax.swing.JCheckBox sortCheck;
  private javax.swing.JButton submitButton;
  private javax.swing.JLabel suffix1Label;
  private javax.swing.JTextField suffix1Text;
  private javax.swing.JLabel suffix2Label;
  private javax.swing.JTextField suffix2Text;
  // End of variables declaration//GEN-END:variables
  
  public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent)
  {
    updateValidity();
  }

}

