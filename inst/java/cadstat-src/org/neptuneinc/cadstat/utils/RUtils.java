/*
 * RUtils.java
 *
 * Created on September 20, 2005, 9:25 PM
 */

package org.neptuneinc.cadstat.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.rosuda.JGR.JGR;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Pasha Minallah
 */
public class RUtils
{
  public static Rengine R_ENGINE = JGR.R;
  
  public static String[] ls()
  {
    if (R_ENGINE == null)
    {
      return null;
    }
    
    REXP exp = R_ENGINE.eval("ls()");
    
    if (exp != null)
    {
      return exp.asStringArray();
    }
    
    else
    {
      return null;
    }
  }
  
  public static Object[] ls(String rClass)
  {
    if (R_ENGINE == null)
    {
      return null;
    }
    
    List vars = new ArrayList();
    String[] ls = ls();
    String var;
    
    for (int i = 0, n = ls.length; i < n; ++i)
    {
      var = ls[i];
      REXP exp = R_ENGINE.eval("attr(" + var + ", 'class')");
      
      if (exp != null)
      {
        String varClass = exp.asString();

        // Add variable to list if classes match
        if (varClass != null && varClass.compareTo(rClass) == 0)
        {
          vars.add(var);
        }
      }
    }
    
    return(vars.toArray());
  }
  
  public static Object[] getDatasetList()
  {
    return(RUtils.ls("data.frame"));
  }
  
  public static String[] colnames(String x)
  {
    if (R_ENGINE == null)
    {
      return null;
    }
    
    REXP exp = R_ENGINE.eval("colnames(" + x + ")");
    
    if (exp != null)
    {
      return exp.asStringArray();
    }
    
    else
    {
      return null;
    }
  }
  
  public static Vector colnamesVector(String x)
  {
    String[] cols = colnames(x);
    Vector colvec = new Vector(cols.length);
    for(int i = 0, n = cols.length; i < n; ++i)
    {
      colvec.add(cols[i]);
    }
    return(colvec);
  }
  
  public static Vector factors(String x)
  {
    String[] cols = colnames(x);
    
    if (cols != null)
    {
      Vector factors = new Vector(cols.length);

      String var, col;
      boolean isFactor;

      for (int i = 0, n = cols.length; i < n; ++i)
      {
        col = cols[i];
        var = x + "$" + col;
        
        REXP exp = R_ENGINE.eval("as.character(is.factor(" + var + ") | is.logical(" + var + ") | is.character(" + var + "))");
        
        if (exp != null)
        {
          isFactor = Boolean.valueOf(exp.asString()).booleanValue();

          if (isFactor)
          {
            factors.add(col);
          }
        }
      }

      return(factors);
    }
    
    else
    {
      return null;
    }
  }
  
  public static Vector nonFactors(String x)
  {
    String[] cols = colnames(x);
    
    if (cols != null)
    {
      Vector nonFactors = new Vector(cols.length);

      String var, col;
      boolean isFactor;

      for (int i = 0, n = cols.length; i < n; ++i)
      {
        col = cols[i];
        var = x + "$" + col;
        isFactor = Boolean.valueOf(R_ENGINE.eval(
          "as.character(is.factor(" + var + ") | is.logical(" + var + ") | is.character(" + var + "))").asString()).booleanValue();

        if (!isFactor)
        {
          nonFactors.add(col);
        }
      }

      return(nonFactors);
    }
    
    else
    {
      return null;
    }
  }
  
  public static String toString(Object[] obj, String separator, String quote)
  {
    if (obj != null)
    {
      return RUtils.toString(Arrays.asList(obj), separator, quote);
    }
    
    else
    {
      return "";
    }
  }
  
  public static String toString(List list, String separator, String quote)
  {
    String str = "";
    
    if (separator == null)
      separator = ",";
    
    if (quote == null)
      quote = "'";
    
    if (list != null && list.size() > 0)
    {
      Object o;
      
      for (int i = 0, n = list.size(); i < n; ++i)
      {
        o = list.get(i);
        
        if (o != null)
        {
          if (o instanceof Integer || o instanceof Double)
          {
            str += o + separator;
          }

          else
          {
            str += quote + o + quote + separator;
          }
        }
      }
      
      if (str.length() >= separator.length())
        str = str.substring(0, str.length() - separator.length());
    }
    
    return(str);
  }
  
  public static Vector evalAsVector(String cmd)
  {
    if (R_ENGINE == null)
    {
      return null;
    }
    
    REXP exp = R_ENGINE.eval("as.vector(" + cmd + ")");
    
    if (exp != null)
    {
      Vector v;
      int[] intArray;
      double[] dblArray;
      String[] strArray;
      String str;

      if ((intArray = exp.asIntArray()) != null)
      {
        v = new Vector(intArray.length);

        for (int i = 0, n = intArray.length; i < n; ++i)
        {
          v.add(new Integer(intArray[i]));
        }
      }

      else if ((dblArray = exp.asDoubleArray()) != null)
      {
        v = new Vector(dblArray.length);

        for (int i = 0, n = dblArray.length; i < n; ++i)
        {
          v.add(new Double(dblArray[i]));
        }
      }

      else if ((strArray = exp.asStringArray()) != null)
      {
        v = new Vector(strArray.length);

        for (int i = 0, n = strArray.length; i < n; ++i)
        {
          v.add(strArray[i]);
        }
      }

      else if ((str = exp.asString()) != null)
      {
        v = new Vector(1);

        v.add(str);
      }

      else
      {
        strArray = R_ENGINE.eval("as.character(" + cmd + ")").asStringArray();

        v = new Vector(strArray.length);

        for (int i = 0, n = strArray.length; i < n; ++i)
        {
          v.add(strArray[i]);
        }
      }

      return v;
    }
    
    else
    {
      return null;
    }
  }
  
  public static String evalAsString(String cmd)
  {
    if (R_ENGINE == null)
    {
      return null;
    }
    
    REXP exp = R_ENGINE.eval(cmd);
    
    if (exp != null)
    {
      return exp.asString();
    }
    
    else
    {
      return null;
    }
  }
  
}
