# This function follows get.mismatch function after user corrects mismatched taxa in the java
# window and click ok; it will send the corrected taxa names back to tmiss1

"get.duplicates"<-function(bcnt,tmiss1=character(0),itis.ttable) {

  globenv <- globalenv()
  if (length(tmiss1)>0) {
    tmiss0 <- character(0)
    for (i in 1:length(globenv[["dfref"]]$f2)) {
      if (! (globenv[["dfref"]]$f2[i] %in% itis.ttable$TAXON)) {
        tmiss0 <- c(tmiss0, globenv[["dfref"]]$f2[i])
      }    
    }

    tmiss0 <- sort(unique(tmiss0))
    dfcorrect <- data.frame(I(tmiss0), I(tmiss1))
    names(dfcorrect) <- c("TAXANAME", "CORRECTION")
    dfcorrect$CORRECTION <- toupper(dfcorrect$CORRECTION)

# the for loop put unmatched taxa into dfref
    for (i in 1:nrow(dfcorrect)) {
      incvec <- globenv[["dfref"]]$f2 == dfcorrect$TAXANAME[i]      
      if (! is.na(dfcorrect$CORRECTION[i])) {
        imatch <- match(toupper(dfcorrect$CORRECTION[i]), itis.ttable$TAXON)

        if (is.na(imatch)) {                            # Not matched taxa
          if (dfcorrect$CORRECTION[i] != "") {
            cat(dfcorrect$CORRECTION[i], " is not found in ITIS.\n")
          }
        }
        else {                                    # matched taxa into dfref
          globenv[["dfref"]]$f2[incvec] <- dfcorrect$CORRECTION[i]
          specpres <- globenv[["dfref"]]$sp.name != ""
          if (sum(specpres & incvec) > 0) {
            isel <- specpres & incvec
            if (itis.ttable$GENUS[imatch] == "") {
              globenv[["dfref"]]$sp.name[isel] <- ""
            }
          }
        }
      }
    }
# End of for loop
  }
#####  Compute number of occurence for mismatched taxa
  f.tname<-(names(bcnt))[2]
  tlevs <- names(itis.ttable)
  imatch <- match("TAXON", tlevs)
  tlevs <- tlevs[-imatch]
  df1 <- merge(globenv[["dfref"]], itis.ttable, by.x = "f2", by.y = "TAXON", all.x = TRUE)
  nomatch <- is.na(df1[, tlevs[1]])

  df2 <- merge(bcnt, globenv[["dfref"]], by.x = f.tname, by.y = "f1", all.x = TRUE)        # merge with bcnt
  df2 <- merge(df2, itis.ttable, by.x = "f2", by.y = "TAXON", all.x = TRUE)   # merge with itis

  incvec <- is.na(df2[, tlevs[1]])
  if (sum(incvec) > 0) {
    tmiss <- sort(unique(df2[incvec, "f2"]))
    sumnumocc <- rep(NA, times = length(tmiss))
    tlen<-length(tmiss)
    for (i in 1:length(tmiss)) {
      sumnumocc[i] <- sum(df2[, "f2"] == tmiss[i])
    }
    dfsumnumocc <- data.frame(I(tmiss), sumnumocc)
    names(dfsumnumocc) <- c("TAXANAME", "NUMBER OF OCCURRENCES")
   #  Message box for number of occurence, disabled due to inefficiency in java 
#          okPushed="false"
#              w<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameMoreInfor")
#              .jcall(w,"Ljavax/swing/JFrame;", "getMyGUI",length(tmiss), tmiss, sumnumocc)
#             repeat { 
#                Sys.sleep(0.5)  
#                if (okPushed=="true") break
#                }
#            }
# 
    cat("Summary of taxa without matches: \n")
    format(dfsumnumocc, justify = "centre")
    print(dfsumnumocc)
  }
  flush.console()

    # Final check for duplications in itis table, if there are duplicates in itis
    # invoke a java window for users to select a single taxon; if no duplicates, go to get.mergedfile
    
  dftemp <- data.frame(I(unique(globenv[["dfref"]]$f2)))
  names(dftemp) <- c("f2")
  globenv[["dftemp2"]] <- merge(itis.ttable, dftemp, by.x = "TAXON", by.y = "f2",
                    all.y = TRUE)                                       # a global object dftemp2
  reps <- unique(globenv[["dftemp2"]]$TAXON[duplicated(globenv[["dftemp2"]]$TAXON)])        # Taxa with replicates, e.g. MENETUS-MOLLSCA----, MENETUS-ARTHROPODA---
  if (length(reps) > 0) {
    replist<-globenv[["dftemp2"]]$TAXON[reps == globenv[["dftemp2"]]$TAXON]                # TAXA LIST COULD BE SAME TAXON E.G. MENETUS AND MENETUS
    sumstr <- rep("", times = length(replist))
    globenv[["isav"]] <- (1:nrow(globenv[["dftemp2"]]))[reps == globenv[["dftemp2"]]$TAXON]            # find out row index
    print(globenv[["dftemp2"]][globenv[["isav"]], c("TAXON", "PHYLUM", "CLASS", "ORDER", "FAMILY")])
    for (i in 1:length(replist)) {
      sumstr[i] <- paste(globenv[["dftemp2"]][globenv[["isav"]][i], c("TAXON","PHYLUM", "CLASS", "ORDER", "FAMILY")],
                         collapse = "-")
    }                                           
    globenv[["sumstr"]]<-sumstr
    d<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameDupITIS")
    .jcall(d,"Ljavax/swing/JFrame;", "getMyGUI",length(globenv[["sumstr"]]),
           globenv[["sumstr"]],0)
  }
  else {
    get.mergedfile(bcnt, itis.ttable, selstr=globenv[["tempstr"]])        
  }
}

       
