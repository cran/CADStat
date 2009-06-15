cor.JGR <- function(my.data, subset1.name, subset1.val, subset2.name, subset2.val,
          my.vars, iMethod="pearson", iCI=FALSE, conf.level=0.95, iScatterplot=FALSE, browserResults=FALSE)
{
  #computes a correlation matrix and confidence intervals for variables in my.vars
  
  #my.data    data.frame
  #subset1.name  column name of the first subsetting variable (panel)
  #subset1.val  values of the first subsetting variable
  #subset2.name  column name of the second subsetting variable (group)
  #subset2.val  values of the second subsetting variable
  #my.vars    vector of strings giving the variable names of interest
  #iMethod    indicates the computation method - pearson, kendall, spearman
  #iCI      indicates whether correlation confidence intervals should be computed
  #conf.level    confidence level
  #iScatterplot  indicates whether a scatterplot matrix should be drawn
  
  ## Get place to store results
  resultLocation = genResultSpace()

  n.vars <- length(my.vars)

  if (n.vars < 2) {
    print("")
    print("Please select at least two variables.")
    print("")
  } else {
    #NA handling
    iNA <- is.na(my.data[,my.vars[1]])
    for (i in 2:n.vars) iNA <- iNA | is.na(my.data[,my.vars[i]])
    my.subset <- my.data[!iNA,]
    
    #find proper data subset
    n.val1 <- length(subset1.val)
    n.val2 <- length(subset2.val)
    
    if (n.val1>0 & n.val2>0) {
      my.subset <- my.subset[my.subset[,subset1.name]%in%subset1.val & my.subset[,subset2.name]%in%subset2.val,]
    } else if (n.val1>0) {
      my.subset <- my.subset[my.subset[,subset1.name]%in%subset1.val,]
    } else if (n.val2>0) {
      my.subset <- my.subset[my.subset[,subset2.name]%in%subset2.val,]
    }
    
    #NOTE: the remainder of the code groups subset1.val values together and
    #groups subset2.val values together rather than perform computations for
    #individual subsets
    
    #output the correlation matrix
    print("Correlation Matrix")
    print(cor(my.subset[,my.vars], method=iMethod))
    cor.results <- as.data.frame(cor(my.subset[,my.vars], method=iMethod))
    
    #output coefficient CIs if necessary
    if (iCI) {
      #check iMethod and conf.level
      if (is.na(iMethod)) iMethod <- "pearson"
  
      if (!is.numeric(conf.level) | is.nan(conf.level)) {
        conf.level <- 0.95
      } else if (conf.level <= 0 | conf.level >= 1) {
        conf.level <- 0.95
      } 
  
      #create ci result matrix
      my.results <- data.frame(matrix(NA, nrow=n.vars*(n.vars-1)/2, ncol=4))
      names(my.results) <- c(" ","coefficient", "lower bound", "upper bound")
      
      count <- 1
      
      for (i in 1:(n.vars-1)){
        for (j in (i+1):n.vars){
          #row names
          my.results[count,1] <- paste(my.vars[i],my.vars[j],sep="-")
                    
          #confidence interval
          my.ci <- cor.test(my.subset[,my.vars[i]], my.subset[,my.vars[j]], method=iMethod, conf.level=conf.level)
          if (!is.null(my.ci)) {
            my.results[count,2] <- my.ci$estimate
            my.results[count,3] <- my.ci$conf.int[1]
            my.results[count,4] <- my.ci$conf.int[2]
          }
          count <- count + 1
        }
      }
      print("Confidence Interval Table")
      print(my.results)
    }
    
      ##output scatterplot matrix if necessary
      if (iScatterplot) {
        if(browserResults)
          png(file=file.path(resultLocation,"Scatterplot matrix.png"),width=600,height=600)
        else
          JavaGD(height=500,width=600)
        par(mar=c(4,4,2,1))
        my.formula <- paste("~", my.vars[1], sep="")
        for (i in 2:n.vars) my.formula <- paste(my.formula, "+", my.vars[i], sep="")
        pairs(as.formula(my.formula), data=my.subset)
        if(browserResults) dev.off()    
      }
    if (browserResults) {
      if(iCI){
        buildresultsXML(object=list(cor.results,my.results),location=resultLocation,
                        title="Correlation Analysis Summary")
      }else{
        buildresultsXML(object=list(cor.results),location=resultLocation,
                        title="Correlation Analysis Summary")
      }
    }
  }
  return(invisible())
}

