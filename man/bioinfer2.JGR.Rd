\name{bioinfer2.JGR}
\alias{bioinfer2.JGR}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
  This file contains a function that calls functions from get.duplicates function,
  to calculate maximum likelihood estimates of environmental conditions based on
  a set of benthic counts.

  A global variable is required from bioinfer1 reading bcnt matrix.
}
\usage{
bioinfer2.JGR(tmiss1 = character(0), taxonomicFile = "bcntTax.txt", otuFile = "OTU.txt", mlFile = "MaxLikEnv.txt")
}
\arguments{
  \item{tmiss1}{a vector by user input to correct mismatched taxa}
  \item{taxonomicFile}{
	a tab-delimited text file for output of
      the get.mismatch get.duplicates, and get.mergedfile functions, 
      that merges taxonomy of thebenthic count names and the itis.ttable data
      This file is written as output, unless runTaxonomic is FALSE,
      in which case it is read as input instead.
  }
  \item{otuFile}{
	a tab-delimited text file for output of the get.otu
      function that constructs the operational taxonomic units, using
      the results in taxonomicFile along with the tnames given in
      coefficientFile.  This file is written as output, unless
      runOTU is FALSE, in which case it is read as input instead.
  }
  \item{mlFile}{
  	a tab-delimited text file for output of the function
      that contains the maximum likelihood estimates of environmental
      conditions.
  }
}
\seealso{
	\code{\link{bioinfer1.JGR}},
	\code{\link{bioinfer3.JGR}},
	\code{\link{JGRMessageBox}},
	\code{\link{get.mergedfile}},
	\code{\link{get.mismatch}},
	\code{\link{dup.sel}},
	\code{\link{get.duplicates}},
	\code{\link{get.otu.jgr}}
}
\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (tmiss1 = character(0), taxonomicFile = "bcntTax.txt", 
    otuFile = "OTU.txt", mlFile = "MaxLikEnv.txt") 
{
    data(itis.ttable)
    errorMessage = character()
    resultLocation = genResultSpace()
    taxonomyMatrix = try(get.duplicates(bencntMatrix, tmiss1 = character(0), 
        itis.ttable))
    if (isS4(taxonomyMatrix)) {
        return("Transfer to Java operation")
    }
    if (inherits(taxonomyMatrix, "try-error")) {
        localJGRError(c(errorMessage, "Failure completing taxonomy merge.", 
            "Error occurs when checking ITIS duplicates."), resultLocation)
    }
    tmp = try(write.table(taxonomyMatrix, taxonomicFile, sep = "\t"))
    print(tmp)
    if (inherits(tmp, "try-error")) {
        localJGRError(c(errorMessage, paste("Failure writing to file:", 
            taxonomicFile)), resultLocation)
    }
    else {
        errorMessage = c(errorMessage, paste("Taxonomy merge completed, written to file:", 
            taxonomicFile))
    }
    otuMatrix = try(get.otu.jgr(taxonomyMatrix, optlist = coefs$tnames, 
        outputFile = NULL))
    if (inherits(otuMatrix, "try-error")) {
        localJGRError(c(errorMessage, paste("Failure completing generation of operational", 
            "taxonomic units."), paste("Check that file", taxonomicFile, 
            "is formatted", "properly.")), resultLocation)
    }
    tmp = try(write.table(otuMatrix, otuFile, sep = "\t"))
    if (inherits(tmp, "try-error")) {
        localJGRError(c(errorMessage, paste("Error trying to write operational taxonomic units", 
            "to", otuFile)), resultLocation)
    }
    else {
        errorMessage = c(errorMessage, "Generation of operational taxonomic units completed.")
    }
    ssMatrix = try(makess(otuMatrix, plothist = FALSE, prints = FALSE))
    if (inherits(ssMatrix, "try-error")) {
        localJGRError(c(errorMessage, "Failure in generation of site-OTU matrix."), 
            resultLocation)
    }
    mlMatrix = try(mlsolve(ssMatrix, coefs))
    if (inherits(mlMatrix, "try-error")) {
        localJGRError(c(errorMessage, "Failure in generation of maximum likelihood estimates."), 
            resultLocation)
    }
    tmp = try(write.table(mlMatrix, mlFile, sep = "\t", row.names = FALSE))
    if (inherits(tmp, "try-error")) {
        localJGRError(c(errorMessage, paste("Failure writing to file:", 
            mlFile)), resultLocation)
    }
    mlMatrix = as.data.frame(mlMatrix)
    colnames(mlMatrix) = c("Site", "Sediment", "Temperature", 
        "Inconsistent")
    attr(mlMatrix, "title") = "Maximum Likelihood Estimates of Environmental Conditions by Site"
    png(file = file.path(resultLocation, "Abundance Histogram.png"), 
        width = 600, height = 600)
    par(mar = c(4, 4, 3, 1))
    plot(attr(ssMatrix, "histogram"), xlab = "Proportion of abundance", 
        main = "")
    title("Abundance Divided by Total Abundance", line = 2)
    title("for Operational Taxonomic Units", line = 1)
    dev.off()
    tmp = buildresultsXML(object = list(mlMatrix), location = resultLocation, 
        rowNames = FALSE)
    rm(list = c("bencntMatrix", "coefs", "dfref", "dftemp2", 
        "isav", "sumstr"), inherits = T)
    return(mlMatrix)
  }
}
\keyword{regression}

