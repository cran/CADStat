#' @export
CADStat.help <- function(topic=NULL,doc="CADStat.JGR.html",doc.path=file.path("CADStat","doc"),home=.Library)
{
  ## CADStat.help will open an xml, html, xhtml, sxw, or pdf in a browser
  ## e.g., CADStat.help("speciestolerancevalues")
  if(!is.null(topic)){
    if(file.exists(file.path(.Library,"CADStat","doc",paste(topic,".html",sep="")))) {
      helpURL <- file.path(.Library,"CADStat","doc",paste(topic,".html",sep=""))
    }else{
      helpURL <- file.path(.Library,"CADStat","html",paste(topic,".html",sep=""))
    }
  }else{
     helpURL <- paste("file:///",file.path(home[1],doc.path,doc),sep="")
  }
  
# browseHelp(helpURL)
  browseURL(helpURL,browser=NULL)
  invisible("")
}

browseHelp <- function(url="about:blank")
{
  win.browser.path <- file.path(.Library, "CADStat","doc","browser","k-meleon.exe")
  
  if (.Platform$OS.type == 'windows' && file.exists(win.browser.path))
  {
    url <- chartr("/", "\\", url)

    win.browser.path <- file.path(.Library, "CADStat","doc","browser","k-meleon.exe")
    cmd <- paste("\"",win.browser.path,"\" \"",url,"\"",sep="")
    
    system(cmd, wait = FALSE)
  }
  
  else
  {
    browseURL(url)
  }
}

