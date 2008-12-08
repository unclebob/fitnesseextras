package com.objectmentor.fitnesse;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.components.RecentChanges;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.NotFoundResponder;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;

public class AddBlogCommentResponder implements Responder {
  private final String nl = "\n";
  public static final String Marker =
    "#----- Blog Comment Marker (Please don't delete me) -----#";

  private boolean reverseOrder = false;
  private String tail = "";
  private PageData data;
  private String user;

  public Response makeResponse(FitNesseContext context, Request request)
    throws Exception {
    final String pageName = request.getResource();
    String sourcePage = request.getInput("sourcePage").toString();
    reverseOrder = request.hasInput("reverse");
    user = request.getAuthorizationUsername();

    PageCrawler crawler = context.root.getPageCrawler();
    WikiPage page = crawler.getPage(context.root, PathParser.parse(pageName));
    if (page != null) {
      data = page.getData();
      appendRequestedCommentToPage(page, request);
      RecentChanges.updateRecentChanges(data);
      SimpleResponse response = new SimpleResponse();
      response.redirect(sourcePage);
      return response;
    } else {
      return new NotFoundResponder().makeResponse(context, request);
    }
  }

  private void appendRequestedCommentToPage(WikiPage page, Request request)
    throws Exception {
    String content = data.getContent();

    StringBuffer buffer = new StringBuffer();
    if (reverseOrder)
      addCommentInReverseOrder(content, buffer);
    else
      buffer.append(content);

    addComment(buffer, request);
    buffer.append(tail);

    data.setContent(buffer.toString());
    if (user != null)
      data.setAttribute(WikiPage.LAST_MODIFYING_USER, user);
    else
      data.removeAttribute(WikiPage.LAST_MODIFYING_USER);
    page.commit(data);
  }

  private String addCommentInReverseOrder(String content, StringBuffer buffer) {
    int markerIndex = content.indexOf(Marker);
    if (markerIndex > 0) {
      int markerEndIndex = markerIndex + Marker.length();
      buffer.append(content.substring(0, markerEndIndex));
      tail = content.substring(markerEndIndex);
    } else {
      buffer.append(content);
      addMarker(buffer);
    }
    return tail;
  }

  private void addMarker(StringBuffer buffer) {
    buffer.append(nl);
    buffer.append(Marker);
  }

  private void addComment(StringBuffer buffer, Request request) {
    buffer.append(nl);
    buffer.append("!* ");
    buffer.append(request.getInput("date")).append(", ");
    buffer.append(request.getInput("bloggerName")).append(", ");
    buffer.append(request.getInput("subject")).append(nl);
    buffer.append(request.getInput("comment")).append(nl);
    buffer.append("*!");
  }
}
