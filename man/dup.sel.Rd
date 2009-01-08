\name{dup.sel}
\alias{dup.sel}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
  	This function is used to check duplicate records after user select records from a JGR message box 
}
\usage{
	dup.sel(bcnt, itis.ttable, tempstr)
}
\arguments{
  \item{bcnt}{ the benthic count matrix  }
  \item{itis.ttable}{ Taxonomic table from ITIS website }
  \item{tempstr}{ user selected string from java interface }
}
\seealso{ 
	\code{\link{bioinfer1.JGR}},
	\code{\link{bioinfer2.JGR}},
	\code{\link{bioinfer3.JGR}},
	\code{\link{JGRMessageBox}},
	\code{\link{get.mergedfile}},
	\code{\link{get.mismatch}},
	\code{\link{get.duplicates}},
	\code{\link{get.otu.jgr}}
}
\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (bcnt, itis.ttable, tempstr) 
{
    reps <- unique(dftemp2$TAXON[duplicated(dftemp2$TAXON)])
    taxalist <- (substr(tempstr, 1, regexpr("-", tempstr) - 1))
    unmatch <- length(reps[!(reps \%in\% unique(taxalist))])
    if (unmatch > 0 | length(reps) != length(taxalist)) {
        d <- .jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameDupITIS")
        .jcall(d, "Ljavax/swing/JFrame;", "getMyGUI", length(sumstr), 
            sumstr, 1)
    }
    else {
        get.mergedfile(bcnt, itis.ttable, selstr = tempstr, outputFile = NULL)
    }
  }
}
\keyword{regression}

