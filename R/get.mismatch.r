"get.mismatch" <-
function(bcnt, itis.ttable, exlocal = character(0),outputFile=NULL ) {

  globenv <- globalenv()

  f.tname <- (names(bcnt))[2]

  tlevs <- names(itis.ttable)
  imatch <- match("TAXON", tlevs)
  tlevs <- tlevs[-imatch]


  if (is.factor(bcnt[,2])) {
    f1 <- sort(unique(levels(bcnt[,2])[bcnt[,2]]))
  }
  else {
    if (is.character(bcnt[,2])) {
      f1 <- sort(unique(bcnt[,2]))
    }
    else {
    JGRMessageBox(w.title="Error", msg="2nd field is neither factor nor character")
    }
  }

  f2 <- toupper(f1)
  dfref <- data.frame(I(f1), I(f2))

  itis.taxa <- itis.ttable$TAXON

  substr <- as.list(rep(NA, times = 1))
  i <- 1
  tmiss0 <- dfref$f2

  # get rid of anything inside parentheses
  w1 <- regexpr("\\(", tmiss0)               # find index value of "("
  w2 <- regexpr("\\)", tmiss0)               # find index value of ")"
  incvec <- (w1 != -1) & (w2 != -1)          # incvec select ones with match ()
  tmiss0[incvec] <- paste(substring(tmiss0[incvec], 1, w1[incvec]-1),
                          substring(tmiss0[incvec], w2[incvec]+1,
                                    nchar(tmiss0[incvec])))      # cut incvec ()

  # parse all continuous character strings into separate storage lists
  repeat {
    w <- regexpr("[A-Z]+", tmiss0)
    if (sum(w != -1) == 0) break
    substr[[i]] <- substring(tmiss0, w, w+attributes(w)$match.length-1)
    w3 <- w + attributes(w)$match.length
    tmiss0 <- substring(tmiss0, w3, nchar(tmiss0))
    if (sum(tmiss0 != "") == 0) break
    i <- i + 1
  }

  # First guess at species names
  exlist <- c("DUPLICATE", "SETAE", "CODE", "GROUP", "TYPE", "GENUS",
              "PANEL", "SAND", "TURRET", "CASE", "LARVAE", "ADULT",
              toupper(exlocal))
  sp.name <- rep("", times = length(substr[[1]]))
  if (length(substr) > 1) {
    for (i in 1:length(substr[[1]])) {
      for (j in 2:length(substr)) {
        if ((nchar(substr[[j]][i]) > 3) & (! (substr[[j]][i] %in% exlist))) {
          if (sp.name[i] == "") {
            sp.name[i] <- substr[[j]][i]
          }
          else{
            sp.name[i] <- paste(sp.name[i], substr[[j]][i], sep = "/")
          }
        }
      }
    }
  }

  dfref$f2 <- substr[[1]]
  dfref$sp.name <- sp.name

  #Check for any compound taxa
  #Only allow compound taxa up to the same family

  if (length(substr) > 1) {
    imatch <- match("SUBCLASS", toupper(tlevs))
    tlevs.loc <- rev(tlevs[length(tlevs):imatch])
    for (i in 1:nrow(dfref)) {
      if (nchar(substr[[2]][i]) > 3) {
        imatch1 <- match(substr[[1]][i], itis.taxa)
        imatch2 <- match(substr[[2]][i], itis.taxa)
        if (! is.na(imatch1) & ! is.na(imatch2)) {
          comp1 <- itis.ttable[imatch1, tlevs.loc]
          comp2 <- itis.ttable[imatch2, tlevs.loc]
        tlev.sav <- ""
          for (j in 1:length(comp1)){
            if (! is.na(comp1[j]) & ! is.na(comp2[j])) {
              if ((comp1[j] == comp2[j]) & (comp1[j] != "")) {
                tlev.sav <- tlevs.loc[j]
              }
            }
          }
          if (tlev.sav != "") {
            dfref$f2[i] <- comp1[,tlev.sav]
          dfref$sp.name[i] <- ""
          }
        # default here if both strings match to something
        # but a common higher level is not found is genus.species
        }
        else {
          if (!is.na(imatch1)) {
          # check whether first string is a genus
            if (is.na(itis.ttable[imatch1, "GENUS"])) {
              dfref$sp.name[i] <- ""
            }
            else {
              if (itis.ttable[imatch1, "GENUS"] == "") {
                dfref$sp.name[i] <- ""
              }
            }
          }
          else {
            if (! is.na(imatch2)) {
              dfref$f2[i] <- substr[[2]][i]
              dfref$sp.name[i] <- ""
            }
          }
        }
      }
    }
  }

  globenv[["dfref"]] <-dfref         # have to be a global variable
  
  # check for mismatched taxa with itis
  tmiss0 <- character(0)
  for (i in 1:length(globenv[["dfref"]]$f2)) {
    if (! (globenv[["dfref"]]$f2[i] %in% itis.taxa)) {
      tmiss0 <- c(tmiss0, globenv[["dfref"]]$f2[i])
    }
  }

  tmiss0 <- sort(unique(tmiss0))

  if (length(tmiss0) > 0) {
      b<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameUnrecog")
      .jcall(b,"Ljavax/swing/JFrame;", "getMyGUI",length(tmiss0),tmiss0)
 #     return(dfref)
    }
  else   {
    get.duplicates( bcnt,tmiss1=character(0), itis.ttable)        
  }
}
