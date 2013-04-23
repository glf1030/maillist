package Email;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import javax.mail.search.FlagTerm;

import com.sun.mail.util.BASE64DecoderStream;

public class TestToReadEmails 
{
	static boolean showStructure = false;
	 static boolean saveAttachments = false;
	 static int attnum = 1;
	 static String mailService="pop.gmail.com";
	 static String name="dsus401@gmail.com";
	 static String password="dsusdsus";
	 static String fileLocation="D:\\DiaosiWang\\Data\\EmailTest.txt";
	 static String filesFolder="D:\\DiaosiWang\\Data\\EmailTest";
	 
	
  public static void main(String args[]) throws IOException 
  {
	     TestToReadEmails test=new TestToReadEmails();
	     ArrayList<Email> emails=new ArrayList<Email>();
         Properties properties = System.getProperties();
         // fetch emails through imaps.
         properties.setProperty("mail.store.protocol", "imaps");
         
         
             try {
                 Session session = Session.getDefaultInstance(properties, null);
                 //create session instance
                 Store store = session.getStore("imaps");//create store instance
                 store.connect(mailService, name, password);
                 //set your user_name and password
                
//            
                Folder inbox = store.getFolder("Inbox");
                 //set folder from where u wants to read mails
                 inbox.open(Folder.READ_WRITE);//set access type of Inbox
             
              
                FlagTerm ft=new FlagTerm(new Flags(Flags.Flag.SEEN),false);
                 //inbox.search(ft);
                 
               // Message messages[] = inbox.search(ft);// gets inbox messages
                 Message messages[]=inbox.getMessages();
                 System.out.println(messages.length);
                
                 for (int i = 0; i <messages.length; i++) 
                 {
//                System.out.println("------------ Message " + (i + 1) + " ------------");
//                System.out.println("SentDate : " + messages[i].getSentDate()); //print sent date
//                System.out.println("From : " + messages[i].getFrom()[0]); //print email id of sender
//                  System.out.println("Sub : " + messages[i].getSubject()); //print subject of email

             try
                {
                	Email e=new Email();
                	test.dumpPart(messages[i],e);
                	System.out.println(e.getContent());
                    emails.add (e);
                    
                }
                catch (Exception ex)
                {
                     System.out.println("Exception arise at get Content");
                     ex.printStackTrace();
                }
                
           }
                
           Email[] emailsToDump=new Email[emails.size()];
           for(int i=0;i<emails.size();i++)
           {
        	   emailsToDump[i]=emails.get(i);
           }
           DumpToDisk.dumpToFiles(emailsToDump, filesFolder);
           store.close();
      }
catch (Exception e) {
System.out.println(e);  
}  
}
 
 
  public void dumpPart(Part p,Email e) throws Exception 
  {
	    if(p instanceof Message)
	      dumpEnvelope((Message)p,e);

	  
		String ct = p.getContentType();
		try {
		    pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
		} catch (ParseException pex) {
		    pr("BAD CONTENT-TYPE: " + ct);
		}
		String filename = p.getFileName();
		if (filename != null)
		    pr("FILENAME: " + filename);

		/*
		 * Using isMimeType to determine the content type avoids
		 * fetching the actual content data until we need it.
		 */
		if (p.isMimeType("text/plain")) 
		{
		    pr("This is plain text");
		    pr("---------------------------");
		    e.setContent((String)p.getContent());
	        System.out.println(e.getContent());
	        pr("---------------------------");
		} 
		else if 
		(p.isMimeType("multipart/*")) 
		{
		    pr("This is a Multipart");
		    pr("---------------------------");
		    Multipart mp = (Multipart)p.getContent();
		    level++;
		    int count = mp.getCount();
		    for (int i = 0; i < count; i++)
			dumpPart(mp.getBodyPart(i),e);
		    level--;
		} 
		else if 
		(p.isMimeType("message/rfc822")) 
		{
		    pr("This is a Nested Message");
		    pr("---------------------------");
		    level++;
		    dumpPart((Part)p.getContent(),e);
		    level--;
		} 
		else 
		{
		    if (!showStructure && !saveAttachments) 
		    {
			Object o = p.getContent();
			if (o instanceof String) {
			    pr("This is a string");
			    pr("---------------------------");
			    System.out.println((String)o);
			} else if (o instanceof InputStream) {
			    pr("This is just an input stream");
			    pr("---------------------------");
			    pr("INPUTSTREAM");
//			    InputStream is = (InputStream)o;
//			    int c;
//			    while ((c = is.read()) != -1)
//				System.out.write(c);
			} else {
			    pr("This is an unknown type");
			    pr("---------------------------");
			    pr(o.toString());
			 
			}
		    } else {
			// just a separator
			pr("---------------------------");
		    }
		}

		/*
		 * If we're saving attachments, write out anything that
		 * looks like an attachment into an appropriately named
		 * file.  Don't overwrite existing files to prevent
		 * mistakes.
		 */
		if (saveAttachments && level != 0 && !p.isMimeType("multipart/*")) {
		    String disp = p.getDisposition();
		    // many mailers don't include a Content-Disposition
		    if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
			if (filename == null)
			    filename = "Attachment" + attnum++;
			pr("Saving attachment to file " + filename);
			try {
			    File f = new File(filename);
			    if (f.exists())
				// XXX - could try a series of names
				throw new IOException("file exists");
			    ((MimeBodyPart)p).saveFile(f);
			} catch (IOException ex) {
			    pr("Failed to save attachment: " + ex);
			}
			pr("---------------------------");
		    }
		}
		
		
	  }




		public  Email dumpEnvelope(Message m, Email e) throws Exception 
	    {
	    	
	    	
		pr("This is the message envelope");
		pr("---------------------------");
		Address[] a;
		
		// FROM 
		if ((a = m.getFrom()) != null) 
		{
			e.setSender(a);
		    for (int j = 0; j < a.length; j++)
			pr("FROM: " + a[j].toString());
		}

		// REPLY TO
		if ((a = m.getReplyTo()) != null) 
		{
			e.setReplyTo(a);
		    for (int j = 0; j < a.length; j++)
			pr("REPLY TO: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null)
		{
			e.setReceiver(a);
		    for (int j = 0; j < a.length; j++) {
			pr("TO: " + a[j].toString());
			InternetAddress ia = (InternetAddress)a[j];
			if (ia.isGroup()) {
			    InternetAddress[] aa = ia.getGroup(false);
			    for (int k = 0; k < aa.length; k++)
				pr("  GROUP: " + aa[k].toString());
			}
		    }
		}

		// SUBJECT
		e.setSubject(m.getSubject());
		pr("SUBJECT: " + m.getSubject());

		// DATE
		e.setSentDate(m.getSentDate().toString());
		Date d = m.getSentDate();
		pr("SendDate: " +
		    (d != null ? d.toString() : "UNKNOWN"));

		// FLAGS
	   
		Flags flags = m.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
		    String s;
		    Flags.Flag f = sf[i];
		    if (f == Flags.Flag.ANSWERED)
			s = "\\Answered";
		    else if (f == Flags.Flag.DELETED)
			s = "\\Deleted";
		    else if (f == Flags.Flag.DRAFT)
			s = "\\Draft";
		    else if (f == Flags.Flag.FLAGGED)
			s = "\\Flagged";
		    else if (f == Flags.Flag.RECENT)
			s = "\\Recent";
		    else if (f == Flags.Flag.SEEN)
			s = "\\Seen";
		    else
			continue;	// skip it
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(s);
		}

		String[] uf = flags.getUserFlags(); // get the user flag strings
		for (int i = 0; i < uf.length; i++) {
		    if (first)
			first = false;
		    else
			sb.append(' ');
		    sb.append(uf[i]);
		}
		pr("FLAGS: " + sb.toString());
		 e.setFlags(sb.toString());

		// X-MAILER
		String[] hdrs = m.getHeader("X-Mailer");
		if (hdrs != null)
		    pr("X-Mailer: " + hdrs[0]);
		else
		    pr("X-Mailer NOT available");
		return e;
	    }

	    static String indentStr = "                                               ";
	    static int level = 0;

	    /**
	     * Print a, possibly indented, string.
	     */
	    public static void pr(String s) 
	    {
		if (showStructure)
		    System.out.print(indentStr.substring(0, level * 2));
		System.out.println(s);
	    }




} 
