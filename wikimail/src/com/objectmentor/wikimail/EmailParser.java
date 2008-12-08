package com.objectmentor.wikimail;

import java.util.regex.*;

public class EmailParser
{
	private static Pattern fromPattern = Pattern.compile("From: (.*)");
	private static Pattern subjectPattern = Pattern.compile("Subject: (.*)");
	private static Pattern boundaryPattern = Pattern.compile("boundary=\"?([^\"]*)\"?");

	private static final int IN_HEADER = 0;
	private static final int IN_TEXT_BODY = 1;
	private static final int DONE = 2;
	private static final int IN_MULTIPART_HEADER = 3;
	private static final int IN_PART_HEADER = 4;
	private static final int IN_TEXT_PART_BODY = 5;

	private static final int LINE = 0;
	private static final int BLANK = 1;
	private static final int MULTIPART_HEADER = 3;
	private static final int BOUNDARY = 4;

	private Matcher matcher;
	private String boundary = null;
	private boolean inPlainPart = false;
	private Message message = new Message();

	public Message parse(String emailContent)
	{
		inPlainPart = false;

		message = new Message();

		String[] lines = emailContent.split("\n");

		int state = IN_HEADER;
		int eventCode = -1;
		for(int i = 0; i < lines.length && state != DONE; i++)
		{
			String line = lines[i];
			eventCode = parseLine(line);
			switch(state)
			{
				case IN_HEADER:
					switch(eventCode)
					{
						case LINE:
							checkHeader(line);
							break;
						case BLANK:
							state = IN_TEXT_BODY;
							break;
						case MULTIPART_HEADER:
						  state = IN_MULTIPART_HEADER;
							break;
					}
					break;
				case IN_TEXT_BODY:
					switch(eventCode)
					{
						case LINE:
						case BLANK:
							message.body.append("\n").append(line);
							break;
					}
					break;
				case IN_MULTIPART_HEADER:
				  switch(eventCode)
				  {
				    case LINE:
					    checkHeader(line);
					    break;
					  case BOUNDARY:
					    state = IN_PART_HEADER;
					    break;
				  }
					break;
				case IN_PART_HEADER:
				  switch(eventCode)
				  {
				    case LINE:
					    checkHeader(line);
					    break;
					  case BLANK:
					    if(inPlainPart)
					      state = IN_TEXT_PART_BODY;
					    break;
				  }
					break;
				case IN_TEXT_PART_BODY:
				  switch(eventCode)
				  {
				    case LINE:
					  case BLANK:
							message.body.append("\n").append(line);
					    break;
					  case BOUNDARY:
					    state = DONE;
					    break;
				  }
					break;
			}
		}

		return message;
	}

	private int parseLine(String line)
	{
		if("".equals(line))
			return BLANK;
		else if(boundary != null && ("--" + boundary).equals(line))
			return BOUNDARY;
		else if(matchesPattern(boundaryPattern, line))
		{
			boundary = matcher.group(1);
			return MULTIPART_HEADER;
		}
		else
			return LINE;
	}

	private void checkHeader(String line)
	{
		if(matchesPattern(fromPattern, line))
			message.from = matcher.group(1);
		else if(matchesPattern(subjectPattern, line))
			message.subject = matcher.group(1);
		else if(line.startsWith("Content-Type: text/plain"))
			inPlainPart = true;

	}

	private boolean matchesPattern(Pattern pattern, String line)
	{
		matcher = pattern.matcher(line);
		return matcher.find();
	}

	public static Message parseMessage(String emailContent)
	{
		return new EmailParser().parse(emailContent);
	}
}
