/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neptuneinc.cadstat.utils;

import javax.swing.JComboBox;
import javax.swing.JList;

/**
 *
 * @author Pasha Minallah
 */
public class GUIUtils
{
  public static String getSelectedItem(JComboBox comboBox)
  {
    if (comboBox != null && comboBox.getItemCount() > 0)
    {
      return comboBox.getSelectedItem().toString();
    }
    
    else
    {
      return null;
    }
  }
  
  public static Object[] getSelectedValues(JList list)
  {
    if (list != null && !list.isSelectionEmpty())
    {
      return list.getSelectedValues();
    }
    
    else
    {
      return null;
    }
  }
  
}
