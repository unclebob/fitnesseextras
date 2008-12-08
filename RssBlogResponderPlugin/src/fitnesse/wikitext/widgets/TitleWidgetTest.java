package fitnesse.wikitext.widgets;

import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;

public class TitleWidgetTest extends WidgetTestCase {
  private WikiPage root;
  private WikiPage page;
  private TitleWidget widget;

  public void setUp() throws Exception {
    root = InMemoryPage.makeRoot("RooT");
    page = root.getPageCrawler().addPage(root, PathParser.parse("SomePage"),
      "some text"
    );
    widget = new TitleWidget(new WidgetRoot(page), "!title Test Title");
  }

  public void testGetTitle() {
    assertEquals("Test Title", widget.getTitle());
  }

  protected String getRegexp() {
    return TitleWidget.REGEXP;
  }

  public void testRegExp() {
    assertNoMatch("x !title Test Page");
    assertEquals("!title Test Page", "!title Test Page");
  }

  public void testGetTitleFromPage() throws Exception {
    PageData data = page.getData();
    assertEquals(widget.getTitle(), data.getAttribute("title"));
  }

  public void testRender() throws Exception {
    assertEquals(
      TitleWidget.FORMAT_START + "Test Title" + TitleWidget.FORMAT_END,
      widget.render()
    );
  }
}
