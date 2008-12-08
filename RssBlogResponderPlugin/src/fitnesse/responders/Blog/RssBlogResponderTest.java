package fitnesse.responders.Blog;

import fitnesse.Responder;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.ResponderTestCase;
import fitnesse.util.XmlUtil;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RssBlogResponderTest extends ResponderTestCase {
  protected Element channelElement;
  protected Element rssElement;
  protected Document rssDoc;

  protected Responder responderInstance() {
    return new RssBlogResponder();
  }

  public void testTitleField() throws Exception {
    WikiPage page = crawler.addPage(root, PathParser.parse("MyNewPage"));
    PageData data = new PageData(page);
    data.setAttribute("title", "My Title");
    data.setContent("content");
    page.commit(data);
    NodeList items = getReportedItems("|MyNewPage|me|now|");
    assertEquals(1, items.getLength());
    String title = "My Title";
    String author = "me";
    String pubDate = "now";
    String description = "content";
    checkItem(items.item(0), title, author, pubDate, description,
      "http://localhost/MyNewPage"
    );
  }

  public void testRssLinkField() throws Exception {
    WikiPage page = crawler.addPage(root, PathParser.parse("MyNewPage"));
    PageData data = new PageData(page);
    data.setAttribute("rsslink", "My Link");
    data.setContent("content");
    page.commit(data);
    NodeList items = getReportedItems("|MyNewPage|me|now|");
    assertEquals(1, items.getLength());
    String title = "MyNewPage";
    String author = "me";
    String pubDate = "now";
    String description = "content";
    String link = "http://localhost/My Link";
    checkItem(items.item(0), title, author, pubDate, description, link);
  }

  public void testReportedPagesSelectedByResource() throws Exception {
    //todo get these from FitNesseContext the next time we update the jar file on shelob.
    String recentChangesDateFormat = "kk:mm:ss EEE, MMM dd, yyyy";
    SimpleDateFormat dateFormat = new SimpleDateFormat(recentChangesDateFormat);
    String date = dateFormat.format(new Date());

    WikiPage page = crawler.addPage(root, PathParser.parse("FrontPage"));
    PageData data = page.getData();
    data.setAttribute("title", "my title");
    data.setContent("content");
    page.commit(data);

    request.setResource("FrontPage");
    String page1 = "|SomePage|me|" + date + "|";
    String page2 = "|FrontPage|me|" + date + "|";
    String page3 = "|FrontPage.MyPage|me|" + date + "|";
    String page4 = "|SomePage.FrontPage|me|" + date;

    String recentChangesContent =
      page1 + "\n" + page2 + "\n" + page3 + "\n" + page4 + "\n";
    NodeList items = getReportedItems(recentChangesContent);
    assertEquals(2, items.getLength());
    checkItem(items.item(0), "my title", "me", date, "content",
      "http://localhost/FrontPage"
    );
    checkItem(items.item(1), "FrontPage.MyPage", "me", date, null,
      "http://localhost/FrontPage.MyPage"
    );
  }

  private void checkItem(
    Node node, String title, String author, String pubDate, String description,
    String link
  )
    throws Exception {
    Element itemElement = (Element) node;
    assertEquals(title, XmlUtil.getTextValue(itemElement, "title"));
    assertEquals(author, XmlUtil.getTextValue(itemElement, "author"));
    assertEquals(pubDate, XmlUtil.getTextValue(itemElement, "pubDate"));
    assertEquals(description, XmlUtil.getTextValue(itemElement, "description"));
    assertEquals(link, XmlUtil.getTextValue(itemElement, "link"));
  }

  private NodeList getReportedItems(String recentChangesContent)
    throws Exception {
    crawler.addPage(root, PathParser.parse("RecentChanges"),
      recentChangesContent
    );
    buildRssChannel();
    NodeList items = channelElement.getElementsByTagName("item");
    return items;
  }

  private void buildRssChannel() throws Exception {
    SimpleResponse response = (SimpleResponse) responder.makeResponse(context,
      request
    );
    rssDoc = XmlUtil.newDocument(response.getContent());
    rssElement = rssDoc.getDocumentElement();
    channelElement = XmlUtil.getElementByTagName(rssElement, "channel");
  }
}
