package com.objectmentor.wikimail;

import junit.framework.TestCase;
import com.objectmentor.wikimail.EmailParser;

public class EmailParserTest extends TestCase
{
	public void setUp() throws Exception
	{
	}

	public void tearDown() throws Exception
	{
	}

	public void testSimplestEmail() throws Exception
	{
		createAndCheckMessage("unclebob@objectmentor.com", "Test Subject", "Test body.");
		createAndCheckMessage("me", "subject", "body");
	}

	public void testMessage1() throws Exception
	{
		String body = "a test message\n" +
									"\n" +
									"Micah Martin\n" +
									"Object Mentor, Inc.\n" +
									"www.objectmentor.com";
		String subject = "test";
		String from = "Micah Martin <micah@objectmentor.com>";

		checkMessage(message1, body, subject, from);
	}

	public void testMessage2() throws Exception
	{
		String body = "Test body.\n" +
									"\n" +
									"-----\n" +
									"Robert C. Martin (Uncle Bob)  | email: unclebob@objectmentor.com\n" +
									"Object Mentor Inc.            | blog:  www.butunclebob.com\n" +
									"The Agile Transition Experts  | web:   www.objectmentor.com\n" +
									"800-338-6716                  |";
		String subject = "Test subject";
		String from = "\"Robert C. Martin\" <unclebob@objectmentor.com>";

		checkMessage(message2, body, subject, from);
	}

	public void testMessage3() throws Exception
	{
		String body = "David Chelimsky\n" +
									"\n" +
									"Object Mentor, Inc.\n" +
									"\n" +
									"www.objectmentor.com";
		String subject = "simple email";
		String from = "\"David Chelimsky\" <david@objectmentor.com>";

		checkMessage(message3, body, subject, from);
	}

	public void testMessage4() throws Exception
	{
		String body = "";
		String subject = "simple email from home";
		String from = "David Chelimsky <david@chelimsky.org>";

		checkMessage(message4, body, subject, from);
	}

	private void createAndCheckMessage(String from, String subject, String body)
	{
		String emailContent = "From: "+from+"\n" +
		  "To: wiki@objectmentor.com\n" +
		  "Subject: "+subject+"\n" +
		  "\n" +
		  body;

		checkMessage(emailContent, body, subject, from);
	}

	private void checkMessage(String emailContent, String body, String subject, String from)
	{
		Message message = EmailParser.parseMessage(emailContent);

		assertEquals(body, message.getBody());
		assertEquals(subject, message.getSubject());
		assertEquals(from, message.getFrom());
	}

	static String message1 = "Received: from [192.168.1.2] (ip67-153-232-125.z232-153-67.customer.algx.net [::ffff:67.153.232.125])\n" +
	  "  (AUTH: LOGIN micah, SSL: TLSv1/SSLv3,128bits,RC4-SHA)\n" +
	  "  by cliff.objectmentor.com with esmtp; Thu, 02 Sep 2004 10:52:34 -0500\n" +
	  "Mime-Version: 1.0 (Apple com.objectmentor.wikimail.Message framework v619)\n" +
	  "To: wiki@objectmentor.com\n" +
	  "com.objectmentor.wikimail.Message-Id: <19121E62-FCF8-11D8-BF64-000A95A94CA2@objectmentor.com>\n" +
	  "Content-Type: multipart/alternative; boundary=Apple-Mail-79--130965924\n" +
	  "From: Micah Martin <micah@objectmentor.com>\n" +
	  "Subject: test\n" +
	  "Date: Thu, 2 Sep 2004 10:52:31 -0500\n" +
	  "X-Mailer: Apple Mail (2.619)\n" +
	  "\n" +
	  "\n" +
	  "--Apple-Mail-79--130965924\n" +
	  "Content-Transfer-Encoding: 7bit\n" +
	  "Content-Type: text/plain;\n" +
	  "        charset=US-ASCII;\n" +
	  "        format=flowed\n" +
	  "\n" +
	  "a test message\n" +
	  "\n" +
	  "Micah Martin\n" +
	  "Object Mentor, Inc.\n" +
	  "www.objectmentor.com\n" +
	  "\n" +
	  "--Apple-Mail-79--130965924\n" +
	  "Content-Transfer-Encoding: 7bit\n" +
	  "Content-Type: text/enriched;\n" +
	  "        charset=US-ASCII\n" +
	  "\n" +
	  "another test message\n" +
	  "\n" +
	  "<x-tad-bigger>\n" +
	  "\n" +
	  "</x-tad-bigger>Micah Martin\n" +
	  "\n" +
	  "Object Mentor, Inc.\n" +
	  "\n" +
	  "www.objectmentor.com<x-tad-bigger>\n" +
	  "\n" +
	  "</x-tad-bigger>\n" +
	  "--Apple-Mail-79--130965924--\n" +
	  "\n";

	static String message2 = "Received: from LouisWu (ip67-153-232-125.z232-153-67.customer.algx.net [::ffff:67.153.232.125])\n" +
	  "  (AUTH: LOGIN robertcmartin, SSL: TLSv1/SSLv3,128bits,RC4-MD5)\n" +
	  "  by cliff.objectmentor.com with esmtp; Thu, 02 Sep 2004 10:54:38 -0500\n" +
	  "Reply-To: unclebob@objectmentor.com\n" +
	  "From: \"Robert C. Martin\" <unclebob@objectmentor.com>\n" +
	  "To: wiki@objectmentor.com\n" +
	  "Subject: Test subject\n" +
	  "Date: Thu, 2 Sep 2004 10:54:39 -0500\n" +
	  "Organization: Object Mentor Inc.\n" +
	  "MIME-Version: 1.0\n" +
	  "Content-Type: text/plain;\n" +
	  "        charset=\"US-ASCII\"\n" +
	  "Content-Transfer-Encoding: 7bit\n" +
	  "X-Mailer: Microsoft Office Outlook, Build 11.0.5510\n" +
	  "X-MimeOLE: Produced By Microsoft MimeOLE V6.00.2900.2180\n" +
	  "Thread-Index: AcSRBScbgVhK9QJ9RZOKdgXtCPyhrw==\n" +
	  "\n" +
	  "Test body.\n" +
	  "\n" +
	  "-----\n" +
	  "Robert C. Martin (Uncle Bob)  | email: unclebob@objectmentor.com\n" +
	  "Object Mentor Inc.            | blog:  www.butunclebob.com\n" +
	  "The Agile Transition Experts  | web:   www.objectmentor.com\n" +
	  "800-338-6716                  |              \n" +
	  "\n" +
	  "  \n" +
	  "\n";

