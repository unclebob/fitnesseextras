package com.objectmentor.fitnesse.releases;

import fitnesse.FitNesseContext;
import fitnesse.responders.BasicResponder;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlUtil;
import fitnesse.http.Request;
import fitnesse.http.Response;

public class DisabledResponder extends BasicResponder {
  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    String content = prepareResponseDocument(context).html();
    return responseWith(content);
  }

  private HtmlPage prepareResponseDocument(FitNesseContext context) {
    HtmlPage responseDocument = context.htmlPageFactory.newPage();
    HtmlUtil.addTitles(responseDocument, "Default Responder");
    responseDocument.main.use(content());
    return responseDocument;
  }

  private String content() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("We have disabled this function of fitnesse.org<br/>");
    buffer.append("because of link farmers, robots, and other denizens of the net.<br/>");
    buffer.append("You can access this feature by downloading FitNesse and running.<br/>");
    buffer.append("it on your own machine.  Thanks,<br/>");
    buffer.append("<ul><li><a href=\"mailto:unclebob@objectmentor.com\">The FitNesse development team.</a></ul>");
    return buffer.toString();
  }

}
