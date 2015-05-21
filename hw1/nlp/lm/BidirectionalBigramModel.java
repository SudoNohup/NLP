package nlp.lm;

import java.io.*;
import java.util.*;

/** 
 * @author Ray Mooney
 * A simple bigram language model that uses simple fixed-weight interpolation
 * with a unigram model for smoothing.
 */

public class BidirectionalBigramModel {

  public BigramModel forward_model = null;
  public BackwardBigramModel backward_model = null;

  /** Interpolation weight for unigram model */
  public double lambda1 = 0.5;

  /** Interpolation weight for bigram model */
  public double lambda2 = 0.5;

  /** Initialize model with empty hashmaps with initial
   *  unigram entries for setence start (<S>), sentence end (</S>)
   *  and unknown tokens */
  public BidirectionalBigramModel() {
	forward_model = new BigramModel();
	backward_model = new BackwardBigramModel();
  }

  /** Train the model on a List of sentences represented as
   *  Lists of String tokens */
  public void train (List<List<String>> sentences) {
	forward_model.train(sentences);
	backward_model.train(sentences);
  }
  
  /** Return bigram string as two tokens separated by a newline */
  public String bigram (String prevToken, String token) {
	return prevToken + "\n" + token;
  }

  /** Return fist token of bigram (substring before newline) */
  public String bigramToken1 (String bigram) {
	int newlinePos = bigram.indexOf("\n");
	return bigram.substring(0,newlinePos);
  } /** Return second token of bigram (substring after newline) */ public String bigramToken2 (String bigram) {
	int newlinePos = bigram.indexOf("\n");
	return bigram.substring(newlinePos + 1, bigram.length());
  }


  /** Like test1 but excludes predicting end-of-sentence when computing perplexity */
  public void test2 (List<List<String>> sentences) {
	double totalLogProb = 0;
	double totalNumTokens = 0;
	for (List<String> sentence : sentences) {
	  totalNumTokens += sentence.size();
	  double sentenceLogProb = sentenceLogProb2(sentence);
	  //	    System.out.println(sentenceLogProb + " : " + sentence);
	  totalLogProb += sentenceLogProb;
	}
	double perplexity = Math.exp(-totalLogProb / totalNumTokens);
	System.out.println("Word Perplexity = " + perplexity );
  }

  
  /** Like sentenceLogProb but excludes predicting end-of-sentence when computing prob */
  public double sentenceLogProb2 (List<String> sentence) {
	double[] forward_probs = forward_model.sentenceTokenProbs(sentence);
	double[] backward_probs = backward_model.sentenceTokenProbs(sentence);

	DoubleValue forward_tokenprob, backward_tokenprob;
	double sentenceLogProb = 0;

	forward_tokenprob = new DoubleValue();
	backward_tokenprob = new DoubleValue();

	//for (int i = 0; i <= sentence.size()+1; i++) {
	for (int i = 1; i <= sentence.size(); i++) {
	  if (i == 0) {
		//backward_tokenprob.setValue(backward_probs[sentence.size()]);
		double logProb = Math.log(backward_probs[sentence.size()]);
		sentenceLogProb += logProb;
	  } else if (i == sentence.size() + 1) {
		//forward_tokenprob.setValue(forward_probs[sentence.size()]);
		double logProb = Math.log(forward_probs[sentence.size()]);
		sentenceLogProb += logProb;
	  } else {
		forward_tokenprob.setValue(forward_probs[i-1]);
		backward_tokenprob.setValue(backward_probs[sentence.size()-i]);
		double logProb = Math.log(interpolatedProb(forward_tokenprob, backward_tokenprob));
		sentenceLogProb += logProb;
	  }
	}

	return sentenceLogProb;
  }

  /** Interpolate bigram prob using bigram and unigram model predictions */	 
  public double interpolatedProb(DoubleValue unigramVal, DoubleValue bigramVal) {
	double bigramProb = 0;
	// In bigram unknown then its prob is zero
	if (bigramVal != null)
	  bigramProb = bigramVal.getValue();
	// Linearly combine weighted unigram and bigram probs
	return lambda1 * unigramVal.getValue() + lambda2 * bigramProb;
  }

  public static int wordCount (List<List<String>> sentences) {
	int wordCount = 0;
	for (List<String> sentence : sentences) {
	  wordCount += sentence.size();
	}
	return wordCount;
  }

  /** Train and test a bigram model.
   *  Command format: "nlp.lm.BigramModel [DIR]* [TestFrac]" where DIR 
   *  is the name of a file or directory whose LDC POS Tagged files should be 
   *  used for input data; and TestFrac is the fraction of the sentences
   *  in this data that should be used for testing, the rest for training.
   *  0 < TestFrac < 1
   *  Uses the last fraction of the data for testing and the first part
   *  for training.
   */
  public static void main(String[] args) throws IOException {
	// All but last arg is a file/directory of LDC tagged input data
	File[] files = new File[args.length - 1];
	for (int i = 0; i < files.length; i++) 
	  files[i] = new File(args[i]);
	// Last arg is the TestFrac
	double testFraction = Double.valueOf(args[args.length -1]);
	// Get list of sentences from the LDC POS tagged input files
	List<List<String>> sentences = 	POSTaggedFile.convertToTokenLists(files);
	int numSentences = sentences.size();
	// Compute number of test sentences based on TestFrac
	int numTest = (int)Math.round(numSentences * testFraction);
	// Take test sentences from end of data
	List<List<String>> testSentences = sentences.subList(numSentences - numTest, numSentences);
	// Take training sentences from start of data
	List<List<String>> trainSentences = sentences.subList(0, numSentences - numTest);
	System.out.println("# Train Sentences = " + trainSentences.size() + 
		" (# words = " + wordCount(trainSentences) + 
		") \n# Test Sentences = " + testSentences.size() +
		" (# words = " + wordCount(testSentences) + ")");
	// Create a bigram model and train it.
	BidirectionalBigramModel model = new BidirectionalBigramModel();
	System.out.println("Training...");
	model.train(trainSentences);
	// Test on training data using test2
	model.test2(trainSentences);
	System.out.println("Testing...");
	// Test on test data using test2
	model.test2(testSentences);
  }

}
