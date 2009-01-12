/*
 * ConditionalProbability.java
 *
 * Created on October 9, 2005, 9:47 AM
 */

package org.neptuneinc.cadstat.plots;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

import org.neptuneinc.cadstat.prefs.PreferencesManager;
import org.neptuneinc.cadstat.ui.DataPlotDialog;
import org.neptuneinc.cadstat.utils.GUIUtils;
import org.neptuneinc.cadstat.utils.RUtils;
import org.rosuda.JGR.JGR;

/**
 *
 * @author  Pasha Minallah
 */
public class ConditionalProbability extends DataPlotDialog
{
  private String condDir;
  private String probDir;
  
  /**
   * Creates new form ConditionalProbability
   */
  public ConditionalProbability()
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
    this.refreshCondValue();
    this.refreshValidity();
    this.refreshyAxis();
  }

  private void datasetComboBoxActionPerformed(ActionEvent e)
  {
    this.refreshVariableList();
    this.refreshCondValue();
    this.refreshValidity();
    this.refreshyAxis();
  }

  public void refreshVariableListValidity()
  {
    stressorLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && stressorComboBox.getItemCount() > 0);
    stressorComboBox.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && stressorComboBox.getItemCount() > 0);
    responseLabel.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && responseComboBox.getItemCount() > 0);
    responseComboBox.setEnabled(this.getDatasetPane().getDatasetComboBox().getItemCount() > 0 && responseComboBox.getItemCount() > 0);
    weightsLabel.setEnabled(weightsCheckBox.isSelected());
    weightsComboBox.setEnabled(weightsCheckBox.isSelected());
  }

  public void refreshVariableList()
  {
    if (this.getDatasetPane().getDatasetComboBox().getItemCount() > 0)
    {
      Vector nonFactors = RUtils.nonFactors(this.getDatasetPane().getSelectedDataset());
      
      stressorComboBox.setModel(new DefaultComboBoxModel(nonFactors));
      responseComboBox.setModel(new DefaultComboBoxModel(nonFactors));
      weightsComboBox.setModel(new DefaultComboBoxModel(nonFactors));

      if (stressorComboBox.getItemCount() > 0)
      {
        stressorComboBox.setSelectedIndex(0);
      }
      
      if (responseComboBox.getItemCount() > 0)
      {
        responseComboBox.setSelectedIndex(0);
      }
      
      if (weightsComboBox.getItemCount() > 0)
      {
        weightsComboBox.setSelectedIndex(0);
      }
    }

    else
    {
      stressorComboBox.removeAllItems();
      responseComboBox.removeAllItems();
      weightsComboBox.removeAllItems();
    }

    this.refreshValidity();
  }

  public void refreshCondValue()
  {
    if (responseComboBox.getItemCount() > 0)
    {
      String var, rangeCmd, meanCmd;

      var = this.getDatasetPane().getSelectedDataset() + "$" + GUIUtils.getSelectedItem(responseComboBox);
      rangeCmd = "range(" + var + ", na.rm=T, finite=T)";
      meanCmd = "mean(" + var + ", na.rm=T, finite=T)";

      Vector range, mean;

      range = RUtils.evalAsVector(rangeCmd);
      mean = RUtils.evalAsVector(meanCmd);

      boolean isDouble = range.get(0) instanceof Double;
      boolean isInteger = range.get(0) instanceof Integer;

      SpinnerNumberModel model;

      if (isDouble)
      {
        double def, min, max, step;

        min = ((Double) range.get(0)).doubleValue();
        max = ((Double) range.get(1)).doubleValue();

        if (mean.get(0) instanceof Double)
          def = Math.rint(((Double) mean.get(0)).doubleValue());
        else if(mean.get(0) instanceof Integer)
          def = Math.rint(((Integer) mean.get(0)).intValue());
        else
          def = Math.rint(max - 1);

        step = new Double((max - min) / 100.00).intValue();
        step = ((step != 0.0) ? step : 1.0);

        model = new SpinnerNumberModel(def, min, max, step);
      }

      else if (isInteger)
      {
        int def, min, max, step;

        min = ((Integer) range.get(0)).intValue();
        max = ((Integer) range.get(1)).intValue();

        if (mean.get(0) instanceof Double)
          def = ((Double) mean.get(0)).intValue();
        else if(mean.get(0) instanceof Integer)
          def = ((Integer) mean.get(0)).intValue();
        else
          def = max;

        step = new Double((max - min) / 100.00).intValue();
        step = ((step != 0) ? step : 1);

        model = new SpinnerNumberModel(def, min, max, step);
      }

      else
        model = new SpinnerNumberModel(0, 0, 0, 0);

      condValueSpinner.setModel(model);
    }
  }
  
  public void refreshOptionalParamsValidity()
  {
    numBootstrapsLabel.setEnabled(bootstrapCheckBox.isSelected());
    numBootstrapsSpinner.setEnabled(bootstrapCheckBox.isSelected());
  }

  public void refreshyAxis()
  {
      yAxis.setText(GUIUtils.getSelectedItem(responseComboBox));
  }
  
  public void refreshxAxis()
  {
      xAxis.setText(GUIUtils.getSelectedItem(stressorComboBox));
  }

  public void refreshSubmitButtonValidity()
  {
    this.getSubmitButton().setEnabled(responseComboBox.getItemCount() > 0);
  }

  public void refreshValidity()
  {
    this.refreshVariableListValidity();
    this.refreshOptionalParamsValidity();
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
        varPane = new javax.swing.JPanel();
        stressorLabel = new javax.swing.JLabel();
        stressorComboBox = new javax.swing.JComboBox();
        responseLabel = new javax.swing.JLabel();
        responseComboBox = new javax.swing.JComboBox();
        weightsLabel = new javax.swing.JLabel();
        weightsComboBox = new javax.swing.JComboBox();
        weightsCheckBox = new javax.swing.JCheckBox();
        analysisOptionsPane = new javax.swing.JPanel();
        condDirLabel = new javax.swing.JLabel();
        condDirGreaterThanRadioButton = new javax.swing.JRadioButton();
        condDirLessThanRadioButton = new javax.swing.JRadioButton();
        condValueLabel = new javax.swing.JLabel();
        condValueSpinner = new javax.swing.JSpinner();
        probDirLabel = new javax.swing.JLabel();
        probDirGreaterThanEqualRadioButton = new javax.swing.JRadioButton();
        probDirLessThanEqualRadioButton = new javax.swing.JRadioButton();
        bootstrapCheckBox = new javax.swing.JCheckBox();
        alphaLabel = new javax.swing.JLabel();
        alphaSpinner = new javax.swing.JSpinner();
        numBootstrapsLabel = new javax.swing.JLabel();
        numBootstrapsSpinner = new javax.swing.JSpinner();
        plotLabelPane = new javax.swing.JPanel();
        plotTitleLabel = new javax.swing.JLabel();
        plotTitle = new javax.swing.JTextField();
        xAxisLabel = new javax.swing.JLabel();
        xAxis = new javax.swing.JTextField();
        yAxisLabel = new javax.swing.JLabel();
        yAxis = new javax.swing.JTextField();
        savePanel = new javax.swing.JPanel();
        browserResults = new javax.swing.JCheckBox();
        condDirButtonGroup = new javax.swing.ButtonGroup();
        probDirButtonGroup = new javax.swing.ButtonGroup();

        varPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));

        stressorLabel.setText("Stressor:");

        stressorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stressorComboBoxActionPerformed(evt);
            }
        });

        responseLabel.setText("Response:");

        responseComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                responseComboBoxActionPerformed(evt);
            }
        });

        weightsLabel.setText("Weights:");

        weightsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightsComboBoxActionPerformed(evt);
            }
        });

        weightsCheckBox.setText("Specify a weighting variable");
        weightsCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                weightsCheckBoxStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout varPaneLayout = new org.jdesktop.layout.GroupLayout(varPane);
        varPane.setLayout(varPaneLayout);
        varPaneLayout.setHorizontalGroup(
            varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(varPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(varPaneLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(weightsLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(weightsComboBox, 0, 163, Short.MAX_VALUE))
                    .add(varPaneLayout.createSequentialGroup()
                        .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(stressorLabel)
                            .add(responseLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(stressorComboBox, 0, 161, Short.MAX_VALUE)
                            .add(responseComboBox, 0, 161, Short.MAX_VALUE)))
                    .add(weightsCheckBox))
                .addContainerGap())
        );
        varPaneLayout.setVerticalGroup(
            varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(varPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(stressorLabel)
                    .add(stressorComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(responseLabel)
                    .add(responseComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(weightsCheckBox)
                .add(7, 7, 7)
                .add(varPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(weightsLabel)
                    .add(weightsComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        analysisOptionsPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Options"));

        condDirLabel.setText("Cutoff Value Direction:");

        condDirButtonGroup.add(condDirGreaterThanRadioButton);
        condDirGreaterThanRadioButton.setText("Greater Than");
        condDirGreaterThanRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condDirGreaterThanRadioButtonActionPerformed(evt);
            }
        });

        condDirButtonGroup.add(condDirLessThanRadioButton);
        condDirLessThanRadioButton.setSelected(true);
        condDirLessThanRadioButton.setText("Less Than");
        condDirLessThanRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                condDirLessThanRadioButtonActionPerformed(evt);
            }
        });

        condValueLabel.setLabelFor(condValueSpinner);
        condValueLabel.setText("Response Cutoff Value:");

        condValueSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(0.0d)));

        probDirLabel.setText("Probability Direction:");

        probDirButtonGroup.add(probDirGreaterThanEqualRadioButton);
        probDirGreaterThanEqualRadioButton.setSelected(true);
        probDirGreaterThanEqualRadioButton.setText("Greater Than or Equal");
        probDirGreaterThanEqualRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                probDirGreaterThanEqualRadioButtonActionPerformed(evt);
            }
        });

        probDirButtonGroup.add(probDirLessThanEqualRadioButton);
        probDirLessThanEqualRadioButton.setText("Less Than or Equal");
        probDirLessThanEqualRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                probDirLessThanEqualRadioButtonActionPerformed(evt);
            }
        });

        bootstrapCheckBox.setText("Bootstrap confidence intervals");
        bootstrapCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bootstrapCheckBoxStateChanged(evt);
            }
        });
        bootstrapCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bootstrapCheckBoxActionPerformed(evt);
            }
        });

        alphaLabel.setText("Confidence Interval Alpha:");

        alphaSpinner.setModel(new javax.swing.SpinnerNumberModel(0.05d, 0.01d, 1.0d, 0.01d));

        numBootstrapsLabel.setText("Number of Bootstraps:");

        numBootstrapsSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(1), null, Integer.valueOf(1)));

        org.jdesktop.layout.GroupLayout analysisOptionsPaneLayout = new org.jdesktop.layout.GroupLayout(analysisOptionsPane);
        analysisOptionsPane.setLayout(analysisOptionsPaneLayout);
        analysisOptionsPaneLayout.setHorizontalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(analysisOptionsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(analysisOptionsPaneLayout.createSequentialGroup()
                                .add(probDirLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(probDirGreaterThanEqualRadioButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(probDirLessThanEqualRadioButton))
                            .add(analysisOptionsPaneLayout.createSequentialGroup()
                                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, analysisOptionsPaneLayout.createSequentialGroup()
                                        .add(condValueLabel)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(condValueSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, analysisOptionsPaneLayout.createSequentialGroup()
                                        .add(19, 19, 19)
                                        .add(condDirLabel)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(condDirGreaterThanRadioButton)))
                                .add(42, 42, 42)
                                .add(condDirLessThanRadioButton))
                            .add(analysisOptionsPaneLayout.createSequentialGroup()
                                .add(alphaLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(alphaSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(analysisOptionsPaneLayout.createSequentialGroup()
                        .add(39, 39, 39)
                        .add(numBootstrapsLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(numBootstrapsSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(analysisOptionsPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(bootstrapCheckBox)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        analysisOptionsPaneLayout.setVerticalGroup(
            analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisOptionsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(condValueLabel)
                    .add(condValueSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(condDirGreaterThanRadioButton)
                    .add(condDirLessThanRadioButton)
                    .add(condDirLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(probDirLabel)
                    .add(probDirGreaterThanEqualRadioButton)
                    .add(probDirLessThanEqualRadioButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(alphaLabel)
                    .add(alphaSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(bootstrapCheckBox)
                .add(3, 3, 3)
                .add(analysisOptionsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(numBootstrapsLabel)
                    .add(numBootstrapsSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        plotLabelPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Labels"));

        plotTitleLabel.setLabelFor(plotTitle);
        plotTitleLabel.setText("Plot Title:");

        plotTitle.setColumns(12);
        plotTitle.setText("Conditional Probability");

        xAxisLabel.setLabelFor(xAxis);
        xAxisLabel.setText("X-Axis:");

        xAxis.setColumns(12);
        xAxis.setText("X");

        yAxisLabel.setLabelFor(yAxis);
        yAxisLabel.setText("Y-Axis:");

        yAxis.setColumns(12);
        yAxis.setText("Y");

        org.jdesktop.layout.GroupLayout plotLabelPaneLayout = new org.jdesktop.layout.GroupLayout(plotLabelPane);
        plotLabelPane.setLayout(plotLabelPaneLayout);
        plotLabelPaneLayout.setHorizontalGroup(
            plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotLabelPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(xAxisLabel)
                    .add(yAxisLabel)
                    .add(plotTitleLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(plotTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .add(xAxis, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .add(yAxis, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                .add(36, 36, 36))
        );
        plotLabelPaneLayout.setVerticalGroup(
            plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotLabelPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(plotTitleLabel)
                    .add(plotTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xAxisLabel)
                    .add(xAxis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(plotLabelPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(yAxisLabel)
                    .add(yAxis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        savePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        browserResults.setText("Display Results in Browser");
        browserResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        browserResults.setMargin(new java.awt.Insets(0, 0, 0, 0));

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
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(varPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(plotLabelPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(plotPaneLayout.createSequentialGroup()
                        .add(savePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(420, 420, 420))))
        );
        plotPaneLayout.setVerticalGroup(
            plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(plotPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(plotPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(varPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(plotLabelPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisOptionsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(savePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setTitle("Conditional Probability");

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-621)/2, (screenSize.height-586)/2, 621, 586);
    }// </editor-fold>//GEN-END:initComponents

private void responseComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_responseComboBoxActionPerformed
  this.refreshCondValue();
  this.refreshyAxis();
}//GEN-LAST:event_responseComboBoxActionPerformed

private void bootstrapCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bootstrapCheckBoxStateChanged
  this.refreshOptionalParamsValidity();
}//GEN-LAST:event_bootstrapCheckBoxStateChanged

private void condDirGreaterThanRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condDirGreaterThanRadioButtonActionPerformed
  this.setConditionalValueDirection(">");
}//GEN-LAST:event_condDirGreaterThanRadioButtonActionPerformed

private void condDirLessThanRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_condDirLessThanRadioButtonActionPerformed
  this.setConditionalValueDirection("<");
}//GEN-LAST:event_condDirLessThanRadioButtonActionPerformed

private void probDirGreaterThanEqualRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_probDirGreaterThanEqualRadioButtonActionPerformed
  this.setProbabilityDirection(">=");
}//GEN-LAST:event_probDirGreaterThanEqualRadioButtonActionPerformed

private void probDirLessThanEqualRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_probDirLessThanEqualRadioButtonActionPerformed
  this.setProbabilityDirection("<=");
}//GEN-LAST:event_probDirLessThanEqualRadioButtonActionPerformed

private void weightsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightsComboBoxActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_weightsComboBoxActionPerformed

private void weightsCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_weightsCheckBoxStateChanged
    this.refreshVariableListValidity();
}//GEN-LAST:event_weightsCheckBoxStateChanged

private void bootstrapCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bootstrapCheckBoxActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_bootstrapCheckBoxActionPerformed

private void stressorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stressorComboBoxActionPerformed
  this.refreshxAxis();
}//GEN-LAST:event_stressorComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alphaLabel;
    private javax.swing.JSpinner alphaSpinner;
    private javax.swing.JPanel analysisOptionsPane;
    private javax.swing.JCheckBox bootstrapCheckBox;
    private javax.swing.JCheckBox browserResults;
    private javax.swing.ButtonGroup condDirButtonGroup;
    private javax.swing.JRadioButton condDirGreaterThanRadioButton;
    private javax.swing.JLabel condDirLabel;
    private javax.swing.JRadioButton condDirLessThanRadioButton;
    private javax.swing.JLabel condValueLabel;
    private javax.swing.JSpinner condValueSpinner;
    private javax.swing.JLabel numBootstrapsLabel;
    private javax.swing.JSpinner numBootstrapsSpinner;
    private javax.swing.JPanel plotLabelPane;
    private javax.swing.JPanel plotPane;
    private javax.swing.JTextField plotTitle;
    private javax.swing.JLabel plotTitleLabel;
    private javax.swing.ButtonGroup probDirButtonGroup;
    private javax.swing.JRadioButton probDirGreaterThanEqualRadioButton;
    private javax.swing.JLabel probDirLabel;
    private javax.swing.JRadioButton probDirLessThanEqualRadioButton;
    private javax.swing.JComboBox responseComboBox;
    private javax.swing.JLabel responseLabel;
    private javax.swing.JPanel savePanel;
    private javax.swing.JComboBox stressorComboBox;
    private javax.swing.JLabel stressorLabel;
    private javax.swing.JPanel varPane;
    private javax.swing.JCheckBox weightsCheckBox;
    private javax.swing.JComboBox weightsComboBox;
    private javax.swing.JLabel weightsLabel;
    private javax.swing.JTextField xAxis;
    private javax.swing.JLabel xAxisLabel;
    private javax.swing.JTextField yAxis;
    private javax.swing.JLabel yAxisLabel;
    // End of variables declaration//GEN-END:variables

  @Override
  protected void submitButtonAction()
  {
    PreferencesManager prefsManager = new PreferencesManager();
    
    try
    {
      prefsManager.importPreferences();
    }
    catch (Exception ex)
    {
      Logger.getLogger(ConditionalProbability.class.getName()).log(Level.INFO, null, ex);
    }

    String cmd = "conditionalprob.JGR("
      + "my.data=" + this.getDatasetPane().getSelectedDataset()
      + ", x=" + "'" + GUIUtils.getSelectedItem(stressorComboBox) + "'"
      + ", y=" + "'" + GUIUtils.getSelectedItem(responseComboBox) + "'"
      + ", weights=" + (weightsCheckBox.isSelected() ? "'" + GUIUtils.getSelectedItem(weightsComboBox) + "'" : "NULL")
      + ", cond.val=" + condValueSpinner.getValue()
      + ", cond.val.direction=" + (this.getConditionalValueDirection()==null ? "'<'" : "'" + this.getConditionalValueDirection() + "'" )
      + ", prob.direction=" + (this.getProbabilityDirection()==null ? "'>='" : "'" + this.getProbabilityDirection() + "'" )
      + ", alpha=" + alphaSpinner.getValue()
      + ", R=" + (bootstrapCheckBox.isSelected() ? numBootstrapsSpinner.getValue() : "NULL")
      + ", subset1.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane1().getSelectedFactor() + "'" )
      + ", subset1.val="  + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'") + ")")
      + ", subset2.name=" + (RUtils.toString(this.getFactorSelectionPane1().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "'" + this.getFactorSelectionPane2().getSelectedFactor() + "'" )
      + ", subset2.val="  + (RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'").length()==0 ? "NULL" : "c(" + RUtils.toString(this.getFactorSelectionPane2().getSelectedFactorValues(), ",", "'") + ")")
      + ", main=" + "'" + plotTitle.getText() + "'"
      + ", xlab=" + "'" + xAxis.getText() + "'"
      + ", ylab=" + "'" + yAxis.getText() + "'"
      + ", browserResults=" + String.valueOf(browserResults.isSelected()).toUpperCase()
      + ", " + prefsManager.getCadstatPreferences().toString()
      + ")";
    JGR.MAINRCONSOLE.execute(cmd, true);
  }

  @Override
  protected void helpButtonAction()
  {
    String cmd = "CADStat.help('conditionalprob.JGR')";
    JGR.MAINRCONSOLE.execute(cmd, true);
//    JGR.MAINRCONSOLE.help("Conditionalprob");
  }

  public String getConditionalValueDirection()
  {
    return condDir;
  }

  public void setConditionalValueDirection(String condDir)
  {
    this.condDir = condDir;
  }

  public String getProbabilityDirection()
  {
    return probDir;
  }

  public void setProbabilityDirection(String probDir)
  {
    this.probDir = probDir;
  }

}
