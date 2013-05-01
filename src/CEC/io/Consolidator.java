package CEC.io;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
import java.util.ArrayList;

public class Consolidator{
	
	public String path;
	
	public Consolidator(String str){
		path = str;
	}
	
	public String returnConsolidatedFile(){
		
		String res = "";
		
		try{
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("./stopwords.txt")));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
					   (new FileOutputStream("./inputFiles.txt"), "GBK"));
			
			File[] subfolders = new File(path).listFiles();
			
			for(File sfolder : subfolders){
				File[] files = sfolder.listFiles();
				String label = sfolder.getName();
				System.out.println(label);
				
				for(File f: files){
					
					String rawStr = "";
					String tmp = "";
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					
					while((tmp = reader.readLine())!=null){
						rawStr += tmp;
					}
					
					rawStr = rawStr.replaceAll("[\n|-|>|¡·|¨D|*|\t]+", " ");
					rawStr = rawStr.replaceAll("[ ]+", " ");
					
					writer.append(label+"\n");
					writer.append(rawStr+"\n");
				}
				
			}
			
			reader.close();
			
			writer.flush();
			writer.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	//test
	public static void main(String[] args){
		Consolidator c = new Consolidator("./emails");
		String str = c.returnConsolidatedFile();
		System.out.println(str);
	}
	
}