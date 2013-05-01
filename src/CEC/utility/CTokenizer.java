package CEC.utility;

import org.ictclas4j.bean.MidResult;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.segment.SegTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import CEC.utility.Stopwords;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;

/*
 * A tokenizer for word segementation of Chinese sentences/paragraph
 */
public class CTokenizer {

	private String fileContent; // a string to tokenize
	private SegTag segTag; // tokenizer
	private SegResult segResult; // token results
	private int para = 1; // a parameter

	private Properties props;
	private CRFClassifier classifier;

	// test
	public static void main(String[] args) {
		CTokenizer CT = new CTokenizer();
		Stopwords words = new Stopwords();
		/*
		 * String str = "昨天，南京市政府专门召开新闻发布会，市卫生局、疾控中心、工商局、农委和商务局等五部门介绍了该市人"
		 * +"感染H7N9禽流感病例情况。3月31日、4月2日、4月5日，江苏省卫生厅通报了南京市医院收治了5例人感"
		 * +"染H7N9禽流感确诊病例。其中南京籍3例，外地籍2例。5例病例均危重，医院正全力救治。目前，南京地区"
		 * +"5家哨点监测医院，已经将H7N9作为流感病原检测的新增常规项目，同时每家医院增加送检样本数量。";
		 */

		/*
		String str = "圳";
		str = str.replaceAll("http.*?\\s", " ");
		ArrayList<String> ts = CT.returnTokenList(str);
		for (String s : ts) {
			if (words.contains(s))
				continue;

			System.out.println(s);
		}
		*/
	}

	// constructor
	public CTokenizer() {
		fileContent = "";
		segTag = new SegTag(para);
		segResult = null;

		props = new Properties();
		props.setProperty("sighanCorporaDict", "data");
		props.setProperty("serDictionary", "data/dict-chris6.ser.gz");
		props.setProperty("inputEncoding", "UTF-8");
		props.setProperty("sighanPostProcessing", "true");

		classifier = new CRFClassifier(props);
		classifier.loadClassifierNoExceptions("data/ctb.gz", props);
		// flags must be re-set after data is loaded
		classifier.flags.setProperties(props);
	}

	//
	public void setPath(int i) {
		this.para = i;
	}

	public int getPath() {
		return this.para;
	}

	/*
	 * input: str(input string); output: a list of tokens
	 */
	public ArrayList<String> returnTokenList(String str) {

		ArrayList<String> res = new ArrayList<String>();

		// tokenize
		this.fileContent = str.trim().replaceAll("http.*?\\s", " ");
		
		List<String> tmp = classifier.segmentString(this.fileContent);
		
		for(String s: tmp)
		{
			res.add(s);
		}
		
		
		/*
		this.fileContent = this.fileContent.replaceAll("圳", " ");
		if (this.fileContent == null || this.fileContent == "")
			return res;
		System.out.println(this.fileContent);
		segResult = segTag.split(fileContent);

		// populate the result lists
		// System.out.println(segResult.getFinalResult());
		String[] units = segResult.getFinalResult().split("(\\s)+");
		for (String s : units) {
			if (s.indexOf("/") != -1)
				res.add(s.substring(0, s.indexOf("/")));
		}
		*/
		return res;

	}

}