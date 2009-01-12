\name{get.otu.jgr}
\alias{get.otu.jgr}
\title{Dialog box for predicting env. conditions from biological observations}
\description{
  This function constructs the operational taxonomic units using the results/output 
  from get.mergedfile }
\usage{
get.otu.jgr(bcnt, optlist = NULL, ndc = TRUE, outputFile = "sum.otu.txt")
}
\arguments{
  \item{bcnt}{ a taxonomic matrix that have merged taxonomy of the benthic count names
    and the itis.ttable data, it can be output from get.mergedfile function or user input }
  \item{optlist}{ ~~Describe \code{optlist} here~~ }
  \item{ndc}{a flag }
  \item{outputFile}{ a tab-delimited text file for output of the get.otu
     function that constructs the operational taxonomic units }
}
\seealso{ 
	\code{\link{bioinfer1.JGR}},
	\code{\link{bioinfer2.JGR}},
	\code{\link{bioinfer3.JGR}},
	\code{\link{JGRMessageBox}},
	\code{\link{get.mergedfile}},
	\code{\link{get.mismatch}},
	\code{\link{dup.sel}},
	\code{\link{get.duplicates}}
}
\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (bcnt, optlist = NULL, ndc = TRUE, outputFile = "sum.otu.txt") 
{
    names0 <- names(bcnt)
    siteid <- names0[1]
    nameid <- names0[2]
    abnid <- names0[3]
    if (is.list(optlist)) {
        optlist <- optlist$tnames
    }
    w <- regexpr("\\.", optlist)
    optlist.spec <- optlist[w != -1]
    JGRMessageBox(msg = "<HTML> Please wait while R compute inferences! <br/> It may take several minutes! </HTML>", 
        w.title = "BiologicalInferences: Info")
    if (!is.null(optlist)) {
        if (length(optlist.spec) > 0) {
            spec <- sort(unique(bcnt$SPECIES))
            name.orig <- character(0)
            name.change <- character(0)
            for (i in 1:length(spec)) {
                if (is.na(match(spec[i], optlist.spec))) {
                  w <- regexpr("\\.", spec[i])
                  gen <- substring(spec[i], 1, w - 1)
                  spec.half <- substring(spec[i], w + 1, nchar(spec[i]))
                  opt.sel <- character(0)
                  speclist <- as.list(rep(NA, times = 1))
                  k <- 1
                  repeat {
                    w2 <- regexpr("[A-Z]+", spec.half)
                    if (w2 == -1) 
                      break
                    speclist[[k]] <- substring(spec.half, w2, 
                      w2 + attributes(w2)$match.length - 1)
                    spec.half <- substring(spec.half, w2 + attributes(w2)$match.length, 
                      nchar(spec.half))
                    k <- k + 1
                  }
                  ind1 <- grep(gen, optlist.spec)
                  if (length(ind1) > 0) {
                    ind.sel <- ind1
                    for (k in 1:length(speclist)) {
                      ind2 <- grep(speclist[[k]], optlist.spec)
                      ind.all <- c(ind.sel, ind2)
                      ind.sel <- ind.all[duplicated(ind.all)]
                    }
                    opt.sel <- c(opt.sel, optlist.spec[ind.sel])
                  }
                  if (length(opt.sel) > 0) {
                    if (length(opt.sel) > 1) {
                      specnew <- select.list(c(opt.sel, "NONE"), 
                        preselect = "NONE", title = paste(spec[i]))
                    }
                    else {
                      specnew <- opt.sel
                    }
                    if ((specnew != "") & (specnew != "NONE")) {
                      spec[i] <- gsub("\\(", ".", spec[i])
                      incvec <- regexpr(spec[i], bcnt$SPECIES) != 
                        -1
                      incvec[is.na(incvec)] <- FALSE
                      name.orig <- c(name.orig, spec[i])
                      name.change <- c(name.change, specnew)
                      bcnt$SPECIES[incvec] <- toupper(specnew)
                    }
                  }
                }
            }
            if (length(name.orig) > 0) {
                cat("Review the changes in species names: \n")
                dftemp <- data.frame(name.orig, name.change)
                names(dftemp) <- c("Original name", "Revised name")
                print(dftemp)
                cat("\n")
            }
        }
    }
    tlev <- names0[4:length(names0)]
    tname <- rep(NA, times = nrow(bcnt))
    for (i in length(tlev):1) {
        incvec <- is.na(tname)
        tname[incvec] <- bcnt[incvec, tlev[i]]
    }
    lookup <- unique.data.frame(data.frame(tname, bcnt[, nameid]))
    names(lookup) <- c("TNAME", "TAXANAME")
    getocc <- function(x) length(unique(x))
    numocc <- tapply(bcnt[, siteid], tname, getocc)
    df1 <- data.frame(names(numocc), numocc)
    names(df1) <- c("TNAME", "NUMOCC")
    df2 <- unique.data.frame(data.frame(bcnt[, tlev], tname))
    names(df2) <- c(tlev, "TNAME")
    df2 <- merge(df2, df1, by = "TNAME")
    if (!is.null(optlist)) {
        otufin <- rep(NA, times = nrow(df2))
        tlevel <- rep(NA, times = nrow(df2))
        for (i in 1:nrow(df2)) {
            j <- length(tlev)
            while (is.na(df2[i, tlev[j]])) j <- j - 1
            while (is.na(match(df2[i, tlev[j]], optlist)) & (j > 
                1)) j <- j - 1
            if (!is.na(match(df2[i, tlev[j]], optlist))) {
                otufin[i] <- df2[i, tlev[j]]
                tlevel[i] <- j
            }
        }
        otufin1 <- otufin
    }
    else {
        otufin <- levels(df2$TNAME)[df2$TNAME]
        otufin1 <- otufin
    }
    in.all <- rep(TRUE, times = nrow(df2))
    otufin2 <- rep(NA, times = nrow(df2))
    for (i in 1:(length(tlev) - 1)) {
        taxa.all <- df2[, tlev[i]]
        taxa.red <- taxa.all[in.all]
        taxa.u <- sort(unique(taxa.red))
        in.all.n <- in.all
        for (j in 1:length(taxa.u)) {
            incvec <- taxa.all == taxa.u[j]
            incvec[is.na(incvec)] <- FALSE
            numocc.loc <- df2$NUMOCC[incvec]
            otufin.loc <- otufin1[incvec]
            v <- otufin.loc == taxa.u[j]
            v[is.na(v)] <- FALSE
            if (sum(v) > 0) {
                a <- sum(numocc.loc[v])
                b <- sum(numocc.loc[!v])
                c <- sum(v)
                d <- sum(!v)
                if ((a >= b) | ((c == 1) & (d == 1))) {
                  otufin2[incvec] <- taxa.u[j]
                  in.all.n[incvec] <- FALSE
                }
                else {
                  otufin2[incvec] <- otufin.loc
                  otufin2[otufin1 == taxa.u[j]] <- NA
                  in.all.n[otufin1 == taxa.u[j]] <- FALSE
                  otufin1[otufin1 == taxa.u[j]] <- NA
                }
                in.all <- in.all.n
            }
        }
    }
    incvec <- (!is.na(otufin1)) & in.all
    otufin2[incvec] <- otufin1[incvec]
    df2 <- data.frame(df2, otufin, otufin2)
    df2 <- df2[do.call(order, df2[, tlev]), ]
    if (is.character(outputFile)) {
        write.table(df2, file = outputFile, sep = "\t", row.names = FALSE)
        JGRMessageBox(msg = paste("Check OTU assignments in", 
            outputFile), w.title = "BiologicalInferences: info")
        cat("Check OTU assignments in", outputFile, "\n")
    }
    if (ndc) {
        df3 <- df2[, c("TNAME", "otufin2")]
    }
    else {
        df3 <- df2[, c("TNAME", "otufin")]
    }
    names(df3) <- c("TNAME", "OTU")
    bcnt <- data.frame(bcnt, tname)
    bcnt <- merge(df3, bcnt, by.x = "TNAME", by.y = "tname")
    bcnt.otu <- bcnt[, c(siteid, nameid, abnid, "TNAME", "OTU")]
    return(bcnt.otu)
  }
}
\keyword{regression}

