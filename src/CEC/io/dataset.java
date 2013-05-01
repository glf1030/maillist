package CEC.io;

import CEC.utility.CTokenizer;
import CEC.utility.Dictionary;
import CEC.utility.Document;
import CEC.utility.Stopwords;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;

public class dataset {

	// ---------------------------------------------------------------
	// Instance Variables
	// ---------------------------------------------------------------

	public Dictionary dict; // local dictionary
	public Document[] docs; // a list of documents
	public Stopwords stop; // a set of stopwords
	public int M; // number of documents
	public int V; // number of words
	public CTokenizer CT; // tokenizer

	// --------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------

	public dataset() {
		dict = new Dictionary();
		CT = new CTokenizer();
		stop = new Stopwords();
		M = 0;
		V = 0;
		docs = null;

	}

	public dataset(int M) {
		dict = new Dictionary();
		CT = new CTokenizer();
		stop = new Stopwords();
		this.M = M;
		this.V = 0;
		docs = new Document[M];

	}

	// ---------------------------------------------------------------
	// I/O methods
	// ---------------------------------------------------------------

	/**
	 * read a dataset from a stream, create new dictionary
	 * 
	 * @return dataset if success and null otherwise
	 */
	public static dataset readDataSet(String filename) {
		try {

			preprocess(filename);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("./document.txt"), "GBK"));

			dataset data = readDataSet(reader);

			return data;

		} catch (Exception e) {
			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * read a dataset from a stream, create new dictionary
	 * 
	 * @return dataset if success and null otherwise
	 */
	public static dataset readDataSet(BufferedReader reader) {
		try {

			// read number of document
			String line;
			String label;
			line = reader.readLine();
			int M = Integer.parseInt(line);

			dataset data = new dataset(M);

			for (int i = 0; i < M; ++i) {

				label = reader.readLine();
				line = reader.readLine();
				
				data.setDoc(line, i, label);
			}

			reader.close();

			return data;

		} catch (Exception e) {

			System.out.println("Read Dataset Error: " + e.getMessage());
			e.printStackTrace();
			return null;

		}
	}

	private static void preprocess(String filename) throws Exception{
		// TODO Auto-generated method stub
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "GBK"));
		
		int count = 0;
		
		String tmp = "";
		
		while((tmp = reader.readLine())!=null){
			count++;
		}
		count = count/2;
		
		reader.close();
		
		reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), "GBK"));
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("./document.txt"), "GBK"));
		
		writer.append(count+"\n");
		
		while((tmp = reader.readLine())!=null){
			writer.append(tmp+"\n");
		}
		
		writer.flush();
		writer.close();
	}

	// ---------------------------------------------------------------
	// setDoc method
	// ---------------------------------------------------------------
	public void setDoc(String str, int idx, String label) {
		if (0 <= idx && idx < M) {
			// tokenize
			ArrayList<String> words = CT.returnTokenList(str);

			Vector<Integer> ids = new Vector<Integer>();

			for (String word : words) {

				// filter out stopwords
				if (stop.contains(word))
					continue;

				int _id = dict.word2id.size();

				if (dict.contains(word))
					_id = dict.getID(word);

				dict.addWord(word);
				ids.add(_id);

			}

			Document doc = new Document(ids);

			doc.setLabel(label);
			doc.setRawStr(str);

			docs[idx] = doc;

			V = dict.word2id.size();
		}
	}

	// test
	public static void main(String[] args) {

		try {
			
			Consolidator cons = new Consolidator("./emails");
			cons.returnConsolidatedFile();
			
			// read data from a given file
			dataset data = readDataSet("./inputFiles.txt");

			FastVector atts;
			FastVector attVals;
			Instances instances;
			double[] vals;

			atts = new FastVector();

			// numeric
			atts.addElement(new Attribute("ID"));

			// raw content
			atts.addElement(new Attribute("rawString", (FastVector) null));

			// raw content
			atts.addElement(new Attribute("wordIDs", (FastVector) null));

			// label attribute
			attVals = new FastVector();
			attVals.addElement("买卖");
			attVals.addElement("就业");
			attVals.addElement("旅游拼车");
			attVals.addElement("求助");
			attVals.addElement("活动");
			attVals.addElement("租房");
			atts.addElement(new Attribute("label", attVals));

			instances = new Instances("Relation", atts, 0);

			
			// populate instances
			for (int idx = 0; idx < data.docs.length; idx++) {

				// populate the value for an instance
				vals = new double[instances.numAttributes()];
				vals[0] = idx;// id
				vals[1] = instances.attribute(1).addStringValue(
						data.docs[idx].getRawStr());// raw content

				int[] d_ids = data.docs[idx].words;
				String idStr = "";
				for (int i = 0; i < d_ids.length; i++) {
					idStr = idStr + d_ids[i] + " ";
				}
				idStr = idStr.trim();

				vals[2] = instances.attribute(2).addStringValue(idStr);// word
																		// id
																		// list
																		// string
				vals[3] = attVals.indexOf(data.docs[idx].getLabel());// label
				instances.add(new DenseInstance(1.0, vals));
			}

			StringToWordVector myfilter = new StringToWordVector();
	    	myfilter.setOutputWordCounts(true);
	    	myfilter.setIDFTransform(true);
	    	myfilter.setInputFormat(instances);
	    	myfilter.setAttributeIndices("3");

	    	instances = Filter.useFilter(instances, myfilter);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("./train.ARFF"), "GBK"));
			writer.append(instances.toString());
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}