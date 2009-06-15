
"bioinfer.new.JGR" <- function(bencntFile=NULL,coefficientFile=NULL,
                               tname.new = NULL, dupe.sel = NULL)

{

  globenv <- globalenv()
  
  ## Get place to store results
  resultLocation = genResultSpace()

  if (! is.null(bencntFile)) {
  # Load benthic count data
    globenv[["bcnt"]] <-  try(read.table(bencntFile, header=TRUE, sep="\t" ) )
    if(inherits(globenv[["bcnt"]], "try-error" ) ){
      localJGRError(c(errorMessage,
                      paste("Failure reading file: ", bencntFile,".", sep="" ),
                      "Check that file is tab-delimited." ),
                    resultLocation)
    }
  }

  if (! is.null(coefficientFile)) {
  ## Load in coefficients file
    coefname <- try( load( coefficientFile ) )
    if(inherits(coefname, "try-error" ) ){
      localJGRError(c(errorMessage,
                      paste("Error importing coefficients file:",coefficientFile),
                      paste("Check that it is an appropriate .rda coefficient file.") ),
                    resultLocation )
    }
    globenv[["coefs"]] <- get(coefname)
  }

  # modified from bio.infer//get.taxonomic
  if (is.null(dupe.sel)) {
    if (is.null(tname.new)) {
      globenv[["tlevs"]] <- load.itis(globenv)
    
      tname <- get.taxon.names(globenv[["bcnt"]])
      df.parse <- parse.taxon.name(tname)
      
      globenv[["parse.list"]] <- get.valid.names(df.parse, globenv)
    
      if (nrow(globenv[["parse.list"]][[2]]) > 0) {
  
    # resolve records with multiple valid ITIS entries
        globenv[["parse.list"]] <- resolve.mult(globenv[["parse.list"]], globenv)

        if (nrow(globenv[["parse.list"]][[2]]) > 0) {
    # prompt user to correct misspellings
          tmiss <- sort(unique(globenv[["parse.list"]][[2]][,2]))
          b<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameUnrecog")
          .jcall(b,"Ljavax/swing/JFrame;", "getMyGUI",length(tmiss),tmiss)
          
          return()
        }
      }
    }
    else {
      globenv[["parse.list"]] <- incorp.correct(tname.new, globenv[["parse.list"]])
    }
  }

  if (is.null(dupe.sel)) {
    globenv[["fulltab"]] <- make.fulltab1(globenv[["parse.list"]][[1]], globenv)

  # identify and fix duplicate entries
    globenv[["dupe.list"]] <- locate.dupes(globenv[["fulltab"]])
    if (length(globenv[["dupe.list"]]$isav) > 0) {
      d<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameDupITIS")
      .jcall(d,"Ljavax/swing/JFrame;", "getMyGUI",
             length(globenv[["dupe.list"]]$sumstr),
             globenv[["dupe.list"]]$sumstr,0)
      return()
    }
  }
  else {
    globenv[["fulltab"]] <- remove.dupes(globenv[["fulltab"]],
                                         globenv[["dupe.list"]], dupe.sel)
  }

  finaltab <- make.species(globenv[["parse.list"]][[1]], globenv[["fulltab"]])
  output.tax.table(finaltab, globenv[["tlevs"]])
  names0 <- names(globenv[["bcnt"]])

  bcnt.new <- merge(globenv[["bcnt"]], finaltab, by.x = names0[2], by.y = "taxaname.orig")
  bcnt.tax <- bcnt.new[, c(names0,globenv[["tlevs"]], "SPECIES")]

  bcnt.otu <- get.otu(bcnt.tax, globenv[["coefs"]])
  ss <- makess(bcnt.otu)

  globenv[["inf.out"]] <- mlsolve(ss, globenv[["coefs"]])
  cat("Environmental predictions computed and saved in inf.out!\n")

  rm("bcnt", "coefs", "dupe.list", "fulltab", "itis.ttable",
     "parse.list", "tlevs", envir = globenv)
  return()
  

}
