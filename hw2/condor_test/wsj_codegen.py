#!/usr/bin/python

import os.path

#ofilename="condor_wsj_hmm.sub"
#dirname="wsj_hmm"
#dataset="../../wsj/wsj00.pos ../../wsj/wsj01.pos"
#percentage=1.0
#handler="HMMSimpleTagger"

#ofilename="condor_wsj_crf.sub"
#dirname="wsj_crf"
#dataset="../../wsj/wsj00.pos ../../wsj/wsj01.pos"
#percentage=1.0
#handler="SimpleTagger"

ofilename="condor_wsj_crf_extra.sub"
dirname="wsj_crf_extra"
dataset="../../wsj/wsj00_all.pos ../../wsj/wsj01_all.pos"
percentage=1.0
handler="SimpleTagger"




if (os.path.isfile(ofilename)):
    os.remove(ofilename)
ofile = open(ofilename, "a")
for i in range(0, 1):
    ofile.write(  \
"""universe = vanilla
environment = CLASSPATH=../../mallet-2.0.7/class:../../mallet-2.0.7/lib/mallet-deps.jar 

Initialdir = {1}
Executable = /usr/bin/java

+Group   = "GRAD"
+Project = "INSTRUCTIONAL"
+ProjectDescription = "CS388 Homework 2"

Log = exp{0}.log

Notification = complete
Notify_user = jianyu@cs.utexas.edu

Arguments = cc.mallet.fst.{4} \
       --train true --model-file model_file \
       --training-proportion {3} \
       --test lab {2}

Output = exp{0}.out
Error  = exp{0}.err
Queue 1


""".format(i, dirname, dataset, percentage, handler)
               )
ofile.close()
