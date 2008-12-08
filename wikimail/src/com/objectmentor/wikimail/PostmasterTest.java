package com.objectmentor.wikimail;

import fitnesse.authentication.OneUserAuthenticator;
import fitnesse.testutil.FitNesseUtil;
import fitnesse.wiki.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class PostmasterTest extends fitnesse.testutil.RegexTestCase {
  private Postmaster pm;
  private WikiPage root;
  private PageCrawler crawler;
  private ByteArrayOutputStream output;

  public void setUp() throws Exception {
    pm = new Postmaster();
    output = new ByteArrayOutputStream();
    pm.output = new PrintStream(output);
    root = InMemoryPage.makeRoot("RooT");
    crawler = root.getPageCrawler();

    FitNesseUtil.startFitnesse(root);
    pm.host = "localhost";
    pm.port = FitNesseUtil.port;
  }

  public void tearDown() throws Exception {
    FitNesseUtil.stopFitnesse();
  }

  public void testArgs() throws Exception {
    pm.args("localhost 80".split(" "));
    assertEquals("localhost", pm.host);
    assertEquals(80, pm.port);
  }

  public void testArgsAuthenticationCredentials() throws Exception {
    pm.args("-a user pass fitnesse.org 8080".split(" "));
    assertEquals("fitnesse.org", pm.host);
    assertEquals(8080, pm.port);
    assertTrue(pm.usingAuthentication());
    assertEquals("user", pm.username);
    assertEquals("pass", pm.password);
  }

  public void testDeliverMessage() throws Exception {
    crawler.addPage(root, PathParser.parse("SomePage"), "original content.");
    Message message = new Message("Joe Bloe <joe@company.com>", "SomePage",
      "Joe's new content."
    );
    pm.deliver(message);

    PageData data = crawler.getPage(root, PathParser.parse("SomePage"))
      .getData();
    String content = data.getContent();
    assertSubString("original content.", content);
    assertSubString("Joe's new content.", content);
    assertSubString("----", content);
    assertSubString("!meta Emailed by Joe Bloe <joe@company.com>", content);

    assertSubString("Message delivered successfully.", output.toString());
  }

  public void testPageNotFoundHandling() throws Exception {
    Message message = new Message("joe@company.com", "SomePage",
      "email content"
    );
    pm.deliver(message);

    PageData data = crawler.getPage(root, PathParser.parse("SomePage"))
      .getData();
    assertSubString("email content", data.getContent());
  }

  public void testBadSubjectHandling() throws Exception {
    Message message = new Message("joe@company.com", "non wiki word subject",
      "email content"
    );
    pm.deliver(message);

    WikiPage mailPage = crawler.getPage(root, PathParser.parse("WikiMail"));
    assertNotNull(mailPage);

    List children = mailPage.getChildren();
    assertEquals(1, children.size());

    PageData data = ((WikiPage) children.get(0)).getData();
    assertSubString("email content", data.getContent());

    assertSubString("The subject is not a wikiword.  Saving as ",
      output.toString()
    );
  }

  public void testGetMessage() throws Exception {
    String message = "To: wiki@objectmentor.com\n" +
      "From: Micah Martin <micah@objectmentor.com>\n" +
      "Subject: SomePage\n" +
      "\n" +
      "Test Message";
    InputStream input = new ByteArrayInputStream(message.getBytes());
    Message email = pm.getMessage(input);
    assertEquals("Micah Martin <micah@objectmentor.com>", email.getFrom());
    assertEquals("SomePage", email.getSubject());
    assertEquals("Test Message", email.getBody());
  }

  public void testMissingRequiredCredentials() throws Exception {
    secureRoot();

    Message message = new Message("joe@company.com", "SomePage",
      "email content"
    );
    pm.deliver(message);

    assertEquals(401, pm.exitCode);
    assertSubString("Unauthorized", output.toString());
  }

  public void testRequiredCredentialsSupplied() throws Exception {
    secureRoot();
    pm.username = "Aladdin";
    pm.password = "open sesame";

    Message message = new Message("joe@company.com", "SomePage",
      "email content"
    );
    pm.deliver(message);

    assertEquals(0, pm.exitCode);
    assertSubString("Message delivered successfully.", output.toString());
  }

  private void secureRoot() throws Exception {
    FitNesseUtil.context.authenticator = new OneUserAuthenticator("Aladdin",
      "open sesame"
    );
    PageData data = root.getData();
    data.setAttribute("secure-read");
    data.setAttribute("secure-write");
    root.commit(data);
  }
}
