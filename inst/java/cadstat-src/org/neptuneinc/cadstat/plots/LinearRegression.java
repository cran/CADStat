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

import org.neptuneinc.cadstat.utils.RUtils;
import org.rosuda.JGR.JGR;

/**
 *
 * @author  Pasha Minallah
 */
public class LinearRegression extends DataPlotDialog
{
  private String modelString;
  
  /**
   * Creates new form LinearRegression
   */
  public LinearRegression()
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

  public void refreshConfLevelValidity()
  {
    confLabel.setEnabled(confIntervals.isSelected());
    confLevelSpinner.setEnabled(confIntervals.isSelected());
  }

  public void refreshSubmitButtonValidity()
  {
    this.getSubmitButton().setEnabled(!independentList.isSelectionEmpty());
  }

  public void refreshValidity()
  {
    this.refreshModelValidity();
    this.refreshConfLevelValidity();
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
        plotOptsPane = new javax.swing.JPanel();
        resVsFit = new javax.swing.JCheckBox();
        normalQQ = new javax.swing.JCheckBox();
        scaleLoc = new javax.swing.JCheckBox();
        cookDist = new javax.swing.JCheckBox();
        influencePlot = new javax.swing.JCheckBox();
        analysisOptsPane = new javax.swing.JPanel();
        rmIntercept = new javax.swing.JCheckBox();
        confIntervals = new javax.swing.JCheckBox();
        confLabel = new javax.swing.JLabel();
        confLevelSpinner = new javax.swing.JSpinner();
        savePanel = new javax.swing.JPanel();
        saveResults = new javax.swing.JCheckBox();
        resultLabel = new javax.swing.JLabel();
        resultName = new javax.swing.JTextField();
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
                .addContainerGap(16, Short.MAX_VALUE))
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
            .add(modelEqPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
        modelEqPaneLayout.setVerticalGroup(
            modelEqPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(modelEqPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
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
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, sampleSizeComboBox, 0, 196, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dependentComboBox, 0, 196, Short.MAX_VALUE))
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
                    .add(independentScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sampleSizeComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sampleSizeLabel))
                .addContainerGap())
        );

        plotOptsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnostic Plots"));

        resVsFit.setText("Residual vs. Fitted");

        normalQQ.setText("Normal Q-Q Plot");

        scaleLoc.setText("Scale-Location Plot");

        cookDist.setText("Cook's Distance Plot");

        influencePlot.setText("Influence Plot");

        org.jdesktop.layout.GroupLayout plotOptsPaneLayout = new org.jdesktop.layout.GroupLayout(plotOptsPane);
        plotOptsPane.setLayout(plotOptsPaneLayout);
        plotOptsPaneLayout.setHorizontalGroup(
            plotOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotOptsPaneLayout.createSequentialGroup()
                .add(plotOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(plotOptsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(resVsFit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(plotOptsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(normalQQ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(plotOptsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(scaleLoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(plotOptsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(cookDist))
                    .add(plotOptsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(influencePlot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        plotOptsPaneLayout.setVerticalGroup(
            plotOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotOptsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(resVsFit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(normalQQ)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scaleLoc)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cookDist)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(influencePlot)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        analysisOptsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Options"));

        rmIntercept.setText("Remove Intercept");

        confIntervals.setText("Compute Confidence Intervals");
        confIntervals.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                confIntervalsStateChanged(evt);
            }
        });

        confLabel.setText("Confidence Level:");

        confLevelSpinner.setModel(new javax.swing.SpinnerNumberModel(0.95d, 0.01d, 0.99d, 0.01d));

        org.jdesktop.layout.GroupLayout analysisOptsPaneLayout = new org.jdesktop.layout.GroupLayout(analysisOptsPane);
        analysisOptsPane.setLayout(analysisOptsPaneLayout);
        analysisOptsPaneLayout.setHorizontalGroup(
            analysisOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rmIntercept)
                    .add(analysisOptsPaneLayout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(confLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(confLevelSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(confIntervals))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        analysisOptsPaneLayout.setVerticalGroup(
            analysisOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(rmIntercept)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(confIntervals)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(confLabel)
                    .add(confLevelSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        savePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        saveResults.setText("Save R Results?");
        saveResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saveResults.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResultsActionPerformed(evt);
            }
        });

        resultLabel.setText("R Result Name");
        resultLabel.setEnabled(false);

        resultName.setColumns(12);
        resultName.setEnabled(false);

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
                .add(savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(browserResults)
                    .add(savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(savePanelLayout.createSequentialGroup()
                            .add(saveResults)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, savePanelLayout.createSequentialGroup()
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(resultLabel)
                            .add(10, 10, 10)
                            .add(resultName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        savePanelLayout.setVerticalGroup(
            savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(savePanelLayout.createSequentialGroup()
                .add(browserResults)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveResults)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(savePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(resultLabel)
                    .add(resultName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout plotPaneLayout = new org.jdesktop.layout.GroupLayout(plotPane);
        plotPane.setLayout(plotPaneLayout);
        plotPaneLayout.setHorizontalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, modelEqPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, plotPaneLayout.createSequentialGroup()
                        .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(varPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(distPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(savePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(analysisOptsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(plotOptsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        plotPaneLayout.setVerticalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(modelEqPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(plotOptsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisOptsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(distPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(varPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        setTitle("Linear Regression");

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-640)/2, (screenSize.height-675)/2, 640, 675);
    }// </editor-fold>//GEN-END:initComponents

  private void confIntervalsStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_confIntervalsStateChanged
  {//GEN-HEADEREND:event_confIntervalsStateChanged
    this.refreshValidity();
  }//GEN-LAST:event_confIntervalsStateChanged

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

private void saveResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveResultsActionPerformed
  resultLabel.setEnabled(saveResults.isSelected());
  resultName.setEnabled(saveResults.isSelected());
  
  if(saveResults.isSelected())
  {
    resultName.setText("my.glm.fit");
  }
  
  else
  {
    resultName.setText(null);
  }
}//GEN-LAST:event_saveResultsActionPerformed

private void browserResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserResultsActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_browserResultsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel analysisOptsPane;
    public javax.swing.JRadioButton binomDistButton;
    private javax.swing.JCheckBox browserResults;
    private javax.swing.JCheckBox confIntervals;
    private javax.swing.JLabel confLabel;
    private javax.swing.JSpinner confLevelSpinner;
    private javax.swing.JCheckBox cookDist;
    private javax.swing.JComboBox dependentComboBox;
    private javax.swing.JLabel dependentLabel;
    private javax.swing.ButtonGroup distButtonGroup;
    private javax.swing.JPanel distPane;
    private javax.swing.JLabel independentLabel;
    private javax.swing.JList independentList;
    private javax.swing.JScrollPane independentScrollPane;
    private javax.swing.JCheckBox influencePlot;
    private javax.swing.JPanel modelEqPane;
    private javax.swing.JScrollPane modelEqScrollPane;
    private javax.swing.JTextArea modelEqTextArea;
    public javax.swing.JRadioButton normDistButton;
    private javax.swing.JCheckBox normalQQ;
    private javax.swing.JPanel plotOptsPane;
    private javax.swing.JPanel plotPane;
    public javax.swing.JRadioButton poisDistButton;
    private javax.swing.JCheckBox resVsFit;
    private javax.swing.JLabel resultLabel;
    private javax.swing.JTextField resultName;
    private javax.swing.JCheckBox rmIntercept;
    private javax.swing.JComboBox sampleSizeComboBox;
    private javax.swing.JLabel sampleSizeLabel;
    private javax.swing.JPanel savePanel;
    private javax.swing.JCheckBox saveResults;
    private javax.swing.JCheckBox scaleLoc;
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
      
    String cmd = "glm.JGR("
      + "my.data=" + this.getDatasetPane().getSelectedDataset()
      + ", subset1.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane1().getSelectedFactor() + "'" )
      + ", subset1.val="  + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'") + ")")
      + ", subset2.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane2().getSelectedFactor() + "'" )
      + ", subset2.val="  + (RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'") + ")")
      + ", my.formula=" + "'" + this.getModelString() + "'"
      + ", my.family=" + "'" + distfamily + "'"
      + ", iCI=" + String.valueOf(confIntervals.isSelected()).toUpperCase()
      + (confIntervals.isSelected() ? ", conf.level=" + ((Double) confLevelSpinner.getValue()).doubleValue() : "")
      + ", iDiag.1=" + String.valueOf(resVsFit.isSelected()).toUpperCase()
      + ", iDiag.2=" + String.valueOf(normalQQ.isSelected()).toUpperCase()
      + ", iDiag.3=" + String.valueOf(scaleLoc.isSelected()).toUpperCase()
      + ", iDiag.4=" + String.valueOf(cookDist.isSelected()).toUpperCase()
      + ", iInfluence=" + String.valueOf(influencePlot.isSelected()).toUpperCase()
      + ", iRmIntercept=" + String.valueOf(rmIntercept.isSelected()).toUpperCase()
      + ", saveLMName=" + "'" + resultName.getText() + "'" 
      + ", browserResults=" + String.valueOf(browserResults.isSelected()).toUpperCase()
      + ")";
      
    JGR.MAINRCONSOLE.execute(cmd, true);
  }

  @Override
  protected void helpButtonAction()
  {
    String cmd = "CADStat.help('glm.JGR')";
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
