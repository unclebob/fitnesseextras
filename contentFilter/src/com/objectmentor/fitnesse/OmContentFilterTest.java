package com.objectmentor.fitnesse;

import junit.framework.TestCase;

public class OmContentFilterTest extends TestCase {
  private OmContentFilter filter;

  public void setUp() throws Exception {
    filter = new OmContentFilter();
    filter.content = sampleSpam;
  }

  public void tearDown() throws Exception {
  }

  public void testPornWords() throws Exception {
    String content =
      "gay lesbian sex porn cock-dick/pussy the rest don't count: grass nineteen";
    assertEquals(7, filter.countPorn(content));
    assertEquals(183, filter.countPorn(sampleSpam));
  }

  public void testProfanity() throws Exception {
    String content = "fuck shit damn bitch cunt ass hell";
    assertEquals(7, filter.countProfanity(content));
    assertEquals(2, filter.countProfanity(sampleSpam));
  }

  public void testCountWords() throws Exception {
    String content = "Like the long-legged fly on a stream,";
    assertEquals(8, filter.countWords(content));
    assertEquals(788, filter.countWords(sampleSpam));
  }

  public void testDrugWords() throws Exception {
    String content = "pharmacy cialis viagra and such";
    assertEquals(3, filter.countDrugs(content));
    assertEquals(0, filter.countDrugs(sampleSpam));
  }

  public void testSpammerLink() throws Exception {
    String content = "[[http://blah/blah.html http://www.google.com";
    assertEquals(1, filter.countSpamLinks(content));
    assertEquals(60, filter.countSpamLinks(sampleSpam));
  }

  public void testGatherStats() throws Exception {
    filter.gatherStatsFor(sampleSpam);
    assertEquals(788, filter.wordCount);
    assertEquals(60, filter.spamLinkCount);
    assertEquals(183, filter.pornCount);
    assertEquals(2, filter.profanityCount);
    assertEquals(0, filter.drugCount);
    assertEquals(245, filter.offenseCount);

    assertEquals(0.076, filter.spamLinkRatio, 0.001);
    assertEquals(0.232, filter.pornRatio, 0.001);
    assertEquals(0.002, filter.profanityRatio, 0.001);
    assertEquals(0.000, filter.drugRatio, 0.001);
    assertEquals(0.310, filter.offenseRatio, 0.001);
  }

