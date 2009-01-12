get.mergedfile<- function(bcnt, itis.ttable, selstr=character(0),
         outputFile = "sum.tax.table.txt") {
  globenv <- globalenv()
  reps <- unique(dftemp2$TAXON[duplicated(dftemp2$TAXON)])        
  if (length(reps) > 0) {
    isel <- match(selstr, globenv[["sumstr"]])
    isav <- isav[-isel]
    dftemp2 <- dftemp2[-isav,]
  }
  itis.ttable.loc <- dftemp2[, names(itis.ttable)]
  f.tname <- (names(bcnt))[2]
  tlevs <- names(itis.ttable)
  imatch <- match("TAXON", tlevs)
  tlevs <- tlevs[-imatch]
  
  # Eliminate fields with no entries
  iomit <- numeric(0)
  for (i in 1:length(tlevs)) {
    if (sum(itis.ttable.loc[, tlevs[i]] != "", na.rm = TRUE) == 0) {
      iomit <- c(iomit,i)
    }
  }
  if (length(iomit) > 0) {
    tlevs <- tlevs[-iomit]
  }

  # Only allow species names for valid genera
  df1 <- merge(dfref, itis.ttable.loc, by.x = "f2", by.y = "TAXON",
               all.x = TRUE)
  df1$SPECIES <- paste(df1$GENUS, df1$sp.name, sep = ".")
  incvec <- (nchar(df1$sp.name) > 0) & (df1$GENUS != "")
  incvec[is.na(incvec)] <- FALSE
  df1$SPECIES[! incvec] <- ""
  dfref <- df1[, c("f1", "f2", "SPECIES")]

  # delete site id and count fields to generate summary taxa table
  df1 <- df1[, c(tlevs, "SPECIES", "f1")]
  df1 <- df1[do.call(order, df1),]
  names(df1) <- c(tlevs, "SPECIES", f.tname)
  
  if (is.character(outputFile)) {
    write.table(df1, sep = "\t", file = outputFile, row.names = FALSE)
    JGRMessageBox(msg = paste("Check final taxa name assignments in", outputFile), w.title="BiologicalInferences: Info")
  }

  df2 <- merge(bcnt, dfref, by.x = f.tname, by.y = "f1", all.x = TRUE)
  
  df2 <- merge(df2, itis.ttable.loc, by.x = "f2", by.y = "TAXON", all.x = TRUE)
  
  varlist <- c(tlevs, "SPECIES")
  
  for (i in 1:length(varlist)) {
    incvec <- df2[, varlist[i]] == ""
    incvec[is.na(incvec)] <- FALSE
    df2[incvec, varlist[i]] <- NA
  }

#  Final.Matrix<<-df2[, c(names(bcnt), tlevs, "SPECIES")]
  return(df2[, c(names(bcnt), tlevs, "SPECIES")])

}
