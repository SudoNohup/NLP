#javac `pwd`/nlp/lm/*.java
javac nlp/lm/*.java

echo "====Bigram Model(atis)===="
java -cp . nlp.lm.BigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1
echo "====Bigram Model(wsj)===="
java -cp . nlp.lm.BigramModel /projects/nlp/penn-treebank3/tagged/pos/wsj/ 0.1
echo "====Bigram Model(brown)===="
java -cp . nlp.lm.BigramModel /projects/nlp/penn-treebank3/tagged/pos/brown/ 0.1

echo "====Backward Bigram Model(atis)===="
java -cp . nlp.lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1
echo "====Backward Bigram Model(wsj)===="
java -cp . nlp.lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/wsj/ 0.1
echo "====Backward Bigram Model(brown)===="
java -cp . nlp.lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/brown/ 0.1

echo "====Bidirectional Bigram Model(atis)===="
java -cp . nlp.lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1
echo "====Bidirectional Bigram Model(wsj)===="
java -cp . nlp.lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/wsj/ 0.1
echo "====Bidirectional Bigram Model(brown)===="
java -cp . nlp.lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/brown/ 0.1

