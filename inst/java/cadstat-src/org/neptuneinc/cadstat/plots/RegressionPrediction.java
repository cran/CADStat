/*
 * LinearRegression.java
 *
 * Created on September 20, 2005, 8:05 PM
 */

package org.neptuneinc.cadstat.plots;

import org.neptuneinc.cadstat.ui.DataPlotDialog;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import org.neptuneinc.cadstat.utils.GUIUtils;
import org.neptuneinc.cadstat.utils.RUtils;
import org.rosuda.JGR.JGR;

/**
 *
 * @author  Pasha Minallah
 */
public class RegressionPrediction extends DataPlotDialog
{
  private String modelString;
  
  /**
   * Creates new form LinearRegression
   */
  public RegressionPrediction()
  {
    super();
  }

  /** Perform custom initialization. */
  @Override
  protected void initCustom()
  {
    this.initComponents();

    getIdVarList().setModel(new DefaultComboBoxModel());
    getIdVarList().insertItemAt("",0);
    getIdVarList().setSelectedIndex(0);

    this.getPlotPane().add(plotPane, BorderLayout.CENTER);

    this.getDatasetPane().getDatasetComboBox().addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        datasetComboBoxActionPerformed(e);
      }
    });
    
    this.refreshVariableList();
    this.refreshSiteVarList();
    this.refreshIdVarList();
    this.refreshValidity();
    this.updateModelEquation();
  }
  
  private void datasetComboBoxActionPerformed(ActionEvent e)
  {
    this.refreshVariableList();
    this.refreshSiteVarList();
    this.refreshIdVarList();
    this.refreshValidity();
    this.updateModelEquation();
  }

  public void refreshDistValidity()
  {
    Enumeration<AbstractButton> distButtonEnum = distButtonGroup.getElements();
    while (distButtonEnum.hasMoreElements())
    {
      distButtonEnum.nextElement().setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0);
    }
  }

  public void refreshModelValidity()
  {
    this.refreshDistValidity();
    this.refreshVariableListValidity();
  }

  public void updateModelEquation()
  {
    if (this.getDatasetPane().getDatasetComboBox().getItemCount() > 0)
    {
      String modelEq = dependentComboBox.getSelectedItem() + " ~ " +
          RUtils.toString(independentList.getSelectedValues(), " + ", "");
      
      modelEqTextArea.setText(modelEq);
      
      if(binomDistButton.isSelected())
      {
        this.setModelString(sampleSizeComboBox.getSelectedItem() + "," +
          dependentComboBox.getSelectedItem() + "," + RUtils.toString(independentList.getSelectedValues(), ",", ""));
      }

      else
      {
        this.setModelString(modelEq);
      }
    }
    
    else
    {
      this.setModelString(null);
      modelEqTextArea.setText(null);
    }
  }

  public void refreshVariableListValidity()
  {
    dependentLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && dependentComboBox.getItemCount() > 0);
    dependentComboBox.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && dependentComboBox.getItemCount() > 0);
    independentLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && independentList.getModel().getSize() > 0);
    independentList.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && independentList.getModel().getSize() > 0);
    sampleSizeLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && sampleSizeComboBox.getItemCount() > 0 && binomDistButton.isSelected());
    sampleSizeComboBox.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && sampleSizeComboBox.getItemCount() > 0 && binomDistButton.isSelected());
    
    siteVarLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && siteVarList.getItemCount() > 0);
    siteVarList.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && siteVarList.getItemCount() > 0);
    siteIDLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && siteIDValList.getModel().getSize() > 0);
    siteIDValList.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && siteIDValList.getModel().getSize() > 0);
    idVarLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && idVarList.getItemCount() > 0);
    idVarList.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && idVarList.getItemCount() > 0);
  }

  public void refreshVariableList()
  {
    if (this.getDatasetPane().getDatasetComboBox().getItemCount() > 0)
    {
      Vector nonFactors = RUtils.nonFactors(this.getDatasetPane().getSelectedDataset());
      Vector onePlusNonFactors = new Vector(nonFactors.size() + 1);
      onePlusNonFactors.add("1");
      onePlusNonFactors.addAll(nonFactors);
      
      dependentComboBox.setModel(new DefaultComboBoxModel(nonFactors));
      independentList.setListData(RUtils.colnamesVector(this.getDatasetPane().getSelectedDataset()));
      sampleSizeComboBox.setModel(new DefaultComboBoxModel(onePlusNonFactors));

      if (dependentComboBox.getItemCount() > 0)
      {
        dependentComboBox.setSelectedIndex(0);
      }

      if (sampleSizeComboBox.getItemCount() > 0)
      {
        sampleSizeComboBox.setSelectedIndex(0);
      }
    }

    else
    {
      dependentComboBox.removeAllItems();
      independentList.setModel(new DefaultListModel());
      sampleSizeComboBox.removeAllItems();
    }

    this.refreshValidity();
  }

  private void refreshSiteVarList()
  {
    JComboBox colList = getSiteVarList();
    
    String dataset = this.getDatasetPane().getSelectedDataset();
    
    if (dataset != null)
    {
      Vector factors = RUtils.factors(dataset);

      colList.removeAllItems();
      colList.setModel(new DefaultComboBoxModel(factors));
    }

    refreshSiteValList();
  }
  
  private void refreshSiteValList()
  {
    String dataset = this.getDatasetPane().getSelectedDataset();
    Vector v;
    
    if (dataset != null && siteVarList.getItemCount() > 0)
    {
      // Selected column name
      String col = (String) siteVarList.getSelectedItem();

      // Command for unique values
      String cmd = "unique(" + dataset + "$" + col + ")";
      
      // Evaluate command as list
      v = RUtils.evalAsVector(cmd);
      siteIDValList.setListData(v);
      siteIDValList.setSelectedIndex(0);
    }
    else
    {
      v = new Vector(0);
      siteIDValList.setListData(v);
    }
    
    this.refreshValidity();
  }
  
  public void refreshSubmitButtonValidity()
  {
    this.getSubmitButton().setEnabled(siteVarList.getSelectedIndex() >= 0 && siteIDValList.getMaxSelectionIndex() >= 0 && !independentList.isSelectionEmpty());
  }

  public void refreshValidity()
  {
    this.refreshModelValidity();
    this.refreshSubmitButtonValidity();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        distButtonGroup = new javax.swing.ButtonGroup();
        plotPane = new javax.swing.JPanel();
        distPane = new javax.swing.JPanel();
        normDistButton = new javax.swing.JRadioButton();
        poisDistButton = new javax.swing.JRadioButton();
        binomDistButton = new javax.swing.JRadioButton();
        modelEqPane = new javax.swing.JPanel();
        modelEqScrollPane = new javax.swing.JScrollPane();
        modelEqTextArea = new javax.swing.JTextArea();
        varPane = new javax.swing.JPanel();
        dependentLabel = new javax.swing.JLabel();
        dependentComboBox = new javax.swing.JComboBox();
        independentLabel = new javax.swing.JLabel();
        independentScrollPane = new javax.swing.JScrollPane();
        independentList = new javax.swing.JList();
        sampleSizeLabel = new javax.swing.JLabel();
        sampleSizeComboBox = new javax.swing.JComboBox();
        siteSelPane = new javax.swing.JPanel();
        siteVarLabel = new javax.swing.JLabel();
        siteVarList = new javax.swing.JComboBox();
        siteIDLabel = new javax.swing.JLabel();
        siteIDValScrollPane = new javax.swing.JScrollPane();
        siteIDValList = new javax.swing.JList();
        idPane = new javax.swing.JPanel();
        idVarLabel = new javax.swing.JLabel();
        idVarList = new javax.swing.JComboBox();
        analysisOptionsPane = new javax.swing.JPanel();
        sigLabel = new javax.swing.JLabel();
        sigLevel = new javax.swing.JSpinner();
        rmIntercept = new javax.swing.JCheckBox();
        savePanel = new javax.swing.JPanel();
        browserResults = new javax.swing.JCheckBox();

        distPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Distribution"));

        distButtonGroup.add(normDistButton);
        normDistButton.setSelected(true);
        normDistButton.setText("Normal");
        normDistButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        normDistButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        distButtonGroup.add(poisDistButton);
        poisDistButton.setText("Poisson");
        poisDistButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        poisDistButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        distButtonGroup.add(binomDistButton);
        binomDistButton.setText("Binomial");
        binomDistButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        binomDistButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        binomDistButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                binomDistButtonStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout distPaneLayout = new org.jdesktop.layout.GroupLayout(distPane);
        distPane.setLayout(distPaneLayout);
        distPaneLayout.setHorizontalGroup(
            distPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(distPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(normDistButton)
                .add(18, 18, 18)
                .add(poisDistButton)
                .add(18, 18, 18)
                .add(binomDistButton)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        distPaneLayout.setVerticalGroup(
            distPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(distPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(distPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(normDistButton)
                    .add(poisDistButton)
                    .add(binomDistButton))
                .addContainerGap(17, Short.MAX_VALUE))
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
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
                .addContainerGap())
        );
        modelEqPaneLayout.setVerticalGroup(
            modelEqPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(modelEqPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        sampleSizeLabel.setText("Sample Size:");

        org.jdesktop.layout.GroupLayout varPaneLayout = new org.jdesktop.layout.GroupLayout(varPane);
        varPane.setLayout(varPaneLayout);
        varPaneLayout.setHorizontalGroup(
            varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(varPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(sampleSizeLabel)
                    .add(independentLabel)
                    .add(dependentLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, sampleSizeComboBox, 0, 233, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dependentComboBox, 0, 233, Short.MAX_VALUE))
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
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sampleSizeComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sampleSizeLabel))
                .addContainerGap())
        );

        siteSelPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Reference Selection"));

        siteVarLabel.setText("Reference Variable:");

        siteVarList.setPreferredSize(new java.awt.Dimension(120, 24));
        siteVarList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteVarListActionPerformed(evt);
            }
        });

        siteIDLabel.setText("Reference Data Values:");

        siteIDValList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                siteIDValListValueChanged(evt);
            }
        });
        siteIDValScrollPane.setViewportView(siteIDValList);

        org.jdesktop.layout.GroupLayout siteSelPaneLayout = new org.jdesktop.layout.GroupLayout(siteSelPane);
        siteSelPane.setLayout(siteSelPaneLayout);
        siteSelPaneLayout.setHorizontalGroup(
            siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(siteSelPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(siteIDLabel)
                    .add(siteVarLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(siteIDValScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .add(siteVarList, 0, 187, Short.MAX_VALUE))
                .addContainerGap())
        );
        siteSelPaneLayout.setVerticalGroup(
            siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(siteSelPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(siteVarLabel)
                    .add(siteVarList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(siteSelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(siteIDLabel)
                    .add(siteIDValScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addContainerGap())
        );

        idPane.setBorder(javax.swing.BorderFactory.createTitledBorder("ID Variable"));

        idVarLabel.setText("Variable for Labels:");

        org.jdesktop.layout.GroupLayout idPaneLayout = new org.jdesktop.layout.GroupLayout(idPane);
        idPane.setLayout(idPaneLayout);
        idPaneLayout.setHorizontalGroup(
            idPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(idVarLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(idVarList, 0, 209, Short.MAX_VALUE)
                .addContainerGap())
        );
        idPaneLayout.setVerticalGroup(
            idPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(idPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(idVarLabel)
                    .add(idVarList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        analysisOptionsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Options"));

        sigLabel.setText("Significance Level:");

        sigLevel.setModel(new javax.swing.SpinnerNumberModel(0.05d, 0.01d, 0.5d, 0.01d));

        rmIntercept.setText("Remove Intercept");

        org.jdesktop.layout.GroupLayout analysisOptionsPaneLayout = new org.jdesktop.layout.GroupLayout(analysisOptionsPane);
        analysisOptionsPane.setLayout(analysisOptionsPaneLayout);
        analysisOptionsPaneLayout.setHorizontalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(sigLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sigLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(rmIntercept)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        analysisOptionsPaneLayout.setVerticalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sigLabel)
                    .add(sigLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rmIntercept))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .add(modelEqPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, plotPaneLayout.createSequentialGroup()
                        .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(varPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(distPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(idPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(siteSelPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        plotPaneLayout.setVerticalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(siteSelPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(idPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(distPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(varPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        setTitle("Regression Prediction");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-743)/2, (screenSize.height-736)/2, 743, 736);
    }// </editor-fold>//GEN-END:initComponents

  private void siteIDValListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_siteIDValListValueChanged
  {//GEN-HEADEREND:event_siteIDValListValueChanged
    this.refreshValidity();
}//GEN-LAST:event_siteIDValListValueChanged

  private void siteVarListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_siteVarListActionPerformed
  {//GEN-HEADEREND:event_siteVarListActionPerformed
    refreshSiteValList();
  }//GEN-LAST:event_siteVarListActionPerformed

private void binomDistButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_binomDistButtonStateChanged
  sampleSizeLabel.setEnabled(binomDistButton.isSelected());
  sampleSizeComboBox.setEnabled(binomDistButton.isSelected());
  this.updateModelEquation();
}//GEN-LAST:event_binomDistButtonStateChanged

private void dependentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dependentComboBoxActionPerformed
  this.updateModelEquation();
}//GEN-LAST:event_dependentComboBoxActionPerformed

private void independentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_independentListValueChanged
  this.updateModelEquation();
  this.refreshSubmitButtonValidity();
}//GEN-LAST:event_independentListValueChanged

private void browserResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserResultsActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_browserResultsActionPerformed

  public javax.swing.JComboBox getIdVarList()
  {
    return idVarList;
  }
  
  public javax.swing.JComboBox getSiteVarList()
  {
    return siteVarList;
  }
  
  public Object[] getSiteValListSelectedValues()
  {
    return (GUIUtils.getSelectedValues(siteIDValList));
  }

  private void refreshIdVarList()
  {
    JComboBox colList = getIdVarList();
    for (int k=1, m=colList.getItemCount(); k < m; ++k)
    {
      colList.removeItemAt(1);
    }
    String dataset = this.getDatasetPane().getSelectedDataset();
    if ( dataset != null )
    {
      String[] colnames = RUtils.colnames(dataset);
      int colLength = colnames.length;
      for (int k=0; k < colLength; ++k)
      {
        colList.insertItemAt(colnames[k],k+1);
      }
      colList.setSelectedIndex(0);
    }
  }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel analysisOptionsPane;
    public javax.swing.JRadioButton binomDistButton;
    private javax.swing.JCheckBox browserResults;
    private javax.swing.JComboBox dependentComboBox;
    private javax.swing.JLabel dependentLabel;
    private javax.swing.ButtonGroup distButtonGroup;
    private javax.swing.JPanel distPane;
    private javax.swing.JPanel idPane;
    private javax.swing.JLabel idVarLabel;
    private javax.swing.JComboBox idVarList;
    private javax.swing.JLabel independentLabel;
    private javax.swing.JList independentList;
    private javax.swing.JScrollPane independentScrollPane;
    private javax.swing.JPanel modelEqPane;
    private javax.swing.JScrollPane modelEqScrollPane;
    private javax.swing.JTextArea modelEqTextArea;
    public javax.swing.JRadioButton normDistButton;
    private javax.swing.JPanel plotPane;
    public javax.swing.JRadioButton poisDistButton;
    private javax.swing.JCheckBox rmIntercept;
    private javax.swing.JComboBox sampleSizeComboBox;
    private javax.swing.JLabel sampleSizeLabel;
    private javax.swing.JPanel savePanel;
    private javax.swing.JLabel sigLabel;
    private javax.swing.JSpinner sigLevel;
    private javax.swing.JLabel siteIDLabel;
    private javax.swing.JList siteIDValList;
    private javax.swing.JScrollPane siteIDValScrollPane;
    private javax.swing.JPanel siteSelPane;
    private javax.swing.JLabel siteVarLabel;
    private javax.swing.JComboBox siteVarList;
    private javax.swing.JPanel varPane;
    // End of variables declaration//GEN-END:variables

  @Override
  protected void submitButtonAction()
  {
    String distfamily = null;
    
    if (normDistButton.isSelected())
    {
      distfamily = "gaussian";
    }

    else if (poisDistButton.isSelected())
    {
      distfamily = "poisson";
    }

    else if (binomDistButton.isSelected())
    {
      distfamily = "binomial";
    }
      
    String cmd = "glm.pred.JGR("
      + "my.data=" + this.getDatasetPane().getSelectedDataset()
      + ", subset1.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane1().getSelectedFactor() + "'" )
      + ", subset1.val="  + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'") + ")")
      + ", subset2.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane2().getSelectedFactor() + "'" )
      + ", subset2.val="  + (RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'") + ")")
      + ", site.name=" + "'" + getSiteVarList().getSelectedItem().toString() + "'"
      + ", site.val=" + "c(" + RUtils.toString(getSiteValListSelectedValues(), ",", "'") + ")"
      + ", my.formula=" + "'" + this.getModelString() + "'"
      + ", my.family=" + "'" + distfamily + "'"
      + ", label.name=" + "'" + idVarList.getSelectedItem() + "'"
      + ", sig.level=" + ((Double) sigLevel.getValue()).doubleValue()
      + ", iRmIntercept=" + String.valueOf(rmIntercept.isSelected()).toUpperCase()
      + ", browserResults=" + String.valueOf(browserResults.isSelected()).toUpperCase()      
      + ")";
//    RUtils.evalAsVector(cmd);
    JGR.MAINRCONSOLE.execute(cmd, true);
  }

  @Override
  protected void helpButtonAction()
  {
    String cmd = "CADStat.help('glm.pred.JGR')";
    JGR.MAINRCONSOLE.execute(cmd, true);
//    JGR.MAINRCONSOLE.help("LinearRegression");
  }

  public String getModelString()
  {
    return modelString;
  }

  public void setModelString(String modelString)
  {
    this.modelString = modelString;
  }

}
