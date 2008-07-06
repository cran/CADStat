boxplot.JGR <- function(x, result, group.name=NULL, group.val=NULL,
                        subset1.name=NULL, subset1.val=NULL, subset2.name=NULL,
                        subset2.val=NULL, main="", result.lab="",
                        plot.type="both", cex.main=1, cex.lab=1, cex=1,
                        cex.axis=1, horizontal=FALSE,iSampleSize=TRUE, names.rot=FALSE, ...)
{
  # draws boxplots using the JGR dialog box ui input
  
  # x		data.frame
  # subset1.name	column name of the first subsetting variable (panel)
  # subset1.val	values of the first subsetting variable
  # subset2.name	column name of the second subsetting variable (group)
  # subset2.val	values of the second subsetting variable
  # result			column name containing the data to be plotted
  # main			plot title
  # ylab			y-axis label
  # plot.type		indicates the desired scale - "original", "log", or "both"
  # cex.main		magnification of the plot title
  # cex.lab		magnification of the axes labels
  # cex			magnification of the plotted points and lines
  # cex.axis		magnification of the axes
  # names.rot		indicates whether y-axis labels should be rotated 90 degrees
  
  # Get place to store results
  require(lattice)
  
  resultLocation = genResultSpace()
  
  # find proper data subset

  x = gisdt.subset(x, 
                         subset1.name=subset1.name, subset1.val=subset1.val, 
                         subset2.name=subset2.name, subset2.val=subset2.val,
						 na.check=c(result,group.name))

  x$scale = "Original"
  if (plot.type=="both" | plot.type == "log") {
    if(all(x[,result]>0)){
      if (plot.type =="both") {
        tmp = x
        tmp$scale = "Log"
        tmp[,result] = log(x[,result])
        x = rbind(x,tmp)
        rm(tmp)
      }
      else {
        x$scale = "Log"
        x[,result] = log(x[,result])
      }
    }
    else {
      plot(c(0,1),c(0,1),axes=FALSE,type="n",ann=FALSE);
      text(c(0.5,0.5),c(0.5,0.5),"The Result values all \n need to be positive \n to log transform the data",cex=2)
      return()
    }
  }

  if(!is.null(group.name)) {
    x[,group.name] =  as.character(x[,group.name])
    if(length(group.val)>0)
      x = subset(x,get(group.name) %in% group.val)
    if(horizontal){ bw.formula = formula(get(group.name)~get(result)|scale) 
    }else{ bw.formula = formula(get(result)~get(group.name)|scale) }
  } else {
    x$bw_x = result
    if(horizontal) { bw.formula = formula(bw_x~get(result)|scale)
    }else{ bw.formula = formula(get(result)~bw_x|scale) }
  }

  bw.obj = bwplot(bw.formula,data=x,
                  panel = function(x,y,...){
                    panel.bwplot(x,y,...)
                    if(iSampleSize)
                      if(!horizontal)
                        panel.text(unique(x),current.panel.limits()$ylim[1],label=paste("n=",rowSums(table(x,y)),sep=""),pos=3,cex=.75)#,adj = c(-0.5, -0.5))
                      else
                        panel.text(current.panel.limits()$xlim[1],unique(y),label=paste("n=",rowSums(table(y,x)),sep=""),cex=.75,adj = c(-0.5,7.5))
                  },
                  cex=cex,
                  strip=!(length(unique(x$scale))==1 & unique(x$scale)=="Original"),
                  ylab=ifelse(horizontal,"",list(result.lab,cex.lab=cex.lab)), 
                  xlab=ifelse(horizontal,list(result.lab,cex.lab=cex.lab)," "),
                  main=list(label=main,cex=cex.main),
		          horizontal=horizontal,
                  scale=list(y=list(relation='free',rot=0,cex=cex.axis),
                  x=list(rot=as.numeric(names.rot)*90,cex=cex.axis)))

  JavaGD(height=500,width=600)
  print(bw.obj)

  return(invisible())
}
