package fitnesse.wikitext.widgets;

import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;

public class RssLinkWidgetTest extends WidgetTestCase {
  private WikiPage root;
  private WikiPage page;
  private RssLinkWidget widget;

  public void setUp() throws Exception {
    root = InMemoryPage.makeRoot("RooT");
    page = root.getPageCrawler().addPage(root, PathParser.parse("SomePage"),
      "some text"
    );
    widget = new RssLinkWidget(new WidgetRoot(page), "!rsslink Test Link");
  }

  public void testGetLink() {
    assertEquals("Test Link", widget.getLink());
  }

  public void testRegExp() {
    assertNoMatch("x !rsslink Test Link");
    assertEquals("!rsslink Test Link", "!rsslink Test Link");
  }

  public void testGetLinkFromPage() throws Exception {
    PageData data = page.getData();
    assertEquals(widget.getLink(), data.getAttribute("rsslink"));
  }

  public void testRender() throws Exception {
    assertEquals("", widget.render());
  }

  protected String getRegexp() {
    return RssLinkWidget.REGEXP;
  }
}
