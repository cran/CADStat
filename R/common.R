# Sets Visibility
setVisible <- function(jObj, bVisible)
{
  invisible(.jcall(jObj,,"setVisible",bVisible))
}

load.class = function(class){
  .stv <<- .jnew(class); 
  setVisible(.stv, TRUE)
}

jgr.removeMenu = function(name) 
{
 # jgr.removeMenu("Workspace")
    if (!.jgr.works) {
        cat("jgr.removeMenu() cannot be used outside JGR.\n")
        return(invisible(NULL))
    }
    invisible(.jcall(.jnew("org/neptuneinc/cadstat/JGRCustomizer"),"V", "removeMenu", as.character(name)))
}
