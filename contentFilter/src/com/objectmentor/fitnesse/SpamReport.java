package com.objectmentor.fitnesse;

import fitnesse.components.FitNesseTraversalListener;
import fitnesse.wiki.FileSystemPage;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.WikiPage;

public class SpamReport implements FitNesseTraversalListener {
  private PageCrawler crawler;

  public static void main(String[] args) throws Exception {
    SpamReport report = new SpamReport();
    report.run();
  }

  private void run() throws Exception {
    System.out.println(
      "Page Name\tlink count\tporn count\tprofanity count\tdrug count\tmisc count\ttotal offense count" +
        "\tlink ratio\tporn ratio\tprofanity ratio\tdrug ratio\tmisc ratio\ttotal offense ratio"
    );

    WikiPage root = FileSystemPage.makeRoot(".", "FitNesseRoot");
    crawler = root.getPageCrawler();
    crawler.traverse(root, this);
  }

  public void processPage(WikiPage page) throws Exception {
    OmContentFilter filter = new OmContentFilter();
    filter.gatherStatsFor(page.getData().getContent());
    StringBuffer buffer = new StringBuffer();
    buffer.append(crawler.getFullPath(page)).append("\t");
    buffer.append(filter.spamLinkCount).append("\t");
    buffer.append(filter.pornCount).append("\t");
    buffer.append(filter.profanityCount).append("\t");
    buffer.append(filter.drugCount).append("\t");
    buffer.append(filter.miscCount).append("\t");
    buffer.append(filter.offenseCount).append("\t");
    buffer.append(filter.spamLinkRatio).append("\t");
    buffer.append(filter.pornRatio).append("\t");
    buffer.append(filter.profanityRatio).append("\t");
    buffer.append(filter.drugRatio).append("\t");
    buffer.append(filter.miscRatio).append("\t");
    buffer.append(filter.offenseRatio).append("\t");
    System.out.println(buffer.toString());
  }

  public String getSearchPattern() throws Exception {
    return ".";
  }

}
