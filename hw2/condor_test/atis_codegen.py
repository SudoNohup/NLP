#!/usr/bin/python

import os.path

#ofilename="condor_atis_hmm.sub"
#dirname="atis_hmm"
#dataset="../../atis/atis.pos"
#percentage=0.8
#handler="HMMSimpleTagger"

#ofilename="condor_atis_crf.sub"
#dirname="atis_crf"
#dataset="../../atis/atis.pos"
#percentage=0.8
#handler="SimpleTagger"

ofilename="condor_atis_crf_extra.sub"
dirname="atis_crf_extra"
dataset="../../atis/atis_all.pos"
percentage=0.8
handler="SimpleTagger"


if (os.path.isfile(ofilename)):
    os.remove(ofilename)
ofile = open(ofilename, "a")
for i in range(0, 10):
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

Arguments = cc.mallet.fst.{4} --random-seed {0} \
       --train true --model-file model_file \
       --training-proportion {3} \
       --test lab {2}

Output = exp{0}.out
Error  = exp{0}.err
Queue 1


""".format(i, dirname, dataset, percentage, handler)
)
ofile.close()
