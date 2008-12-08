package com.objectmentor.fitnesse.releases;

import fitnesse.util.FileUtil;
import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.WikiPage;
import fitnesse.wikitext.widgets.MockWidgetRoot;
import fitnesse.wikitext.widgets.WidgetRoot;
import fitnesse.wikitext.widgets.WidgetTestCase;

public class ReleaseWidgetTest extends WidgetTestCase {
  private WikiPage page;
  private WidgetRoot widgetRoot;

  public void setUp() throws Exception {
    WikiPage root = InMemoryPage.makeRoot("RooT");
    page = root.addChildPage("ThePage");
    widgetRoot = new WidgetRoot(page);
  }

  public void tearDown() throws Exception {
    FileUtil.deleteFileSystemDirectory("releases");
  }

  public void testRegexp() throws Exception {
    assertMatchEquals("!release somerelease", "!release somerelease");
  }

  public void testReleaseName() throws Exception {
    ReleaseWidget widget = new ReleaseWidget(new MockWidgetRoot(),
      "!release somerelease"
    );
    assertEquals("somerelease", widget.getReleaseName());
  }

  public void testRenderWithNoDirectory() throws Exception {
    ReleaseWidget widget = new ReleaseWidget(new MockWidgetRoot(),
      "!release xyz"
    );
    String html = widget.render();
    assertSubString("release xyz is Corrupted", html);
    assertSubString("The release directory could not be found.", html);
  }

  public void testRenderWithNoPackages() throws Exception {
    FileUtil.makeDir("releases");
    FileUtil.makeDir("releases/xyz");
    ReleaseWidget widget = new ReleaseWidget(new MockWidgetRoot(),
      "!release xyz"
    );
    String html = widget.render();
    assertSubString("release xyz", html);
    assertSubString("There are no files in this release.", html);
  }

  public void testRenderWithPackages() throws Exception {
    makeSampleRelease();
    ReleaseWidget widget = new ReleaseWidget(widgetRoot, "!release xyz");
    String html = widget.render();
    assertSubString("release xyz", html);
    assertSubString("package1", html);
    assertSubString(
      "<a href=\"/package1?responder=releaseDownload&release=xyz\">", html
    );
    assertSubString("package2", html);
    assertSubString(
      "<a href=\"/package2?responder=releaseDownload&release=xyz\">", html
    );
  }

  public void testReverseOrder() throws Exception {
    makeSampleRelease();
    ReleaseWidget widget = new ReleaseWidget(widgetRoot, "!release -r xyz");
    assertTrue(widget.reverseOrder);
    String html = widget.render();
    int indexOf1 = html.indexOf("package1");
    int indexOf2 = html.indexOf("package2");

    assertTrue(indexOf1 > 0);
    assertTrue(indexOf2 > 0);
    assertTrue(indexOf1 > indexOf2);
  }

  public void testReleaseLink() throws Exception {
    createReleaseInfo();
    ReleaseWidget widget = new ReleaseWidget(widgetRoot, "!release xyz someurl"
    );
    assertEquals("someurl", widget.href);
    String html = widget.render();
    assertSubString("<a href=\"someurl\">xyz</a>", html);
  }

  private void createReleaseInfo() {
    String line1 = "fitnesse20050301.zip\t1062799\t1109816900000\t10\n";
    String line2 = "fitnesse_src20050301.zip\t3362842\t1109817028000\t9\n";
    FileUtil.makeDir("releases");
    FileUtil.makeDir("releases/xyz");
    FileUtil.createFile("releases/xyz/.releaseInfo", line1 + line2);
  }

  public void testMultipleUsers() throws InterruptedException {
    Thread firstThread = createThreadForRunningWidget();
    Thread secondThread = createThreadForRunningWidget();

    firstThread.start();
    secondThread.start();
    Thread.sleep(100);
  }

  private void runWidget() {
    try {
      createReleaseInfo();
      ReleaseWidget widget = new ReleaseWidget(widgetRoot,
        "!release xyz someurl"
      );
      String html = widget.render();
      assertSubString("<a href=\"someurl\">xyz</a>", html);
    }
    catch (Exception e) {
      fail();
    }
  }

  private void makeSampleRelease() {
    FileUtil.makeDir("releases");
    FileUtil.makeDir("releases/xyz");
    FileUtil.createFile("releases/xyz/package1", "package one");
    FileUtil.createFile("releases/xyz/package2", "package two");
  }

  private Thread createThreadForRunningWidget() {
    Thread user = new Thread(
      new Runnable() {
        public void run() {
          try {
            runWidget();
          }
          catch (Exception e) {
            fail();
          }
        }
      }
    );
    return user;
  }

  protected String getRegexp() {
    return ReleaseWidget.REGEXP;
  }
}
