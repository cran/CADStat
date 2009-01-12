\name{get.duplicates}
\alias{get.duplicates}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
  This function follows get.mismatch function after user corrects mismatched taxa in the java
  window and click ok; it will send the corrected taxa names back to tmiss1
}
\usage{
get.duplicates(bcnt, tmiss1 = character(0), itis.ttable)
}
\arguments{
  \item{bcnt}{a benthic sample count matrix including 3 columns: Sample ID, genus/species name, and counts }
  \item{tmiss1}{a vector of unrecognized taxa checked by users and return back from java interface}
  \item{itis.ttable}{ taxonomic master table downloaded from itis website }
}
\seealso{ 
	\code{\link{bioinfer1.JGR}},
	\code{\link{bioinfer2.JGR}},
	\code{\link{bioinfer3.JGR}},
	\code{\link{JGRMessageBox}},
	\code{\link{get.mergedfile}},
	\code{\link{get.mismatch}},
	\code{\link{dup.sel}},
	\code{\link{get.otu.jgr}}
}
\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (bcnt, tmiss1 = character(0), itis.ttable) 
{
    if (length(tmiss1) > 0) {
        tmiss0 <- character(0)
        for (i in 1:length(dfref$f2)) {
            if (!(dfref$f2[i] \%in\% itis.ttable$TAXON)) {
                tmiss0 <- c(tmiss0, dfref$f2[i])
            }
        }
        tmiss0 <- sort(unique(tmiss0))
        dfcorrect <- data.frame(I(tmiss0), I(tmiss1))
        names(dfcorrect) <- c("TAXANAME", "CORRECTION")
        dfcorrect$CORRECTION <- toupper(dfcorrect$CORRECTION)
        for (i in 1:nrow(dfcorrect)) {
            incvec <- dfref$f2 == dfcorrect$TAXANAME[i]
            if (!is.na(dfcorrect$CORRECTION[i])) {
                imatch <- match(toupper(dfcorrect$CORRECTION[i]), 
                  itis.ttable$TAXON)
                if (is.na(imatch)) {
                  if (dfcorrect$CORRECTION[i] != "") {
                    cat(dfcorrect$CORRECTION[i], " is not found in ITIS.\n")
                  }
                }
                else {
                  dfref$f2[incvec] <- dfcorrect$CORRECTION[i]
                  specpres <- dfref$sp.name != ""
                  if (sum(specpres & incvec) > 0) {
                    isel <- specpres & incvec
                    if (itis.ttable$GENUS[imatch] == "") {
                      dfref$sp.name[isel] <- ""
                    }
                  }
                }
            }
        }
        dfref <<- dfref
    }
    f.tname <- (names(bcnt))[2]
    tlevs <- names(itis.ttable)
    imatch <- match("TAXON", tlevs)
    tlevs <- tlevs[-imatch]
    df1 <- merge(dfref, itis.ttable, by.x = "f2", by.y = "TAXON", 
        all.x = TRUE)
    nomatch <- is.na(df1[, tlevs[1]])
    df2 <- merge(bcnt, dfref, by.x = f.tname, by.y = "f1", all.x = T)
    df2 <- merge(df2, itis.ttable, by.x = "f2", by.y = "TAXON", 
        all.x = T)
    incvec <- is.na(df2[, tlevs[1]])
    if (sum(incvec) > 0) {
        tmiss <- sort(unique(df2[incvec, "f2"]))
        sumnumocc <- rep(NA, times = length(tmiss))
        tlen <- length(tmiss)
        for (i in 1:length(tmiss)) {
            sumnumocc[i] <- sum(df2[, "f2"] == tmiss[i])
        }
        dfsumnumocc <- data.frame(I(tmiss), sumnumocc)
        names(dfsumnumocc) <- c("TAXANAME", "NUMBER OF OCCURRENCES")
        cat("Summary of taxa without matches: \n")
        format(dfsumnumocc, justify = "centre")
        print(dfsumnumocc)
    }
    flush.console()
    dftemp <- data.frame(I(unique(dfref$f2)))
    names(dftemp) <- c("f2")
    dftemp2 <<- merge(itis.ttable, dftemp, by.x = "TAXON", by.y = "f2", 
        all.y = TRUE)
    reps <- unique(dftemp2$TAXON[duplicated(dftemp2$TAXON)])
    if (length(reps) > 0) {
        replist <- dftemp2$TAXON[reps == dftemp2$TAXON]
        sumstr <- rep("", times = length(replist))
        isav <<- (1:nrow(dftemp2))[reps == dftemp2$TAXON]
        print(dftemp2[isav, c("TAXON", "PHYLUM", "CLASS", "ORDER", 
            "FAMILY")])
        for (i in 1:length(replist)) {
            sumstr[i] <- paste(dftemp2[isav[i], c("TAXON", "PHYLUM", 
                "CLASS", "ORDER", "FAMILY")], collapse = "-")
        }
        sumstr <<- sumstr
        d <- .jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameDupITIS")
        .jcall(d, "Ljavax/swing/JFrame;", "getMyGUI", length(sumstr), 
            sumstr, 0)
    }
    else {
        get.mergedfile(bcnt, itis.ttable, selstr = tempstr, outputFile = NULL)
    }
  }
}
\keyword{regression}

