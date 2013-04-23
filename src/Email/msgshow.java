package Email;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;



public class msgshow {

	static String propertyKey="mail.store.protocol";
    static String protocol="imaps";
    static String host = "pop.gmail.com";
    static String user = "lifanguo@gmail.com";
    static String password = "532463Lifanguo";
    static String mbox = "Inbox";
   
   
   
    static boolean debug = false;
    static boolean showStructure = false;
    static boolean showMessage = true;
    static boolean showAlert = false;
    static boolean saveAttachments = false;
    static int attnum = 1;
    static InputStream msgStream = System.in;

    public static void main(String args[])
    {
	
	try {
	    // Get a Properties object
	    Properties props = System.getProperties();
        props.setProperty(propertyKey, protocol);
	    // Get a Session object
	    Session session = Session.getInstance(props, null);
	    session.setDebug(debug);

	    // Get a Store object
	    Store store = null;
	    store = session.getStore(protocol);
		store.connect(user, password);
	    

	    Folder folder = store.getDefaultFolder();
		mbox = "INBOX";
	    folder = folder.getFolder(mbox);
	    if (folder == null) {
		System.out.println("Invalid folder");
		System.exit(1);
	    }

	    // try to open read/write and if that fails try read-only
	    try {
		folder.open(Folder.READ_WRITE);
	    } catch (MessagingException ex) {
		folder.open(Folder.READ_ONLY);
	    }
	    int totalMessages = folder.getMessageCount();

	    if (totalMessages == 0) {
		System.out.println("Empty folder");
		folder.close(false);
		store.close();
		System.exit(1);
	    }

	    folder.close(false);
	    store.close();
	} catch (Exception ex) {
	    System.out.println("Oops, got exception! " + ex.getMessage());
	    ex.printStackTrace();
	    System.exit(1);
	}
	System.exit(0);
    }

    
}
