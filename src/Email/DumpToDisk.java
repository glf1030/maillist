package Email;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.mail.Address;



public class DumpToDisk 
{
	

	public static void dump(Email[] emails, String fileName)
	{
		BufferedWriter writer;
	    try
	    {
	    	
	    	writer=new BufferedWriter(new FileWriter(fileName));
	    	
	    	for(Email e:emails)
	    	{
	    		
	    		if(e.getSentDate()!=null)
	    		writer.write("SentDate:"+"\t"+e.getSentDate());
	    		writer.write("\r\n");
	    		writer.write("Sender:"+"\t");
	    		if(e.getSender()!=null)
	    		{
	    		for(Address sender:e.getSender())
	    			writer.write(sender+"\t");
	    		}
	    		writer.write("\r\n");
	    		writer.write("ReplyTo:");
	    		if(e.getReplyTo()!=null)
	    		{
	    			for(Address replyTo:e.getReplyTo())
	    				writer.write(replyTo.toString()+"\t");
	    		}
	    	    writer.write("\r\n");
	    		writer.write("Subject:"+"\t");
	    		if(e.getSubject()!=null)
	    		{
	    		writer.write(e.getSubject());
	    		}
	    		writer.write("\r\n");
	    		writer.write("Content:"+"\t");
	    		if(e.getContent()!=null)
	    		writer.write(e.getContent());
	    		writer.write("\r\n");
	    		writer.write("Receiver:"+"\t");
	    		if(e.getReceiver()!=null)
	    		{
	    		for(Address receiver:e.getReceiver())
	    		writer.write(receiver.toString()+"\t");
	    		}
	    		writer.write("\r\n");
	    		writer.write("Flags:"+"\t");
	    		if(e.getFlags()!=null)
	    		{
	    			writer.write(e.getFlags());
	    		}
	    		writer.write("\r\n");
	    		
	    		
	    	}

	    	writer.flush();
	    	writer.close();
	    	
	    	
	    	
	    	writer.close();
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	
	    	
	    }
	}
	
	
	public static void dumpToFiles(Email[] emails,String folderName)
	{
		try
		{
			
			for(int i=0;i<emails.length;i++)
			{
				File eFile=new File(folderName+"\\"+i+".txt");
				if(!eFile.exists())
					eFile.createNewFile();
				BufferedWriter writer=new BufferedWriter(new FileWriter(folderName+"\\"+i+".txt"));
				writer.write(emails[i].getContent());
				writer.flush();
				writer.close();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		
	}

}
