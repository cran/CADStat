/*
 * QuantileRegression.java
 *
 * Created on October 7, 2005, 9:26 AM
 */

package org.neptuneinc.cadstat.plots;

import javax.swing.event.TableModelEvent;
import org.neptuneinc.cadstat.ui.DataPlotDialog;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.neptuneinc.cadstat.utils.GUIUtils;
import org.neptuneinc.cadstat.utils.RUtils;
import org.rosuda.JGR.JGR;

/**
 *
 * @author  Pasha Minallah
 */
public class QuantileRegression extends DataPlotDialog
{
  /**
   * Creates new form QuantileRegression
   */
  public QuantileRegression()
  {
    super();
  }

  /** Perform custom initialization. */
  @Override
  protected void initCustom()
  {
    this.initComponents();
    
    this.getPlotPane().add(plotPane, BorderLayout.CENTER);

    this.getDatasetPane().getDatasetComboBox().addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        datasetComboBoxActionPerformed(e);
      }
    });
    
    quantileTable.getModel().addTableModelListener(new TableModelListener()
    {
      public void tableChanged(TableModelEvent e)
      {
        quantileTableTableChanged(e);
      }
    });

    this.refreshVariableList();
    this.refreshValidity();
    this.updateModelEquation();
  }

  private void datasetComboBoxActionPerformed(ActionEvent e)
  {
    this.refreshVariableList();
    this.refreshValidity();
    this.updateModelEquation();
  }
  
  private void quantileTableTableChanged(TableModelEvent e)
  {
    int row = e.getFirstRow();
    int col = e.getColumn();

    if (row == 0 && col == 0)
    {
      TableModel model = (TableModel) e.getSource();

      if (model.getValueAt(row, col) == null)
        model.setValueAt(new Double(0.5), row, col);
    }

    this.refreshValidity();
  }

  public void updateModelEquation()
  {
    if (this.getDatasetPane().getDatasetComboBox().getItemCount() > 0)
    {
      String modelEq = dependentComboBox.getSelectedItem() + " ~ " +
          RUtils.toString(independentList.getSelectedValues(), " + ", "");
      
      modelEqTextArea.setText(modelEq);
    }
    
    else
    {
      modelEqTextArea.setText(null);
    }
  }

  public void refreshVariableListValidity()
  {
    dependentLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && dependentComboBox.getItemCount() > 0);
    dependentComboBox.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && dependentComboBox.getItemCount() > 0);
    independentLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && independentList.getModel().getSize() > 0);
    independentList.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && independentList.getModel().getSize() > 0);
  }

  public void refreshVariableList()
  {
    if (this.getDatasetPane().getDatasetComboBox().getItemCount() > 0)
    {
      Vector nonFactors = RUtils.nonFactors(this.getDatasetPane().getSelectedDataset());
      
      dependentComboBox.setModel(new DefaultComboBoxModel(nonFactors));
      independentList.setListData(nonFactors);

      if (dependentComboBox.getItemCount() > 0)
      {
        dependentComboBox.setSelectedIndex(0);
      }
    }

    else
    {
      dependentComboBox.removeAllItems();
      independentList.setModel(new DefaultListModel());
    }

    this.refreshValidity();
  }
  
  public void refreshConfLevelValidity()
  {
    confLabel.setEnabled(confIntervals.isSelected());
    confLevel.setEnabled(confIntervals.isSelected());
  }
  
  public void refreshScatterPlotValidity()
  {
    regScatterPlot.setEnabled(!independentList.isSelectionEmpty() && independentList.getSelectedIndices().length == 1);
  }
  
  public void refreshConfBandValidity()
  {
    TableModel model = quantileTable.getModel();
    int numValues = 0;

    for (int i = 0, n = model.getRowCount(); i < n; ++i)
    {
      if (model.getValueAt(i, 0) != null)
        ++numValues;
    }

    boolean enableConfBands = numValues >= 1 && !independentList.isSelectionEmpty() && independentList.getSelectedIndices().length == 1;
    plotCoeffConfBands.setEnabled(enableConfBands);
  }
  
  public void refreshSubmitButtonValidity()
  {
    this.getSubmitButton().setEnabled(!independentList.isSelectionEmpty());
  }

  public void refreshValidity()
  {
    this.refreshVariableListValidity();
    this.refreshConfLevelValidity();
    this.refreshConfBandValidity();
    this.refreshScatterPlotValidity();
    this.refreshSubmitButtonValidity();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plotPane = new javax.swing.JPanel();
        plotOptionsPane = new javax.swing.JPanel();
        regScatterPlot = new javax.swing.JCheckBox();
        plotCoeffConfBands = new javax.swing.JCheckBox();
        modelEqPane = new javax.swing.JPanel();
        modelEqScrollPane = new javax.swing.JScrollPane();
        modelEqTextArea = new javax.swing.JTextArea();
        varPane = new javax.swing.JPanel();
        dependentLabel = new javax.swing.JLabel();
        dependentComboBox = new javax.swing.JComboBox();
        independentLabel = new javax.swing.JLabel();
        independentScrollPane = new javax.swing.JScrollPane();
        independentList = new javax.swing.JList();
        analysisOptionsPane = new javax.swing.JPanel();
        confIntervals = new javax.swing.JCheckBox();
        confLabel = new javax.swing.JLabel();
        confLevel = new javax.swing.JSpinner();
        quantilePane = new javax.swing.JPanel();
        quantileScrollPane = new javax.swing.JScrollPane();
        quantileTable = new javax.swing.JTable();
        savePanel = new javax.swing.JPanel();
        browserResults = new javax.swing.JCheckBox();

        plotOptionsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Options"));

        regScatterPlot.setText("Regression Scatterplot");

        plotCoeffConfBands.setText("Coefficient Confidence Bands");

        org.jdesktop.layout.GroupLayout plotOptionsPaneLayout = new org.jdesktop.layout.GroupLayout(plotOptionsPane);
        plotOptionsPane.setLayout(plotOptionsPaneLayout);
        plotOptionsPaneLayout.setHorizontalGroup(
            plotOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(regScatterPlot)
                    .add(plotCoeffConfBands))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        plotOptionsPaneLayout.setVerticalGroup(
            plotOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(regScatterPlot)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotCoeffConfBands)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        modelEqPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Model Equation"));

        modelEqTextArea.setColumns(20);
        modelEqTextArea.setEditable(false);
        modelEqTextArea.setLineWrap(true);
        modelEqTextArea.setRows(2);
        modelEqTextArea.setWrapStyleWord(true);
        modelEqScrollPane.setViewportView(modelEqTextArea);

        org.jdesktop.layout.GroupLayout modelEqPaneLayout = new org.jdesktop.layout.GroupLayout(modelEqPane);
        modelEqPane.setLayout(modelEqPaneLayout);
        modelEqPaneLayout.setHorizontalGroup(
            modelEqPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, modelEqPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );
        modelEqPaneLayout.setVerticalGroup(
            modelEqPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(modelEqPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        varPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));

        dependentLabel.setText("Dependent:");

        dependentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dependentComboBoxActionPerformed(evt);
            }
        });

        independentLabel.setText("Independent:");

        independentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                independentListValueChanged(evt);
            }
        });
        independentScrollPane.setViewportView(independentList);

        org.jdesktop.layout.GroupLayout varPaneLayout = new org.jdesktop.layout.GroupLayout(varPane);
        varPane.setLayout(varPaneLayout);
        varPaneLayout.setHorizontalGroup(
            varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(varPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(independentLabel)
                    .add(dependentLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dependentComboBox, 0, 173, Short.MAX_VALUE))
                .addContainerGap())
        );
        varPaneLayout.setVerticalGroup(
            varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(varPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dependentComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dependentLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(independentLabel)
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addContainerGap())
        );

        analysisOptionsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Options"));

        confIntervals.setText("Compute Confidence Intervals");
        confIntervals.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                confIntervalsStateChanged(evt);
            }
        });

        confLabel.setText("Confidence Level:");

        confLevel.setModel(new javax.swing.SpinnerNumberModel(0.95d, 0.01d, 0.99d, 0.01d));

        org.jdesktop.layout.GroupLayout analysisOptionsPaneLayout = new org.jdesktop.layout.GroupLayout(analysisOptionsPane);
        analysisOptionsPane.setLayout(analysisOptionsPaneLayout);
        analysisOptionsPaneLayout.setHorizontalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(confIntervals)
                    .add(analysisOptionsPaneLayout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(confLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(confLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        analysisOptionsPaneLayout.setVerticalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(confIntervals)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(confLabel)
                    .add(confLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        quantilePane.setBorder(javax.swing.BorderFactory.createTitledBorder("Quantiles"));

        quantileScrollPane.setPreferredSize(new java.awt.Dimension(200, 120));

        quantileTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {new Double(0.5)},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Quantiles"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        quantileTable.setCellSelectionEnabled(true);
        quantileScrollPane.setViewportView(quantileTable);

        org.jdesktop.layout.GroupLayout quantilePaneLayout = new org.jdesktop.layout.GroupLayout(quantilePane);
        quantilePane.setLayout(quantilePaneLayout);
        quantilePaneLayout.setHorizontalGroup(
            quantilePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(quantilePaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(quantileScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        quantilePaneLayout.setVerticalGroup(
            quantilePaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(quantilePaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(quantileScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        savePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        browserResults.setText("Display Results in Browser");
        browserResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserResults.setMargin(new java.awt.Insets(0, 0, 0, 0));
        browserResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserResultsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout savePanelLayout = new org.jdesktop.layout.GroupLayout(savePanel);
        savePanel.setLayout(savePanelLayout);
        savePanelLayout.setHorizontalGroup(
            savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(savePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(browserResults)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        savePanelLayout.setVerticalGroup(
            savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(savePanelLayout.createSequentialGroup()
                .add(browserResults)
                .addContainerGap(5, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout plotPaneLayout = new org.jdesktop.layout.GroupLayout(plotPane);
        plotPane.setLayout(plotPaneLayout);
        plotPaneLayout.setHorizontalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, modelEqPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, plotPaneLayout.createSequentialGroup()
                            .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(varPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(plotOptionsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(quantilePane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        plotPaneLayout.setVerticalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .add(modelEqPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(quantilePane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(varPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(plotOptionsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setTitle("Quantile Regression");

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-580)/2, (screenSize.height-625)/2, 580, 625);
    }// </editor-fold>//GEN-END:initComponents

  private void confIntervalsStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_confIntervalsStateChanged
  {//GEN-HEADEREND:event_confIntervalsStateChanged
    this.refreshValidity();
  }//GEN-LAST:event_confIntervalsStateChanged

private void dependentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dependentComboBoxActionPerformed
  this.updateModelEquation();
}//GEN-LAST:event_dependentComboBoxActionPerformed

private void independentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_independentListValueChanged
  this.updateModelEquation();
  this.refreshValidity();
}//GEN-LAST:event_independentListValueChanged

private void browserResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserResultsActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_browserResultsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel analysisOptionsPane;
    private javax.swing.JCheckBox browserResults;
    private javax.swing.JCheckBox confIntervals;
    private javax.swing.JLabel confLabel;
    private javax.swing.JSpinner confLevel;
    private javax.swing.JComboBox dependentComboBox;
    private javax.swing.JLabel dependentLabel;
    private javax.swing.JLabel independentLabel;
    private javax.swing.JList independentList;
    private javax.swing.JScrollPane independentScrollPane;
    private javax.swing.JPanel modelEqPane;
    private javax.swing.JScrollPane modelEqScrollPane;
    private javax.swing.JTextArea modelEqTextArea;
    private javax.swing.JCheckBox plotCoeffConfBands;
    private javax.swing.JPanel plotOptionsPane;
    private javax.swing.JPanel plotPane;
    private javax.swing.JPanel quantilePane;
    private javax.swing.JScrollPane quantileScrollPane;
    private javax.swing.JTable quantileTable;
    private javax.swing.JCheckBox regScatterPlot;
    private javax.swing.JPanel savePanel;
    private javax.swing.JPanel varPane;
    // End of variables declaration//GEN-END:variables

  private List getColumn(JTable table, int column)
  {
    if (table != null && column <= table.getColumnCount())
    {
      List list = new ArrayList();

      for (int i = 0, n = table.getRowCount(); i < n; ++i)
      {
        list.add(table.getValueAt(i, column));
      }

      return (list);
    }

    else
    {
      return (null);
    }
  }

  @Override
  protected void submitButtonAction()
  {
    String cmd = "rq.JGR("
      + "my.data=" + this.getDatasetPane().getSelectedDataset()
      + ", subset1.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane1().getSelectedFactor() + "'" )
      + ", subset1.val="  + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'") + ")")
      + ", subset2.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane2().getSelectedFactor() + "'" )
      + ", subset2.val="  + (RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'") + ")")
      + ", my.formula=" + "'" + modelEqTextArea.getText() + "'"
      + ", my.tau=" + "c(" + RUtils.toString(getColumn(quantileTable, 0), ",", "'") + ")"
      + ", iCI=" + String.valueOf(confIntervals.isSelected()).toUpperCase()
      + (confIntervals.isSelected() ? ", conf.level=" + ((Double) confLevel.getValue()).doubleValue() : "")
      + (regScatterPlot.isEnabled() ? ", iScatterplot=" + String.valueOf(regScatterPlot.isSelected()).toUpperCase() : "")
      + ", iCIplot=" + String.valueOf(plotCoeffConfBands.isSelected()).toUpperCase()
      + ", dep.var=" + "'" + GUIUtils.getSelectedItem(dependentComboBox) + "'"
      + ", indep.var=" + "c(" + RUtils.toString(independentList.getSelectedValues(), ",", "'") + ")"
      + ", browserResults=" + String.valueOf(browserResults.isSelected()).toUpperCase()      
      + ")";
    JGR.MAINRCONSOLE.execute(cmd, true);
  }

  @Override
  protected void helpButtonAction()
  {
    String cmd = "CADStat.help('rq.JGR')";
    JGR.MAINRCONSOLE.execute(cmd, true);
//    JGR.MAINRCONSOLE.help("QuantileRegression");
  }

}
