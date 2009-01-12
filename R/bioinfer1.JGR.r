##
## This file contains a function that calls functions from the
##   former bio.infer package, to calculate maximum likelihood estimates
##   of environmental conditions based on a set of benthic counts
##
## Inputs are file names for input/output
##   coefficientFile -- a .rda file (saved R data) is expected.
##     This file contains regression coefficients.  It should
##     be provided in the bio.infer package or downloaded, though
##     it can be constructed using the bio.infer package.
##   bencntFile -- a tab-delimited text file of benthic counts,
##     containing (at least) 3 columns:
##     site name, genus/species name, and counts
##     See bio.infer documentation for details of formatting
##     This file is required if runTaxonomic is TRUE, and ignored
##     if runTaxonomic is FALSE.
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
##   runTaxonomic -- TRUE if the get.mismatch, get.duplicates, and get.mergedfile
##     functions are to be run. Setting to FALSE skips that step and reads in 
##     the results from a previous run of that step from the taxonomicFile.
##   runOTU -- TRUE if the get.otu function is to be run.  It must be
##     TRUE if runTaxonomic is TRUE, since the operational taxonomy
##     units depend on the output of get.taxonomic.  Setting runOTU
##     to FALSE skips the get.otu step and reads in the results from
##     a previous run of get.otu from the otuFile.

"bioinfer1.JGR" = function(
  coefficientFile,
  bencntFile="",
  runTaxonomic=TRUE, taxonomicFile="bcntTax.txt",
  runOTU=TRUE, otuFile="OTU.txt",
  mlFile="MaxLikEnv.txt" )
{

  globenv <- globalenv()

  ## Get place to store results
  resultLocation = genResultSpace()
  data( itis.ttable )

  ## Initial error check
  errorMessage = character()
  if( runTaxonomic & !runOTU ){
    localJGRError( paste( "If the taxonomic merge is being run, then the",
                         "\noperational taxonomy should be run as well" ),
                  resultLocation, geterr=FALSE )
  }
  
  ## Merge the taxon names from the input benthic count data with
  ##   the itis.ttable
  ## If this step has been done in previous runs, it is read from a file
  if( runTaxonomic ){
    globenv[["bencntMatrix"]] <- try( read.table( bencntFile, header=TRUE, sep="\t" ) )
    if( inherits( globenv[["bencntMatrix"]], "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure reading file: ", bencntFile, ".", sep="" ),
                       "Check that file is tab-delimited." ),
                    resultLocation )
    }
#    cat("Starting taxonomic merge.\n")
#    cat("If data editor appears, you may attempt to correct spellings\n")
#    cat("  by typing them in column 2, or just close the editor to continue\n")
#    cat("  without corrections.\n")
#    flush.console()
  }
  else if(runOTU){
    globenv[["taxonomyMatrix"]] <- try( read.table( taxonomicFile, header=TRUE, sep="\t" ) )
    if( inherits( globenv[["taxonomyMatrix"]], "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure reading taxonomic merge from file:",
                             taxonomicFile ),
                       "Check that file exists and is tab-delimited." ),
                    resultLocation )
    }
  }

  ## Load in coefficients file
  coefname = try( load( coefficientFile ) )
  if( inherits( coefname, "try-error" ) ){
    localJGRError( c( errorMessage,
                     paste("Error importing coefficients file:", coefficientFile),
                     paste("Check that it is an appropriate .rda",
                           "coefficient file.") ),
                  resultLocation )
  }
  globenv[["coefs"]] <- get( coefname )

  ## Generate operational taxonomic units
  ## If this step has been done in previous runs, it is read from a file
  
  if (!runOTU){
    globenv[["otuMatrix"]] <- try( read.table( otuFile, header=TRUE, sep="\t" ) )
    if( inherits( globenv[["otuMatrix"]], "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure reading operational taxonomic units from",
                          "file:", taxonomicFile ),
                    "Check that file exists and is tab-delimited." ),
                    resultLocation )
    }
  }
  if( runTaxonomic ) {
    globenv[["taxonomyMatrix"]] <- get.mismatch(globenv[["bencntMatrix"]], itis.ttable) 
    if (isS4(globenv[["taxonomyMatrix"]])) {
      return()
    }  else {
      tmp = try( write.table( globenv[["taxonomyMatrix"]], taxonomicFile, sep="\t" ) )

      if( inherits( tmp, "try-error" ) ){
        localJGRError( c( errorMessage,
                         paste( "Failure writing to file:", taxonomicFile ) ),
                      resultLocation )
      } else {
        errorMessage = c( errorMessage,
          paste( "Taxonomy merge completed, written to file:", taxonomicFile ) )
      }
    }
  }   
  if( runOTU ){
    globenv[["otuMatrix"]] <- try( get.otu.jgr( globenv[["taxonomyMatrix"]], optlist = globenv[["coefs"]]$tnames,
                                               outputFile=NULL ) )
    if( inherits( globenv[["otuMatrix"]], "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Failure completing generation of operational",
                             "taxonomic units." ),
                       paste( "Check that file", taxonomicFile, "is formatted",
                             "properly." ) ),
                    resultLocation )
    }
    tmp = try( write.table( globenv[["otuMatrix"]], otuFile, sep="\t" ) )
    if( inherits( tmp, "try-error" ) ){
      localJGRError( c( errorMessage,
                       paste( "Error trying to write operational taxonomic units",
                             "to", otuFile ) ),
                    resultLocation )
    } else {
      errorMessage = c( errorMessage,
        "Generation of operational taxonomic units completed." )
    }
  } 
######################################################################################  
  ## Generate the site-OTU matrix, or read it in if this step
  ssMatrix = try( makess( globenv[["otuMatrix"]], plothist=FALSE, prints=FALSE ) )
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
