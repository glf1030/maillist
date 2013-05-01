package CEC.utility;

import java.util.HashSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Stopwords{
	
	public HashSet<String> words;
	
	//test
	public static void main(String[] args){
		Stopwords sw = new Stopwords();
	}
	
	/*
	 * Constructor
	 */
	public Stopwords(){
		try{
			
			words = new HashSet<String>();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("./stopwords.txt"), "GBK"));
			
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
			//		new FileOutputStream("./test.txt"),"GBK"));
			
			String str = "";
			
			while((str=reader.readLine())!=null){
				words.add(str.trim());
				//writer.append(str.trim()+"\n");
			}
			
			//writer.flush();
			//writer.close();
			reader.close();
				
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//return true if the given word exist, otherwise false
	public boolean contains(String str){
		
		return words.contains(str);
	}
	
}