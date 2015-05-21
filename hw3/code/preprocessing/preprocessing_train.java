
import java.io.*;
import java.util.*;

/**
 * @author Jianyu Huang
 *
 */

public class preprocessing_train {

  /*
  static int treebankSize;
  public preprocessing_train() {
	treebankSize = 0;
  }
  */

  /** */
  protected void addTree(File ifile, List<List<String>> treebank) {
	BufferedReader ireader = null;

	int banksize = 100;

	if (treebank.size() >= banksize) return;

	try {
	  if (ifile.isDirectory()) {
		//error
		System.out.println("should not be a directory!");
		return;
	  }

	  List<String> curTree = new ArrayList<String>();

	  ireader = new BufferedReader(new FileReader(ifile));
	  String line = ireader.readLine();
	  while (line.startsWith("*sx*")) {
		line = ireader.readLine();
	  }


	  while ((line = ireader.readLine()) != null) {
		if (line.startsWith("( (")) {
		  if (curTree.size() != 0) {
			treebank.add(curTree);
			//treebankSize ++;
			if (treebank.size() >= banksize) return;
		  }
		  curTree = new ArrayList<String>();
		}
		curTree.add(line);
	  }
	  treebank.add(curTree);
	  //treebankSize ++;
	  if (treebank.size() >= banksize) return;

	  if (ireader != null) ireader.close();
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  public List<List<String>> convertToTreeBank(File[] files) {
	List<List<String>> treebank = new ArrayList<List<String>>();

	for (int i = 0; i < files.length; i++) {
	  File input = files[i];
	  if (!input.isDirectory()) {
		addTree(input, treebank);
	  } else {
		File[] ifileList = input.listFiles();
		for (File ifile : ifileList) {
		  addTree(ifile, treebank);
		}
	  }

	}
	return treebank;
  }

  public void writeOutput(File[] ifiles, File ofile) {
	List<List<String>> treebank = convertToTreeBank(ifiles);


	System.out.println("sentence No = " + treebank.size());

	try {
	  BufferedWriter owriter = new BufferedWriter(new FileWriter(ofile));
	  for (List<String> tree : treebank) {
		for (String line : tree) {
		  owriter.write(line);
		  owriter.newLine();
		}
	  }
	  owriter.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

  public static void main(String[] args) throws IOException {
	preprocessing_train handler = new preprocessing_train();

	if (args.length <= 2 ) {
	  //error
	  System.out.println("at least 3 arguments!");
	}

	File[] files = new File[args.length - 2];
	for (int i = 0; i < files. length; i++) {
	  files[i] = new File(args[i]);
	}
	File ofile = new File(args[args.length - 2]);

	int sentenceNum = Integer.parseInt(args[args.length - 1]);


	handler.writeOutput(files, ofile);

  }

}
