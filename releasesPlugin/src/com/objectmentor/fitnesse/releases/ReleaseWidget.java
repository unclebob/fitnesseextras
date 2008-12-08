package com.objectmentor.fitnesse.releases;

import fitnesse.html.*;
import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseWidget extends WikiWidget {
  public static final String REGEXP = "^!release [^\r\n]*";
  private static final Pattern pattern = Pattern.compile(
    "^!release (-r\\s)?(\\S*)\\s?(\\S+)?"
  );
  private String releaseName;
  private Release release;
  public boolean reverseOrder;
  public String href;

  public ReleaseWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = pattern.matcher(text);
    if (match.find()) {
      reverseOrder = match.group(1) != null;
      releaseName = match.group(2);
      href = match.group(3);
      release = new Release(releaseName);
    }
  }

  public String render() throws Exception {
    TagGroup html = new TagGroup();
    addTitle(html);
    if (release.exists()) {
      addFilesHtml(html);
      release.saveInfo();
    } else
      html.add("The release directory could not be found.");
    return html.html();
  }

  private void addTitle(TagGroup html) throws Exception {
    if (release.isCorrupted()) {
      html.add(new HtmlTag("h3", "release " + releaseName + " is Corrupted"));
    } else {
      HtmlTag title = new HtmlTag("h3", "release ");
      if (href != null)
        title.add(HtmlUtil.makeLink(href, releaseName));
      else
        title.add(releaseName);
      html.add(title);
    }
  }

  private void addFilesHtml(TagGroup html) throws Exception {
    if (release.fileCount() == 0)
      html.add("There are no files in this release.");
    else {
      HtmlTableListingBuilder list = new HtmlTableListingBuilder();
      addTitleRow(list);
      List files = release.getFiles();
      if (reverseOrder)
        Collections.reverse(files);
      for (Iterator iterator = files.iterator(); iterator.hasNext();) {
        ReleaseFile releaseFile = (ReleaseFile) iterator.next();
        addFileRow(releaseFile, list);
      }
      html.add(list.getTable());
    }
  }

  private void addFileRow(ReleaseFile file, HtmlTableListingBuilder list)
    throws Exception {
    HtmlElement[] rowElements = new HtmlElement[3];
    String href = "/" + file.getFilename() +
      "?responder=releaseDownload&release=" + releaseName;
    HtmlElement link = HtmlUtil.makeLink(href, new HtmlTag("b",
      file.getFilename()
    )
    );
    rowElements[0] = link;
    rowElements[1] = new RawHtml(file.getSize());
    rowElements[2] = new RawHtml(file.getDownloads());
    list.addRow(rowElements);
  }

  private void addTitleRow(HtmlTableListingBuilder list) throws Exception {
    HtmlElement[] rowElements = new HtmlElement[3];
    rowElements[0] = HtmlUtil.makeSpanTag("caps", "File");
    rowElements[1] = HtmlUtil.makeSpanTag("caps", "Size");
    rowElements[2] = HtmlUtil.makeSpanTag("caps", "# of Downloads");
    list.addRow(rowElements);
  }

  public String getReleaseName() {
    return releaseName;
  }
}
