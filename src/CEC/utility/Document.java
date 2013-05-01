package CEC.utility;

import java.util.Vector;

public class Document {

	// ----------------------------------------------------
	// Instance Variables
	// ----------------------------------------------------
	public int[] words;
	public int length;
	public String rawStr;
	private String label;

	// ----------------------------------------------------
	// Constructors
	// ----------------------------------------------------

	public Document() {
		words = null;
		length = 0;
		rawStr = "";
		this.label = "";
	}

	public Document(int length) {
		this.length = length;
		rawStr = "";
		words = new int[length];
		this.label = "";
	}

	public Document(int length, int[] words) {
		this.length = length;
		rawStr = "";
		this.label = "";
		this.words = new int[length];
		for (int i = 0; i < length; ++i) {
			this.words[i] = words[i];
		}
	}

	public Document(int length, int[] words, String rawStr) {
		this.length = length;
		this.rawStr = rawStr;
		this.label = "";
		this.words = new int[length];
		for (int i = 0; i < length; ++i) {
			this.words[i] = words[i];
		}
	}

	public Document(Vector<Integer> doc) {
		this.length = doc.size();
		rawStr = "";
		this.label = "";
		this.words = new int[length];
		for (int i = 0; i < length; i++) {
			this.words[i] = doc.get(i);
		}
	}

	public Document(Vector<Integer> doc, String rawStr) {
		this.length = doc.size();
		this.rawStr = rawStr;
		this.label = "";
		this.words = new int[length];
		for (int i = 0; i < length; ++i) {
			this.words[i] = doc.get(i);
		}
	}

	// ----------------------------------------------------
	// get/set
	// ----------------------------------------------------
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String str) {
		this.label = str;
	}
	
	public String getRawStr() {
		return this.rawStr;
	}

	public void setRawStr(String str) {
		this.rawStr = str;
	}
}
