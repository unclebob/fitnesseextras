package com.objectmentor.wikimail;

public class Message
{
	protected StringBuffer body = new StringBuffer();
	protected String subject;
	protected String from;

	public Message()
	{
	}

	public Message(String from, String subject, String body)
	{
		this.from = from;
		this.subject = subject;
		this.body.append(body);
	}

	public String getBody()
	{
		return body.toString().trim();
	}

	public String getSubject()
	{
		return subject;
	}

	public String getFrom()
	{
		return from;
	}
}
