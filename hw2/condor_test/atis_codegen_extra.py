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


features=["gerund", "caps", "plural", "hyphen", "noun", "verb", "adj"]

ofilename="condor_atis_crf_extra_cmp.sub"
dirname="atis_crf_extra_cmp"
percentage=0.8
handler="SimpleTagger"

if (os.path.isfile(ofilename)):
    os.remove(ofilename)

ofile = open(ofilename, "a")


counter=0
for feature in features: 
    dataset="../../atis/atis_{0}.pos".format(feature)
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


""".format(counter, dirname, dataset, percentage, handler)
               )
    counter = counter + 1
ofile.close()
