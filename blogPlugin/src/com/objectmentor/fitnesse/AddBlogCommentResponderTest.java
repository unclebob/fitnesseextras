package com.objectmentor.fitnesse;

import fitnesse.Responder;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.ResponderTestCase;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class AddBlogCommentResponderTest extends ResponderTestCase {
  private WikiPagePath blogPagePath;
  private static final String MARKER = AddBlogCommentResponder.Marker;

  // Return an instance of the Responder being tested.
  protected Responder responderInstance() {
    return new AddBlogCommentResponder();
  }

  public void setUp() throws Exception {
    super.setUp();
    blogPagePath = PathParser.parse("BlogPage");
    crawler.addPage(root, blogPagePath, "original text\n");
    request.addInput("page", "BlogPage");
    request.addInput("date", "today");
    request.addInput("sourcePage", "SourcePage");
    request.addInput("bloggerName", "Bob");
    request.addInput("subject", "the subject");
    request.setResource("BlogPage");
  }

  public void testAppendComment() throws Exception {
    String content = getContentAfterAddingComment("the comment");

    assertHasRegexp("original text", content);
    assertHasRegexp("\\!\\* today, Bob, the subject", content);
    assertHasRegexp("the comment", content);
    assertHasRegexp("\\*\\!", content);
  }

  private String getContentAfterAddingComment(String comment) throws Exception {
    addComment(comment);

    WikiPage page = crawler.getPage(root, blogPagePath);
    PageData data = page.getData();
    String content = data.getContent();
    return content;
  }

  private void addComment(String comment) throws Exception {
    request.addInput("comment", comment);
    SimpleResponse response = (SimpleResponse) responder.makeResponse(context,
      request
    );
    assertEquals(303, response.getStatus());
    assertHasRegexp("Location: SourcePage", response.makeHttpHeaders());
  }

  public void testBadPage() throws Exception {
    root.removeChildPage("BlogPage");

    SimpleResponse response = (SimpleResponse) responder.makeResponse(context,
      request
    );
    assertEquals(404, response.getStatus());
  }

  public void testAddsMarker() throws Exception {
    request.addInput("reverse", "1");
    String content = getContentAfterAddingComment("the comment");
    assertSubString(MARKER, content);
  }

  public void testAddMarkerOnlyOnce() throws Exception {
    request.addInput("reverse", "1");
    addComment("1st comment");
    String content = getContentAfterAddingComment("2nd comment");

    int indexOfMarker = content.indexOf(MARKER);
    assertTrue(indexOfMarker != -1);
    int endOfMarkerIndex = indexOfMarker + MARKER.length();

    assertNotSubString(MARKER, content.substring(endOfMarkerIndex));
  }

  public void testReverseOrder() throws Exception {
    request.addInput("reverse", "1");
    addComment("1st comment");
    String content = getContentAfterAddingComment("2nd comment");

    int indexOfFirstComment = content.indexOf("1st comment");
    int indexOfSecondComment = content.indexOf("2nd comment");

    assertTrue(indexOfFirstComment != -1);
    assertTrue(indexOfSecondComment != -1);
    assertTrue(indexOfSecondComment < indexOfFirstComment);
  }

  public void testRecentChangesIsUpdated() throws Exception {
    addComment("comment");

    WikiPage page = root.getChildPage("RecentChanges");
    assertNotNull(page);
    String content = page.getData().getContent();
    assertSubString("BlogPage", content);
  }
}
