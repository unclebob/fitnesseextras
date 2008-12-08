package fitnesse.responders.Blog;

import fitnesse.FitNesseContext;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.responders.RssResponder;
import fitnesse.util.XmlUtil;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RssBlogResponder extends RssResponder {
  private FitNesseContext context = null;

  public Response makeResponse(FitNesseContext context, Request request)
    throws Exception {
    this.context = context;
    return super.makeResponse(context, request);
  }

  protected void buildItemReport(
    WikiPage page, Document rssDocument, String resource
  ) throws Exception {
    if (page != null) {
      String lines[] = convertPageToArrayOfLines(page);
      for (int i = 0; i < lines.length; i++) {
        String fields[] = lines[i].split("\\|");
        String path = fields[1];
        String author = fields[2];
        String pubDate = fields[3];
        String title = path;
        String description = "";


        WikiPage linkPage = context.root.getPageCrawler().getPage(context.root,
          PathParser.parse(path)
        );
        if (linkPage != null) {
          PageData linkData = linkPage.getData();
          if (linkData.getAttribute("rsslink") != null)
            path = linkData.getAttribute("rsslink");
          if (linkData.getAttribute("title") != null) {
            title = linkData.getAttribute("title");
          }
          description = "<![CDATA[" + linkData.getContent() + "]]>";
        }

        if (shouldReportItem(resource, path))
          buildItem(rssDocument, title, author, pubDate, path, description);
      }
    }
  }

  private void buildItem(
    Document rssDocument, String title, String author, String pubDate,
    String link, String description
  )
    throws Exception {
    Element itemElement1 = rssDocument.createElement("item");
    makeNodes(rssDocument, itemElement1, title, author, pubDate);
    buildLink(rssDocument, itemElement1, link);

    XmlUtil.addTextNode(rssDocument, itemElement1, "description", description);
    Element itemElement = itemElement1;
    channelElement.appendChild(itemElement);
  }
}