	static String message3 = "Received: from CHELIMSKY (ip67-153-232-125.z232-153-67.customer.algx.net [::ffff:67.153.232.125])\n" +
	  "  (AUTH: LOGIN david, SSL: TLSv1/SSLv3,128bits,RC4-MD5)\n" +
	  "  by cliff.objectmentor.com with esmtp; Thu, 02 Sep 2004 10:54:39 -0500\n" +
	  "Reply-To: david@objectmentor.com\n" +
	  "From: \"David Chelimsky\" <david@objectmentor.com>\n" +
	  "To: wiki@objectmentor.com\n" +
	  "Subject: simple email\n" +
	  "Date: Thu, 2 Sep 2004 10:55:54 -0500\n" +
	  "Organization: Object Mentor\n" +
	  "MIME-Version: 1.0\n" +
	  "Content-Type: multipart/alternative;\n" +
	  "        boundary=\"----=_NextPart_000_0000_01C490DB.6BAA3190\"\n" +
	  "X-Mailer: Microsoft Office Outlook, Build 11.0.5510\n" +
	  "Thread-Index: AcSRBVQCjFG7jgijTQaB4bjAKffF0A==\n" +
	  "X-MimeOLE: Produced By Microsoft MimeOLE V6.00.2800.1409\n" +
	  "\n" +
	  "This is a multi-part message in MIME format.\n" +
	  "\n" +
	  "------=_NextPart_000_0000_01C490DB.6BAA3190\n" +
	  "Content-Type: text/plain;\n" +
	  "        charset=\"us-ascii\"\n" +
	  "Content-Transfer-Encoding: 7bit\n" +
	  "\n" +
	  " \n" +
	  "\n" +
	  " \n" +
	  "\n" +
	  "David Chelimsky\n" +
	  "\n" +
	  "Object Mentor, Inc.\n" +
	  "\n" +
	  "www.objectmentor.com\n" +
	  "\n" +
	  " \n" +
	  "\n" +
	  "\n" +
	  "------=_NextPart_000_0000_01C490DB.6BAA3190\n" +
	  "Content-Type: text/html;\n" +
	  "        charset=\"us-ascii\"\n" +
	  "Content-Transfer-Encoding: quoted-printable\n" +
	  "\n" +
	  "<html xmlns:o=3D\"urn:schemas-microsoft-com:office:office\" =\n" +
	  "xmlns:w=3D\"urn:schemas-microsoft-com:office:word\" =\n" +
	  "xmlns=3D\"http://www.w3.org/TR/REC-html40\">\n" +
	  "</html>\n" +
	  "\n" +
	  "------=_NextPart_000_0000_01C490DB.6BAA3190--\n" +
	  "\n";

	static String message4 = "Received: from t1.cwihosting.com (t1.cwihosting.com [::ffff:66.216.90.164])\n" +
	  "  by cliff.objectmentor.com with esmtp; Thu, 02 Sep 2004 10:54:58 -0500\n" +
	  "Received: from ip67-153-232-125.z232-153-67.customer.algx.net ([67.153.232.125] helo=[127.0.0.1])\n" +
	  "        by t1.cwihosting.com with asmtp (Exim 4.34)\n" +
	  "        id 1C2tvG-0005iM-8J\n" +
	  "        for wiki@objectmentor.com; Thu, 02 Sep 2004 10:54:50 -0500\n" +
	  "com.objectmentor.wikimail.Message-ID: <41374299.5070906@chelimsky.org>\n" +
	  "Date: Thu, 02 Sep 2004 10:56:09 -0500\n" +
	  "From: David Chelimsky <david@chelimsky.org>\n" +
	  "Reply-To: david@chelimsky.org\n" +
	  "User-Agent: Mozilla Thunderbird 0.7.1 (Windows/20040626)\n" +
	  "X-Accept-Language: en-us, en\n" +
	  "MIME-Version: 1.0\n" +
	  "To: wiki@objectmentor.com\n" +
	  "Subject: simple email from home\n" +
	  "Content-Type: text/plain; charset=us-ascii; format=flowed\n" +
	  "Content-Transfer-Encoding: 7bit\n" +
	  "X-MailScanner-Information: Please contact CWIHosting.com for more information\n" +
	  "X-MailScanner: Found to be clean\n" +
	  "X-AntiAbuse: This header was added to track abuse, please include it with any abuse report\n" +
	  "X-AntiAbuse: Primary Hostname - t1.cwihosting.com\n" +
	  "X-AntiAbuse: Original Domain - objectmentor.com\n" +
	  "X-AntiAbuse: Originator/Caller UID/GID - [47 12] / [47 99]\n" +
	  "X-AntiAbuse: Sender Address Domain - chelimsky.org\n" +
	  "X-Source: \n" +
	  "X-Source-Args: \n" +
	  "X-Source-Dir: \n" +
	  "\n" +
	  "";

}
