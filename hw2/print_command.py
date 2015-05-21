command =  "java -cp . nlp.preprocessing.POStoMallet "
for i in range (0, 8):
  command += "/projects/nlp/penn-treebank3/tagged/pos/wsj/0"+str(i)+"/ "
command += "wsj/wsj82.pos"
print command
