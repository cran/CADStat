\name{bioinfer1.JGR}
\alias{bioinfer1.JGR}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
  This module estimates environmental conditions from observations of
  benthic macroinvertebrates for a set of sites, by combining
  observations of each particular taxon with a taxon-environment
  relationship.

  This calculation has three steps.  First, observations
  of benthic macroinvertebrates are matched with standard taxonomic
  names, with a full taxonomic hierarchy.  Second, operational taxonomic
  units (OTUs) are defined for the data set, so that each individual
  taxon is counted at a consistent taxonomic level.  Third, observations
  are matched with existing taxon-environment relationships and an
  estimate of environmental conditions computed. 

  This file contains a function that calls functions from the
  former bio.infer package, to calculate maximum likelihood estimates
  of environmental conditions based on a set of benthic counts.
}
\usage{
bioinfer1.JGR(coefficientFile, bencntFile = "", runTaxonomic = TRUE, taxonomicFile = "bcntTax.txt", runOTU = TRUE, otuFile = "OTU.txt", mlFile = "MaxLikEnv.txt")
}
\arguments{
  \item{coefficientFile}{ 
	a .rda file (saved R data) is expected.
      This file contains regression coefficients.  It should
      be provided in the bio.infer package or downloaded, though
      it can be constructed using the bio.infer package.
  }
  \item{bencntFile}{
	a tab-delimited text file of benthic counts,
      containing (at least) 3 columns:
      site name, genus/species name, and counts
      See bio.infer documentation for details of formatting
      This file is required if runTaxonomic is TRUE, and ignored
      if runTaxonomic is FALSE.
  }
  \item{runTaxonomic}{ 
	TRUE if the get.mismatch, get.duplicates, and get.mergedfile
      functions are to be run. Setting to FALSE skips that step and reads in 
      the results from a previous run of that step from the taxonomicFile.
  }
  \item{taxonomicFile}{
	a tab-delimited text file for output of
      the get.mismatch get.duplicates, and get.mergedfile functions, 
      that merges taxonomy of thebenthic count names and the itis.ttable data
      This file is written as output, unless runTaxonomic is FALSE,
      in which case it is read as input instead.
  }
  \item{runOTU}{
	TRUE if the get.otu function is to be run.  It must be
      TRUE if runTaxonomic is TRUE, since the operational taxonomy
      units depend on the output of get.taxonomic.  Setting runOTU
      to FALSE skips the get.otu step and reads in the results from
      a previous run of get.otu from the otuFile.
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
\details{
  \strong{User Interface}

	Select \emph{Analysis Tools -> Predicting env. conditions from
	  biological observations} from the menus.

	The Data Directory can be specified to indicate the default
	location that you wish to search for input files and write
	output files.  Unless the browse buttons for individual files
	are used to specify other directories, all input and output
	comes from this directory. 
	Note: Changing the data directory does not change the working directory
	for R, only for the default locations of the input/output files for this
	module.

	There are three options for starting points for running this module.  By
	default, the entire process should be run.  However, since the first two
	steps of the process may require some feedback, these steps can be
	skipped in subsequent runs to reduce user effort.

	\bold{Run Entire Process:} This option is the default setting.  It
	begins with the benthic macroinvertebrate count file and runs all steps
	required for: merging taxonomy with a standard taxonomic hierarchy from
	the Integrated Taxonomic Information System (ITIS), generating OTUs
	corresponding to the coefficient file, and computing maximum likelihood
	estimates of environmental conditions.  Output files include an merged
	taxonomic file, an OTU file, and a maximum likelihood file.

	\bold{Start with Merge Taxonomic Merge File} This option assumes that
	the tool has been run previously to merge taxonomy of the benthic
	macroinvertebrate count file with a standard taxonomic hierarchy.  The
	benthic macroinvertebrate file is not necessary for input, as the merged
	taxonomic file is read as input for the starting point.  Output files
	include an OTU file and a maximum likelihood file.  This option is
	recommended only for experienced users.

	\bold{Start with Operational Taxonomy File} This option assumes that the
	tool has been run previously to merge taxonomy of the benthic
	macroinvertebrate count file with a standard taxonomic hierarchy and to
	generate OTUs.  The benthic macroinvertebrate file and the taxonomic
	merge file are not necessary for input, as the OTU file is read as input
	for the starting point.  Only one output file is generated: the maximum
	likelihood file.  This option is recommended only for experienced
	users.

	\strong{Input files}

	\bold{Coefficients file:}  This file is an R data file (e.g., .rda or
	.rdata) that contains the taxon-environment relationships.  At the
	present time only one coefficient file is available: coef.west.wt.rda,
	which provides relationships for stream temperature and percent
	substrate sand/fines for macroinvertebrates of western U.S. streams.
	Select this file from the default directory that appears when the Browse
	button is selected.

	\bold{Benthic Count File:} This file contains the observations of
	benthic macroinvertebrate.  The file should be supplied as tab-delimited
	text.  Three columns in the following order are required: (1) Sample
	identifier, (2) taxon name, and (3) taxon abundance.  

	\bold{Taxonomic Merge File:} If Run Entire Process is chosen, this file
	is the result of the merge between local macroinvertebrate observations
	and a standard taxonomic hierarchy.  If \bold{Start with Merge Taxonomic Merge
	File} is chosen, this file is the starting input file. The format of the
	file is tab-delimited text.

	Operational Taxonomy File:  If \bold{Start with Operational Taxonomy
  	File} is chosen, it is the starting input file.  See above.

	\bold{Output file}

	\bold{Maximum likelihood File:} This file is the saved output of this module.
	It is a tab-delimited text file, with at least three columns: 1) Site
	name; 2) Maximum likelihood estimate of environmental conditions; 3) An
	indicator of whether or not multiple maximum likelihood estimates were
	found. \bold{TRUE} here indicates that global convergence was not
	attained.  The number of columns may increase, depending on how many
	environmental parameters are estimated simultaneously.
}
\seealso{ 
	\code{\link{bioinfer2.JGR}},
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
function (coefficientFile, bencntFile = "", runTaxonomic = TRUE, 
    taxonomicFile = "bcntTax.txt", runOTU = TRUE, otuFile = "OTU.txt", 
    mlFile = "MaxLikEnv.txt") 
{
    resultLocation = genResultSpace()
    data(itis.ttable)
    errorMessage = character()
    if (runTaxonomic & !runOTU) {
        localJGRError(paste("If the taxonomic merge is being run, then the", 
            "\noperational taxonomy should be run as well"), 
            resultLocation, geterr = FALSE)
    }
    if (runTaxonomic) {
        bencntMatrix <<- try(read.table(bencntFile, header = TRUE, 
            sep = "\t"))
        if (inherits(bencntMatrix, "try-error")) {
            localJGRError(c(errorMessage, paste("Failure reading file: ", 
                bencntFile, ".", sep = ""), "Check that file is tab-delimited."), 
                resultLocation)
        }
        cat("Starting taxonomic merge.\n")
        cat("If data editor appears, you may attempt to correct spellings\n")
        cat("  by typing them in column 2, or just close the editor to continue\n")
        cat("  without corrections.\n")
        flush.console()
    }
    else {
        taxonomyMatrix <<- try(read.table(taxonomicFile, header = TRUE, 
            sep = "\t"))
        if (inherits(taxonomyMatrix, "try-error")) {
            localJGRError(c(errorMessage, paste("Failure reading taxonomic merge from file:", 
                taxonomicFile), "Check that file exists and is tab-delimited."), 
                resultLocation)
        }
    }
    coefname = try(load(coefficientFile))
    if (inherits(coefname, "try-error")) {
        localJGRError(c(errorMessage, paste("Error importing coefficients file:", 
            coefficientFile), paste("Check that it is an appropriate .rda", 
            "coefficient file.")), resultLocation)
    }
    coefs <<- get(coefname)
    if (!runOTU) {
        otuMatrix <<- try(read.table(otuFile, header = TRUE, 
            sep = "\t"))
        if (inherits(otuMatrix, "try-error")) {
            localJGRError(c(errorMessage, paste("Failure reading operational taxonomic units from", 
                "file:", taxonomicFile), "Check that file exists and is tab-delimited."), 
                resultLocation)
        }
    }
    if (runTaxonomic) {
        taxonomyMatrix <<- get.mismatch(bencntMatrix, itis.ttable)
        if (isS4(taxonomyMatrix)) {
            return("Transfer to Java operation")
        }
        else {
            tmp = try(write.table(taxonomyMatrix, taxonomicFile, 
                sep = "\t"))
            if (inherits(tmp, "try-error")) {
                localJGRError(c(errorMessage, paste("Failure writing to file:", 
                  taxonomicFile)), resultLocation)
            }
            else {
                errorMessage = c(errorMessage, paste("Taxonomy merge completed, written to file:", 
                  taxonomicFile))
            }
        }
    }
    if (runOTU) {
        otuMatrix <<- try(get.otu.jgr(taxonomyMatrix, optlist = coefs$tnames, 
            outputFile = NULL))
        if (inherits(otuMatrix, "try-error")) {
            localJGRError(c(errorMessage, paste("Failure completing generation of operational", 
                "taxonomic units."), paste("Check that file", 
                taxonomicFile, "is formatted", "properly.")), 
                resultLocation)
        }
        tmp = try(write.table(otuMatrix, otuFile, sep = "\t"))
        if (inherits(tmp, "try-error")) {
            localJGRError(c(errorMessage, paste("Error trying to write operational taxonomic units", 
                "to", otuFile)), resultLocation)
        }
        else {
            errorMessage = c(errorMessage, "Generation of operational taxonomic units completed.")
        }
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
    return(mlMatrix)
  }
}
\keyword{regression}


