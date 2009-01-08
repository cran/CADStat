##
## This file contains a function that accept user input to select one of duplicate records
##   from itis table, assign OTU and then calculate maximum likelihood estimates
##   of environmental conditions
##
##   bencntFile -- a tab-delimited text file of benthic counts, has been assigned as object bencntMatrix
##   
##   tempstr -- user selected one string of duplicated itis records

##   taxonomicFile -- a tab-delimited text file for output of
##     the get.mismatch get.duplicates, and get.mergedfile functions, 
##     that merges taxonomy of thebenthic count names and the itis.ttable data
##     This file is written as output, unless runTaxonomic is FALSE,
##     in which case it is read as input instead.
##   otuFile -- a tab-delimited text file for output of the get.otu
##     function that constructs the operational taxonomic units, using
##     the results in taxonomicFile along with the tnames given in
##     coefficientFile.  This file is written as output, unless
##     runOTU is FALSE, in which case it is read as input instead.
##   mlFile -- a tab-delimited text file for output of the function
##     that contains the maximum likelihood estimates of environmental
##     conditions.


"bioinfer3.JGR" = function(
  tempstr=character(0), taxonomicFile="bcntTax.txt", otuFile="OTU.txt",
  mlFile="MaxLikEnv.txt" )
{
  globenv <- globalenv()

  data( itis.ttable )
  errorMessage = character()
  resultLocation = genResultSpace()
  ## Merge the taxon names from the input benthic count data with
  ##   the itis.ttable
  ## If this step has been done in previous runs, it is read from a file

  taxonomyMatrix = try(dup.sel(globenv[["bencntMatrix"]], itis.ttable, tempstr)) 
    if (isS4(taxonomyMatrix)) {
        return("Transfer to Java operation")
        } 
    if( inherits( taxonomyMatrix, "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure merge", taxonomicFile ),
                       "Errors occur during final check." ), resultLocation )
    }

    tmp = try( write.table( taxonomyMatrix, taxonomicFile, sep="\t" ) )

    if( inherits( tmp, "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure writing to file:", taxonomicFile ) ),
                    resultLocation )
    } else {
      errorMessage = c( errorMessage,
        paste( "Taxonomy merge completed, written to file:", taxonomicFile ) )
    }

  ## Generate operational taxonomic units
  ## If this step has been done in previous runs, it is read from a file

    otuMatrix = try( get.otu.jgr( taxonomyMatrix, optlist = globenv[["coefs"]]$tnames,
      outputFile=NULL ) )
    if( inherits( otuMatrix, "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure completing generation of operational",
                             "taxonomic units." ),
                       paste( "Check that file", taxonomicFile, "is formatted",
                             "properly." ) ),
                    resultLocation )
    }
    tmp = try( write.table( otuMatrix, otuFile, sep="\t" ) )
    if( inherits( tmp, "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Error trying to write operational taxonomic units",
                             "to", otuFile ) ), resultLocation )
    } else {
      errorMessage = c( errorMessage,
        "Generation of operational taxonomic units completed." )
    }
  
  
  ## Generate the site-OTU matrix, or read it in if this step
  ssMatrix = try( makess( otuMatrix, plothist=FALSE, prints=FALSE ) )
  if( inherits( ssMatrix, "try-error" ) ){
    localJGRError( c( errorMessage,
                  "Failure in generation of site-OTU matrix." ),
                  resultLocation )
  }
  
  ## Maximum likelihood estimation of environmental conditions
  mlMatrix = try( mlsolve( ssMatrix, globenv[["coefs"]] ) )
  if( inherits( mlMatrix, "try-error" ) ){
    localJGRError( c( errorMessage,
                  "Failure in generation of maximum likelihood estimates." ),
                  resultLocation )
  }
  
  ## Write inference to file
  tmp = try( write.table( mlMatrix, mlFile, sep="\t", row.names=FALSE ) )
  if( inherits( tmp, "try-error" ) ){
    localJGRError( c( errorMessage,
                  paste( "Failure writing to file:", mlFile ) ),
                  resultLocation )
  }
  
  ## Ready maximum likelihood estimates for output
  mlMatrix = as.data.frame(mlMatrix)
#  colnames(mlMatrix) = c("Site","Sediment","Temperature","Inconsistent")
  attr(mlMatrix,"title") =
    "Maximum Likelihood Estimates of Environmental Conditions by Site"

  ## Draw histogram
  png( file=file.path(resultLocation, "Abundance Histogram.png"),
      width=600, height=600 )
  par( mar=c(4,4,3,1) )
  plot( attr( ssMatrix, "histogram" ), xlab="Proportion of abundance", main="" )
  title( "Abundance Divided by Total Abundance", line=2 )
  title( "for Operational Taxonomic Units", line=1 )
  dev.off()
  
  ## Also send results to xml
  tmp = buildresultsXML( object=list(mlMatrix), location=resultLocation, rowNames=FALSE)

#  rm(list=c("bencntMatrix", "coefs","dfref", "dftemp2", "isav","sumstr"),inherits=T)
#  rm(list=ls()[-match("mlMatrix",ls())])
  ## Return final matrix
  return(mlMatrix)
}
