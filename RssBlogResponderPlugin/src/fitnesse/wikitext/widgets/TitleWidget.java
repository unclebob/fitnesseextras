package fitnesse.wikitext.widgets;

import fitnesse.wiki.PageData;
import fitnesse.wiki.WikiPage;
import fitnesse.wikitext.WikiWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleWidget extends WikiWidget {
  public static final String REGEXP =
    "^!title [^\r\n]*" + LineBreakWidget.REGEXP + "?";
  private static final Pattern pattern = Pattern.compile("^!title (.*)");
  private String theTitle;
  public static final String FORMAT_START = "<h3><div class=\"centered\">";
  public static final String FORMAT_END = "</div></h3>";

  public TitleWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = pattern.matcher(text);
    match.find();
    theTitle = match.group(1);

    saveTitle();
  }

  private void saveTitle()
    throws Exception {
    WikiPage page = getWikiPage();
    PageData data = page.getData();
    data.setAttribute("title", theTitle);
    page.commit(data);
  }

  public String getTitle() {
    return theTitle;
  }

  public String render() throws Exception {
    StringBuffer html = new StringBuffer(FORMAT_START);
    html.append(theTitle);
    html.append(FORMAT_END);
    return html.toString();
  }
}
