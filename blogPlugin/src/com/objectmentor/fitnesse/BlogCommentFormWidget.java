package com.objectmentor.fitnesse;

import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;
import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogCommentFormWidget extends WikiWidget {
  public static final String REGEXP = "^!commentForm[^\r\n]*";
  private static final Pattern pattern = Pattern.compile(
    "^!commentForm( -r)?( .+)?"
  );

  public String targetPageName;
  public String sourcePageName;
  public boolean reverseOn;

  public BlogCommentFormWidget(ParentWidget parent, String text)
    throws Exception {
    super(parent);
    sourcePageName = makeSourcePageName();
    Matcher match = pattern.matcher(text);
    if (match.find()) {
      reverseOn = match.group(1) != null;
      String pageName = match.group(2);
      if (pageName != null && pageName.length() > 0)
        targetPageName = pageName.trim();
      else
        targetPageName = sourcePageName;
    }
  }

  public String render() throws Exception {
    if (!targetPageExists())
      return "BlogCommentFormWidget: Oops!  The page " + targetPageName +
        " doesn't seem to exist.";
    else
      return makeBlogForm(targetPageName).html();
  }

  private boolean targetPageExists() throws Exception {
    WikiPage wikiPage = parent.getWikiPage();
    PageCrawler crawler = wikiPage.getPageCrawler();
    WikiPage root = crawler.getRoot(wikiPage);
    return crawler.pageExists(root, PathParser.parse(targetPageName));
  }

  private String makeSourcePageName() throws Exception {
    WikiPage wikiPage = parent.getWikiPage();
    PageCrawler crawler = wikiPage.getPageCrawler();
    WikiPagePath path = crawler.getFullPath(wikiPage);
    String pageName = PathParser.render(path);
    return pageName;
  }

  private HtmlTag makeBlogForm(String pageName) {
    HtmlTag form = new HtmlTag("form");
    form.addAttribute("method", "post");
    form.addAttribute("action", pageName);
    form.add(HtmlUtil.makeInputTag("hidden", "responder", "addBlogComment"));
    form.add(HtmlUtil.makeInputTag("hidden", "date", makeDateString()));
    form.add(HtmlUtil.makeInputTag("hidden", "sourcePage", sourcePageName));
    if (reverseOn)
      form.add(HtmlUtil.makeInputTag("hidden", "reverse", "1"));
    form.add("Your Name:");
    form.add(HtmlUtil.makeInputTag("text", "bloggerName", ""));
    form.add("Subject:");
    form.add(makeSubjectField());
    form.add(HtmlUtil.BR);
    form.add(makeCommentField());
    form.add(HtmlUtil.BR);
    form.add(HtmlUtil.makeInputTag("submit", "blog", "Append Comment"));
    return form;
  }

  private String makeDateString() {
    Date today = new Date();
    SimpleDateFormat f = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    String dateString = f.format(today);
    return dateString;
  }

  private HtmlTag makeSubjectField() {
    HtmlTag subjectField = HtmlUtil.makeInputTag("text", "subject", "");
    subjectField.addAttribute("size", "80");
    return subjectField;
  }

  private HtmlTag makeCommentField() {
    HtmlTag commentField = new HtmlTag("textarea");
    commentField.addAttribute("name", "comment");
    commentField.addAttribute("cols", "80");
    commentField.addAttribute("rows", "6");
    commentField.add("");
    return commentField;
  }
}
