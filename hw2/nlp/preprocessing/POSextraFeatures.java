package nlp.preprocessing;

import java.io.*;
import java.util.*;

/** 
 *
 * @author Ray Mooney
 * Methods for processing Linguistic Data Consortium (LDC,www.ldc.upenn.edu) 
 * data files that are tagged for Part Of Speech (POS). Converts tagged files
 * into simple untagged Lists of sentences which are Lists of String tokens.
 */

/*
 * ing         gerund
 * caps
 * hyphen
 * s           plural
 */

public class POSextraFeatures {

  /** The name of the LDC POS file */
  //public File file = null;
  /** The I/O reader for accessing the file */
  //protected BufferedReader reader = null;

  protected static HashSet<String> featuremap = new HashSet<String>();

  /** Create an object for a given LDC POS tagged file */
  public POSextraFeatures(File file) {
	//this.file = file;
	/*
	try {
	  this.reader = new BufferedReader(new FileReader(file));
	}
	catch (IOException e) {
	  System.out.println("\nCould not open POSTaggedFile: " + file);
	  System.exit(1);
	}
	*/
  }

  /** Segment a token/POS string and return just the token */
  protected static String segmentTokenSpace (String tokenPos) {
	// POS tag follows the last slash
	int slash = tokenPos.lastIndexOf(" ");
	if (slash < 0)	    
	  return tokenPos;
	else
	  return tokenPos.substring(0,slash);
	//return tokenPos.replace('/', ' ');
  }

  public static boolean isNoun(String word) {
	List<String> noun_suffix_list = Arrays.asList("acy", "al", "ance", "ence", "dom", "er", "or", "ism", "ist", "ity", "ty", "ment", "ness", "ship", "sion", "tion");
	for ( String noun_suffix : noun_suffix_list ) {
	  if (word.endsWith(noun_suffix)) return true;
	}
	return false;
  }


  public static boolean isVerb(String word) {
	List<String> verb_suffix_list = Arrays.asList("ate", "en", "ify", "fy", "ize", "ise");
	for ( String verb_suffix : verb_suffix_list ) {
	  if (word.endsWith(verb_suffix)) return true;
	}
	return false;
  }


  public static boolean isAdj(String word) {
	List<String> adj_suffix_list = Arrays.asList("able", "ible", "al", "esque", "ful", "ic", "ical", "ious", "ous", "ish", "ive", "less", "y");
	for ( String adj_suffix : adj_suffix_list ) {
	  if (word.endsWith(adj_suffix)) return true;
	}
	return false;
  }

  public static void writeExtraFeatures(File ifile, File ofile) {
	try {
	  BufferedReader reader = new BufferedReader(new FileReader(ifile));
	  BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));

	  String line;
	  while ((line = reader.readLine()) != null) {
		if (line.equals("")) {
		  writer.write("\n");
		} else {

		  String word = segmentTokenSpace(line);

		  String feature = "";
		  if (featuremap.contains("gerund") && word.endsWith("ing")) {
			feature = feature + " ing";
		  }
		  if (featuremap.contains("plural") && word.endsWith("s")) {
			feature = feature + " s";
		  }
		  if (featuremap.contains("hyphen") && word.contains("-")) {
			feature = feature + " hyphen";
		  }
		  if (featuremap.contains("caps") && Character.isUpperCase(word.charAt(0))) {
			feature = feature + " caps";
		  }
		  if (featuremap.contains("noun") && (isNoun(word))) {
			feature = feature + " noun";
		  }

		  if (featuremap.contains("verb") && (isVerb(word))) {
			feature = feature + " verb";
		  }
		  if (featuremap.contains("adj") && (isAdj(word))) {
			feature = feature + " adj";
		  }

		  String new_line;
		  int slash = line.lastIndexOf(" ");
		  if (slash < 0)
			new_line = word + feature;
		  else {
			new_line = word + feature + " " + line.substring(slash+1, line.length());
		  }
		  writer.write(new_line + "\n");
		}
	  }
	  reader.close();
	  writer.close();
	}catch (IOException e) {
	  e.printStackTrace();
	}
  }

  /** Convert LDC POS tagged files to just lists of tokens for each setences 
   *  and print them out. */
  public static void main(String[] args) throws IOException {
	String ifilename = args[0];
	File ifile = new File(args[0]);

	for (int i = 1; i < args.length; i++) {
	  featuremap.add(args[i]);
	}

	String ofilename;
	if (ifilename.contains(".")) {
	  String[] parts = ifilename.split("\\.");
	  ofilename = parts[0];
	} else {
	  ofilename = ifilename;
	}
	for (int i = 1; i < args.length; i++) {
	  ofilename = ofilename + "_" + args[i];
	}
	ofilename = ofilename + ".pos";
	File ofile = new File(ofilename);
	writeExtraFeatures(ifile, ofile);

  }

}
