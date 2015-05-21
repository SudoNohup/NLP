
import java.io.*;
import java.util.*;

/**
 * @author Jianyu Huang
 *
 */

public class preprocessing {
  
  /** */
  protected void addTree(File ifile, List<List<String>> treebank) {
	BufferedReader ireader = null;
	try {
	  if (ifile.isDirectory()) {
		//error
		System.out.println("should not be a directory!");
		return;
	  }

	  List<String> curTree = new ArrayList<String>();

	  ireader = new BufferedReader(new FileReader(ifile));
	  String line;
	  //String line = ireader.readLine();
//	  while (line.startsWith("*sx*")) {
//		line = ireader.readLine();
//	  }


	  while ((line = ireader.readLine()) != null) {
		if (line.startsWith("( (") || line.startsWith("((") ) {
		  if (curTree.size() != 0) {
			treebank.add(curTree);
		  }
		  curTree = new ArrayList<String>();
		}
		curTree.add(line);
	  }
	  treebank.add(curTree);

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
	preprocessing handler = new preprocessing();
	File[] files = new File[args.length - 1];
	for (int i = 0; i < files. length; i++) {
	  files[i] = new File(args[i]);
	}
	File ofile = new File(args[args.length - 1]);

	handler.writeOutput(files, ofile);

  }

}
