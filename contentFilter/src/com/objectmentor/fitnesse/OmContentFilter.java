package com.objectmentor.fitnesse;

import fitnesse.responders.editing.ContentFilter;
import fitnesse.wikitext.widgets.LinkWidget;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OmContentFilter implements ContentFilter {
  private static String pornList =
    "gay,sex,lesbian,pics,cock,pussy,erotic,ass,teen,suck,lick,incest,porn" +
      ",free,fuck,anal,cunt,cum,tits,dick,boob,breast,naked,sexual,meretricious";
  private static String profanityList =
    "fuck,shit,damn,bitch,cunt,ass,hell,asshole,fucking,shithead,dumbass,";
  private static String drugList =
    "pharmacy,drug,drugs,viagra,cialis,levitra,phentermine,soma,valium" +
      ",vicodin,xanax,medication,penis,enlargement,generic,prescription,lipitor,paxil,vioxx,meridia,prozac,zoloft";
  private static String miscList =
    "cruise,ebay,finance,mortgage,loan,cash,money,airline,flight,airlines" +
      "reservation,order,travel,sperm,zoloft,investment,poker,blackjack,onlinepoker,insurance,cheap,discount";

  private static Pattern wordPattern;
  private static Pattern spamLinkPattern;
  private static Pattern pornPattern;
  private static Pattern profanityPattern;
  private static Pattern drugPattern;
  private static Pattern miscPattern;

  static {
    wordPattern = Pattern.compile("\\b\\w");
    spamLinkPattern = Pattern.compile("\\[\\[" + LinkWidget.REGEXP + " ");
    pornPattern = makePatternFromWordList(pornList);
    profanityPattern = makePatternFromWordList(profanityList);
    drugPattern = makePatternFromWordList(drugList);
    miscPattern = makePatternFromWordList(miscList);
  }

  public String content;
  public int spamLinkCount;
  public int pornCount;
  public int wordCount;
  public int profanityCount;
  public int drugCount;
  public int miscCount;
  public int offenseCount;

  public double spamLinkRatio;
  public double pornRatio;
  public double profanityRatio;
  public double drugRatio;
  public double miscRatio;
  public double offenseRatio;

  public OmContentFilter(Properties properties) {
  }

  public OmContentFilter() {
  }

  public boolean isContentAcceptable(String content, String pageName) {
    gatherStatsFor(content);

    if (profanityRatio > 0.1)
      return false;

    if (offenseRatio > 0.1)
      return false;

    if (offenseRatio > 0.04 && offenseCount > 10)
      return false;

    if (offenseRatio > 0.06 && offenseCount > 5)
      return false;

    if (offenseCount > 20)
      return false;

    if (spamLinkCount > 1)
      return false;

    return true;
  }

  public void gatherStatsFor(String content) {
    wordCount = countWords(content);
    spamLinkCount = countSpamLinks(content);
    pornCount = countPorn(content);
    profanityCount = countProfanity(content);
    drugCount = countDrugs(content);
    miscCount = countMisc(content);
    offenseCount =
      pornCount + profanityCount + drugCount + miscCount + spamLinkCount;

    if (wordCount > 0) {
      spamLinkRatio = (double) spamLinkCount / (double) wordCount;
      pornRatio = (double) pornCount / (double) wordCount;
      profanityRatio = (double) profanityCount / (double) wordCount;
      drugRatio = (double) drugCount / (double) wordCount;
      miscRatio = (double) miscCount / (double) wordCount;
      offenseRatio =
        pornRatio + profanityRatio + drugRatio + miscRatio + spamLinkRatio;
    }
  }

  public int countSpamLinks(String content) {
    return countPatternMatches(spamLinkPattern, content);
  }

  public int countPorn(String content) {
    return countPatternMatches(pornPattern, content.toLowerCase());
  }

  public int countProfanity(String content) {
    return countPatternMatches(profanityPattern, content.toLowerCase());
  }

  public int countWords(String content) {
    return countPatternMatches(wordPattern, content);
  }

  public int countDrugs(String content) {
    return countPatternMatches(drugPattern, content.toLowerCase());
  }

  public int countMisc(String content) {
    return countPatternMatches(miscPattern, content.toLowerCase());
  }

  private int countPatternMatches(Pattern pattern, String content) {
    int count = 0;
    Matcher match = pattern.matcher(content);

    while (match.find())
      count++;

    return count;
  }

  private static Pattern makePatternFromWordList(String wordList) {
    StringBuffer buffer = new StringBuffer();
    String[] words = wordList.split(",");
    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      buffer.append("\\b").append(word).append("\\b");
      if (i < words.length - 1)
        buffer.append("|");
    }
    return Pattern.compile(buffer.toString());
  }
}
