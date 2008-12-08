package fitnesse.wikitext.widgets;

import fitnesse.wiki.PageData;
import fitnesse.wiki.WikiPage;
import fitnesse.wikitext.WikiWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssLinkWidget extends WikiWidget {
  public static final String REGEXP =
    "^!rsslink [^\r\n]*" + LineBreakWidget.REGEXP + "?";
  private static final Pattern pattern = Pattern.compile("^!rsslink (.*)");
  private String rssLink;

  public RssLinkWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = pattern.matcher(text);
    match.find();
    rssLink = match.group(1);
    saveLink();
  }

  private void saveLink()
    throws Exception {
    WikiPage page = getWikiPage();
    PageData data = page.getData();
    data.setAttribute("rsslink", rssLink);
    page.commit(data);
  }

  public String render() throws Exception {
    return "";
  }

  public String getLink() {
    return rssLink;
  }
}
