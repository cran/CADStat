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
  jgr.removeMenu("Packages")
  #load.menus(libname)
  #CADStat.help(doc="CADStat.JGR.html",doc.path="CADStat/doc",home=.libPaths())
#}

#load.menus = function(libname)
#{
  require(XML)

  menu.file = file.path(libname,"CADStat","menu","menu.xml")
  menus.dom = xmlTreeParse(menu.file,useInternalNodes = TRUE)
  menus.node = getNodeSet(menus.dom,"/menus:menus/menus:menu",namespaces=c(menus="http://www.rforge.net/CADStat"))

  for(i.menus in 1:length(menus.node))
  {
    menu.name = unlist(getNodeSet(menus.node[[i.menus]],"/child::*/menus:name",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))

    jgr.addMenu(menu.name)
    menuitem.node = getNodeSet(menus.node[[i.menus]],"//menus:menuitem | //menus:separator",namespaces=c(menus="http://www.rforge.net/CADStat"))
    for(i.menuitem in 1:length(menuitem.node))
    {
      if (xmlName(menuitem.node[[i.menuitem]]) == "separator")
      {
        jgr.addMenuSeparator(menu.name)
      }

      else
      {
        menuitem.name = unlist(getNodeSet(menuitem.node[[i.menuitem]],"/child::*/menus:name",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))
        menuitem.command = unlist(getNodeSet(menuitem.node[[i.menuitem]],"/child::*/menus:command",xmlValue,namespaces=c(menus="http://www.rforge.net/CADStat")))
        jgr.addMenuItem(menu.name,menuitem.name,menuitem.command)
      }
    }
  }
}

