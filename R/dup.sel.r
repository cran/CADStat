# This function is used to check duplicate records after user select records from 
# a JGR message box 

"dup.sel"<-function (bcnt, itis.ttable, tempstr) {
  globenv <- globalenv()
  reps <- unique(globenv[["dftemp2"]]$TAXON[duplicated(globenv[["dftemp2"]]$TAXON)])        
  taxalist<-(substr(tempstr,1,regexpr("-",tempstr)-1))   # find taxon name before "-"
  unmatch<-length(reps[!(reps %in% unique(taxalist))])
  if (unmatch>0|length(reps)!=length(taxalist))  {
    d<-.jnew("org.neptuneinc.cadstat.plots.BiologicalInferencesTaxaNameDupITIS")
    .jcall(d,"Ljavax/swing/JFrame;", "getMyGUI",length(globenv[["sumstr"]]),globenv[["sumstr"]],1)
  }
  else {
    get.mergedfile(bcnt, itis.ttable, selstr=tempstr)
  }
}
