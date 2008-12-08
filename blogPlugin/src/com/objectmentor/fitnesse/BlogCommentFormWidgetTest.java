package com.objectmentor.fitnesse;

import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.WikiPage;
import fitnesse.wikitext.widgets.WidgetRoot;
import fitnesse.wikitext.widgets.WidgetTestCase;

public class BlogCommentFormWidgetTest extends WidgetTestCase {
  private WikiPage rootPage;
  private WikiPage page;
  private WidgetRoot root;

  public void setUp() throws Exception {
    rootPage = InMemoryPage.makeRoot("RooT");
    page = rootPage.addChildPage("MyPage");
    root = new WidgetRoot(page);
  }

  public void tearDown() throws Exception {
  }

  public void testRegexp() throws Exception {
    assertHasRegexp("!commentForm", "!commentForm");
    assertHasRegexp("!commentForm SomePage", "!commentForm SomePage");
    assertHasRegexp("!commentForm -r", "!commentForm -r");
    assertHasRegexp("!commentForm -r SomePage", "!commentForm -r SomePage");
  }

  public void testTargetPageDefault() throws Exception {
    BlogCommentFormWidget widget = new BlogCommentFormWidget(root,
      "!commentForm"
    );
    assertEquals("MyPage", widget.targetPageName);
  }

  public void testTargetPageSet() throws Exception {
    BlogCommentFormWidget widget = new BlogCommentFormWidget(root,
      "!commentForm SomePage"
    );
    assertEquals("SomePage", widget.targetPageName);
  }

  public void testRender() throws Exception {
    BlogCommentFormWidget widget = new BlogCommentFormWidget(root,
      "!commentForm"
    );
    String content = widget.render();

    assertSubString("<form method=\"post\" action=\"MyPage\">", content);
    assertSubString(
      "<input type=\"hidden\" name=\"responder\" value=\"addBlogComment\"/>",
      content
    );
    assertSubString("<input type=\"hidden\" name=\"date\"", content);
    assertSubString(
      "<input type=\"hidden\" name=\"sourcePage\" value=\"MyPage\"/>", content
    );
    assertSubString("<input type=\"text\" name=\"bloggerName\" value=\"\"/>",
      content
    );
    assertSubString(
      "<input type=\"text\" name=\"subject\" value=\"\" size=\"80\"/>", content
    );
    assertSubString(
      "<textarea name=\"comment\" cols=\"80\" rows=\"6\"></textarea>", content
    );
    assertSubString(
      "<input type=\"submit\" name=\"blog\" value=\"Append Comment\"/>", content
    );
  }

  public void testReverseOrderOption() throws Exception {
    BlogCommentFormWidget widget = new BlogCommentFormWidget(root,
      "!commentForm"
    );
    assertFalse(widget.reverseOn);
    widget = new BlogCommentFormWidget(root, "!commentForm -r");
    assertTrue(widget.reverseOn);

    String content = widget.render();
    assertSubString("<input type=\"hidden\" name=\"reverse\" value=\"1\"/>",
      content
    );
  }

  protected String getRegexp() {
    return BlogCommentFormWidget.REGEXP;
  }
}
