package CEC.learner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.*;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class NBClassifier {

	public static void main(String[] args) {
		try {
			SMO classifier = new SMO(); // new instance of SMO
			classifier.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"")); // set classifier

			Instances data = new Instances(new BufferedReader(new FileReader("./train.arff")));
			data.setClassIndex(2);
			

			String[] opt = new String[2];
			opt[0] = "-R"; // "range"
			opt[1] = "2"; // first attribute
			Remove remove = new Remove(); // new instance of filter
			remove.setOptions(opt); // set options
			remove.setInputFormat(data); // inform filter about dataset
			
			// **AFTER** setting options
			Instances newData = Filter.useFilter(data, remove); // apply filter

			Evaluation eval = new Evaluation(newData);
			eval.crossValidateModel(classifier, newData, 10, new Random(1));

			System.out.println(eval.toSummaryString("\nResults\n======\n",
					false));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}