  String sampleSpam =
    "At first their was resistance but then her thighs slowly opened Lesbian sex [[http://onlinepix.dnip.net/lesbian-girls.html Free lesbian]]" +
      "Shemale cocktail [[http://gella.dyndsl.com/tranny.html Free shemale pictures]]" +
      "Lesbian sisters [[http://onlinepix.dnip.net/lesbian-pictures.html Free lesbian picture galleries]]" +
      "Free twinks [[http://karlos.au2000.com/gay-photography.html Free gay stories]]" +
      "Lesbian incest [[http://klanus.slife.com/incest-photos.html Free erotic incest stories]]" +
      "Extreme bdsm [[http://garson.2222.net/lesbian-bdsm.html Bdsm stories]]" +
      "Gay pics [[http://karlos.au2000.com/free-gay-porn.html Free gay]]" +
      "Shemale cartoons [[http://gella.dyndsl.com/shemales.html Shemale videos]]" +
      "Incest quest [[http://klanus.slife.com/free-incest-pictures.html Incest forum]]" +
      "Free shemale thumbnail pics [[http://gella.dyndsl.com/shemale-yum.html Toronto shemale]]" +
      "Bdsm galleries [[http://garson.2222.net/free-bdsm.html Bdsm]]" +
      "Free bdsm pictures [[http://garson.2222.net/bdsm-galleries.html Bdsm fucking]]" +
      "Shemale movies [[http://gella.dyndsl.com/absolute-shemale.html Shemale yum]]" +
      "Free bdsm pics [[http://garson.2222.net/bondage-stories.html Bdsm equipment]]" +
      "Gay gallery [[http://karlos.au2000.com/gay-stories.html Gay stories]]" +
      "Free lesbian mpegs [[http://onlinepix.dnip.net/free-lesbian-photos.html Lesbian movies]]" +
      "Free lesbian pics [[http://onlinepix.dnip.net/free-lesbian-movies.html Lesbian kissing]]" +
      "Free incest porn [[http://klanus.slife.com/free-incest-sex-stories.html Russian incest]]" +
      "Bdsm story [[http://garson.2222.net/free-bdsm-galleries.html Bdsm torture galleries]]" +
      "Free lesbian videos [[http://onlinepix.dnip.net/free-lesbian-movie-clips.html Lesbian thumbnails]]" +
      "More curious than anything I allowed my hand to slide down a long line of vertebrae till it encountered the swell of a warm little butt Free shemale [[http://gella.dyndsl.com/shemale-stories.html Shemale free]]" +
      "Incest forum [[http://klanus.slife.com/incest-sex-stories.html Family incest sex]]" +
      "Free gay stories [[http://karlos.au2000.com/gay-pics.html Gay male stories]]" +
      "Sado [[http://garson.2222.net/free-bdsm-pictures.html Bdsm]]" +
      "Free lesbian pictures [[http://onlinepix.dnip.net/lesbian-photos.html Free lesbian sex stories]]" +
      "Implants shemale slave [[http://gella.dyndsl.com/shemale-gallery.html Shemale pictures]]" +
      "Sado [[http://garson.2222.net/bdsm.html Free bdsm]]" +
      "Lesbian girls [[http://onlinepix.dnip.net/lesbian-galleries.html Lesbian pictures]]" +
      "Gay men in thongs [[http://karlos.au2000.com/gay-gallery.html Muscle hunks]]" +
      "Russian incest [[http://klanus.slife.com/russian-incest.html Family incest]]" +
      "Free incest movies [[http://klanus.slife.com/erotic-incest-stories.html Incest movies]]" +
      "Gay cartoons [[http://karlos.au2000.com/free-twinks.html Free gay]]" +
      "Incest photos [[http://klanus.slife.com/incest-photos.html Young incest]]" +
      "Free lesbian [[http://onlinepix.dnip.net/free-lesbian-mpegs.html Lesbian pics]]" +
      "Gay porn [[http://karlos.au2000.com/gay-hunks.html Muscle hunks]]" +
      "Incest porn [[http://klanus.slife.com/incest-quest.html Incest grrl]]" +
      "Lesbian bdsm [[http://garson.2222.net/free-bdsm-stories.html Bdsm comics]]" +
      "Incest forum [[http://klanus.slife.com/incest-story.html Mom son incest]]" +
      "Gay male stories [[http://karlos.au2000.com/free-gay-stories.html Gay photography]]" +
      "Gay porn [[http://karlos.au2000.com/free-gay-movies.html Gay boys]]" +
      "I let my skirt fall back into place and unbuttoned his shirt Free shemale pics [[http://gella.dyndsl.com/shemale-cartoons.html Shemale pics]]" +
      "Shemale movies [[http://gella.dyndsl.com/shemale-stories.html Free shemale]]" +
      "Mom son incest [[http://klanus.slife.com/young-incest.html Incest erotic stories]]" +
      "Lesbian bdsm [[http://garson.2222.net/sado.html Sado maso]]" +
      "Gay bdsm [[http://garson.2222.net/bdsm-erotica-stories-free.html Bdsm tgp]]" +
      "Bdsm equipment [[http://garson.2222.net/free-bdsm-pics.html Bdsm comics]]" +
      "Bdsm pictures [[http://garson.2222.net/bdsm-fucking.html Stories bdsm]]" +
      "Bdsm equipment [[http://garson.2222.net/bdsm-library.html Bdsm movies]]" +
      "Mother son incest [[http://klanus.slife.com/free-incest-porn.html Family incest]]" +
      "Black studs [[http://karlos.au2000.com/free-gay-galleries.html Gay hunks]]" +
      "Shemale galleries [[http://gella.dyndsl.com/shemale.html Shemale movies]]" +
      "Free lesbian videos [[http://onlinepix.dnip.net/free-lesbian-videos.html Lesbian pics]]" +
      "Free lesbian picture galleries [[http://onlinepix.dnip.net/lesbian-kissing.html Lesbian videos]]" +
      "Bdsm free [[http://garson.2222.net/stories-bdsm.html Bdsm video]]" +
      "Gay sims [[http://karlos.au2000.com/gay-cartoons.html Gay hunks]]" +
      "Free gay galleries [[http://karlos.au2000.com/gay-sims.html Gay teens]]" +
      "Bondage [[http://garson.2222.net/bdsm-furniture.html Free bdsm stories]]" +
      "Free lesbian galleries [[http://onlinepix.dnip.net/free-lesbian.html Lesbian movie scenes]]" +
      "Lesbian teens [[http://onlinepix.dnip.net/free-lesbian-galleries.html Lesbian stories]]" +
      "Gay cops gallery [[http://karlos.au2000.com/gay-gallery.html Black studs]]" +
      "She said, Turning into my slave girl took some of my precious time, but as you learn your duties, it may turn out to be worth it";


}