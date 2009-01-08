\name{get.mergedfile}
\alias{get.mergedfile}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
    This function accepts user selected taxa list following dup.sel function and 
  java operation, combines updated  benthic count table with itis table and save to
  a file.
}
\usage{
get.mergedfile(bcnt, itis.ttable, selstr = character(0), outputFile = "sum.tax.table.txt")
}
\arguments{
  \item{bcnt}{a benthic sample count matrix including 3 columns: Sample ID, genus/species name, and counts}
  \item{itis.ttable}{ taxonomic master table downloaded from itis website }
  \item{selstr}{ user selected string from java window for single records of duplicated taxa }
  \item{outputFile}{ a file merges taxonomy of the benthic count names and the itis.ttable data }
}
\seealso{ 
	\code{\link{bioinfer1.JGR}},
	\code{\link{bioinfer2.JGR}},
	\code{\link{bioinfer3.JGR}},
	\code{\link{JGRMessageBox}},
	\code{\link{get.mismatch}},
	\code{\link{dup.sel}},
	\code{\link{get.duplicates}},
	\code{\link{get.otu.jgr}}
}\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (bcnt, itis.ttable, selstr = character(0), outputFile = "sum.tax.table.txt") 
{
    reps <- unique(dftemp2$TAXON[duplicated(dftemp2$TAXON)])
    if (length(reps) > 0) {
        isel <- match(selstr, sumstr)
        isav <- isav[-isel]
        dftemp2 <- dftemp2[-isav, ]
    }
    itis.ttable.loc <- dftemp2[, names(itis.ttable)]
    f.tname <- (names(bcnt))[2]
    tlevs <- names(itis.ttable)
    imatch <- match("TAXON", tlevs)
    tlevs <- tlevs[-imatch]
    iomit <- numeric(0)
    for (i in 1:length(tlevs)) {
        if (sum(itis.ttable.loc[, tlevs[i]] != "", na.rm = T) == 
            0) {
            iomit <- c(iomit, i)
        }
    }
    if (length(iomit) > 0) {
        tlevs <- tlevs[-iomit]
    }
    df1 <- merge(dfref, itis.ttable.loc, by.x = "f2", by.y = "TAXON", 
        all.x = TRUE)
    df1$SPECIES <- paste(df1$GENUS, df1$sp.name, sep = ".")
    incvec <- (nchar(df1$sp.name) > 0) & (df1$GENUS != "")
    df1$SPECIES[!incvec] <- ""
    dfref <- df1[, c("f1", "f2", "SPECIES")]
    df1 <- df1[, c(tlevs, "SPECIES", "f1")]
    df1 <- df1[do.call(order, df1), ]
    names(df1) <- c(tlevs, "SPECIES", f.tname)
    if (is.character(outputFile)) {
        write.table(df1, sep = "\t", file = outputFile, row.names = F)
        JGRMessageBox(msg = paste("Check final taxa name assignments in", 
            outputFile), w.title = "BiologicalInferences: Info")
    }
    df2 <- merge(bcnt, dfref, by.x = f.tname, by.y = "f1", all.x = T)
    df2 <- merge(df2, itis.ttable.loc, by.x = "f2", by.y = "TAXON", 
        all.x = T)
    varlist <- c(tlevs, "SPECIES")
    for (i in 1:length(varlist)) {
        incvec <- df2[, varlist[i]] == ""
        incvec[is.na(incvec)] <- FALSE
        df2[incvec, varlist[i]] <- NA
    }
    return(df2[, c(names(bcnt), tlevs, "SPECIES")])
  }
}
\keyword{regression}
