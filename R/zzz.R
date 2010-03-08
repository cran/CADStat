.First.lib <- function(libname, pkgname)
{
  cat(libname,"\n")
  .First.sys()
  options(stringsAsFactors=FALSE)
  
  .jpackage("CADStat")
  #.jcall(.jnew("org/neptuneinc/cadstat/JGRCustomizer"),,"customize")
  jgr.removeMenu("About")
  jgr.removeMenu("Preferences")
  jgr.removeMenu("Help")
  jgr.removeMenu("Window")
  #jgr.removeMenu("Packages")
  jgr.addMenuSeparator("Edit")
  jgr.addMenuItem("Edit", "JGR Preferences",
                  ".jnew('org/rosuda/JGR/toolkit/PrefsDialog')")
  jgr.addMenuItem("Edit", "CADStat Preferences",
                  "load.class('org/neptuneinc/cadstat/prefs/PreferencesDialog')")
  jgr.addMenuSeparator("Packages & Data")
  jgr.addMenuItem("Packages & Data", "Data Merge",
                  "load.class('org/neptuneinc/cadstat/plots/DataMergeDialog')")
  jgr.addMenuSeparator("Workspace")
  jgr.addMenuItem("Workspace", "Clean CADStat File Workspace",
                  "cleanCADStatWorkspace()")
  jgr.addMenu("Graph")
  jgr.addMenuItem("Graph", "Boxplot",
                  "load.class('org/neptuneinc/cadstat/plots/BoxPlot')")
  jgr.addMenuItem("Graph", "Scatterplot",
                  "load.class('org/neptuneinc/cadstat/plots/ScatterPlot')")
  jgr.addMenu("Analysis Tools")
  jgr.addMenuItem("Analysis Tools", "Factor Analysis",
                  "load.class('org/neptuneinc/cadstat/plots/FactorAnalysis')")
  jgr.addMenuItem("Analysis Tools", "Linear Regression",
                  "load.class('org/neptuneinc/cadstat/plots/LinearRegression')")
  jgr.addMenuItem("Analysis Tools", "Regression Prediction",
                  "load.class('org/neptuneinc/cadstat/plots/RegressionPrediction')")
  jgr.addMenuItem("Analysis Tools", "Quantile Regression",
                  "load.class('org/neptuneinc/cadstat/plots/QuantileRegression')")
  jgr.addMenuItem("Analysis Tools", "Correlation Analysis",
                  "load.class('org/neptuneinc/cadstat/plots/CorrelationAnalysis')")
  jgr.addMenuItem("Analysis Tools", "Tree Regression",
                  "load.class('org/neptuneinc/cadstat/plots/TreeRegression')")
  jgr.addMenuSeparator("Analysis Tools")
  jgr.addMenuItem("Analysis Tools", "Conditional Probability",
                  "load.class('org/neptuneinc/cadstat/plots/ConditionalProbability')")
  jgr.addMenuItem("Analysis Tools", "Predicting Environmental Conditions from Biological Observations",
      "load.class('org/neptuneinc/cadstat/plots/BiologicalInferences')")
  jgr.addMenuItem("Analysis Tools", "Calculate Trait Metrics",
      "load.class('org/neptuneinc/cadstat/plots/TraitInferences')")
  jgr.addMenu("Help")
  jgr.addMenuItem("Help", "R Help", "help.start()")
  jgr.addMenuSeparator("Help")
  jgr.addMenuItem("Help", "Loading and merging data",
#                  "help(doc='loaddata.html')")
                  "CADStat.help(doc='loaddata.html')")
  jgr.addMenuItem("Help", "Boxplot",
#                  "help('boxplot.JGR')")
                  "CADStat.help(doc='boxplot.JGR.html')")
  jgr.addMenuItem("Help", "Scatterplot",
#                   "help('scatterplot.JGR')")
                   "CADStat.help(doc='scatterplot.JGR.html')")
  jgr.addMenuItem("Help", "Linear Regression",
                  "CADStat.help(doc='lm.JGR.html')")
  jgr.addMenuItem("Help", "Regression Prediction",
                  "CADStat.help(doc='glm.pred.JGR.html')")
  jgr.addMenuItem("Help", "Quantile Regression",
                  "CADStat.help(doc='rq.JGR.html')")
  jgr.addMenuItem("Help", "Correlation Analysis",
                  "CADStat.help(doc='cor.JGR.html')")
  jgr.addMenuItem("Help", "Factor Analysis/PCA",
                  "CADStat.help(doc='pca.fa.JGR.html')")
  jgr.addMenuItem("Help", "Regression Trees",
                  "CADStat.help(doc='rpart.JGR.html')")
  jgr.addMenuItem("Help", "Conditional Probability",
                  "CADStat.help(doc='conditionalprob.JGR.html')")
  jgr.addMenuItem("Help", "Predicting Env. Conditions from Biological Observations",
                  "CADStat.help(doc='bioinfer.JGR.html')")
  jgr.addMenuItem("Help", "Calculating Trait Metrics",
                  "CADStat.help(doc='trait.stat.JGR.html')")
  jgr.addMenu("About")
  jgr.addMenuItem("About", "About JGR",
                  ".jnew('org/rosuda/JGR/toolkit/AboutDialog')")
  jgr.addMenuItem("About", "About CADStat",
                  "CADStat.help(doc='CADStat.JGR.html')")

#  require(XML)

#  menu.file = file.path(libname,"CADStat","menu","menu.xml")
#  menus.dom = xmlTreeParse(menu.file,useInternalNodes = TRUE)
#  menus.node = getNodeSet(menus.dom,"/menus:menus/menus:menu",namespaces=c(menus="http://www.rforge.net/CADStat"))

#  for(i.menus in 1:length(menus.node))
#  {
#    menu.name = unlist(getNodeSet(menus.node[[i.menus]],"/child::*/menus:name",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))

#    jgr.addMenu(menu.name)
#    menuitem.node = getNodeSet(menus.node[[i.menus]],"//menus:menuitem | //menus:separator",namespaces=c(menus="http://www.rforge.net/CADStat"))
#    for(i.menuitem in 1:length(menuitem.node))
#    {
#      if (xmlName(menuitem.node[[i.menuitem]]) == "separator")
#      {
#        jgr.addMenuSeparator(menu.name)
#      }

#      else
#      {
#        menuitem.name = unlist(getNodeSet(menuitem.node[[i.menuitem]],"/child::*/menus:name",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))
#        menuitem.command = unlist(getNodeSet(menuitem.node[[i.menuitem]],"/child::*/menus:command",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))
#        jgr.addMenuItem(menu.name,menuitem.name,menuitem.command)
#      }
#    }
#  }
}

