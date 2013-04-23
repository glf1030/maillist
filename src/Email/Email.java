package Email;

import javax.mail.Address;
import javax.mail.Flags;



public class Email 
{


	public String sentDate;
	public Address[] sender;
	public String subject;
	public String content;
	public Address[] receiver;
	public Address[] replyTo;
	public String flags;
	
	public Email()
	{
		
	}
	
	public void setSentDate(String d)
	{
		this.sentDate=d;
	
	}
	public void setSender(Address[] s)
	{
		this.sender=s;
	}
	public void setSubject(String s)
	{
		this.subject=s;
	}
	public void setContent(String c)
	{
		this.content=c;
	}
	public void setReceiver(Address[] r)
	{
		this.receiver=r;
	}
	
	public void setReplyTo(Address[] r)
	{
		this.replyTo=r;
	}
	
	public void setFlags(String f)
	{
	     this.flags=f;
	}
	
	
	public String getSentDate()
	{
		return this.sentDate;
	}
	public Address[] getSender()
	{
		return this.sender;
	}
	public String getSubject()
	{
		return this.subject;
	}
	public String getContent()
	{
		return this.content;
	}
	public Address[] getReceiver()
	{
		
		return this.receiver;
	}
	public Address[] getReplyTo()
	{
		return this.replyTo;
	}
	
	public String getFlags()
	{
		return this.flags;
	}
}
