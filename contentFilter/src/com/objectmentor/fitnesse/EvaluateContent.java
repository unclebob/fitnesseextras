package com.objectmentor.fitnesse;

public class EvaluateContent {
  static String anotherSample =
    "Chrissy called for me to do some cannonballs but I begged off Discounts Thirfty Car Rental [[http://clapos.sexushost.com/discounts/thirfty-car-rental.html Discounts Thirfty Car Letting]]\n" +
      "Chianti Guides Go [[http://clapos.erosfreehost.com/chianti/guides-tour.html Chianti Guides Go]]\n" +
      "Profina Debt Consolidation Company [[http://fatalhome.myfreewebs.net/profina-debt-consolidation-company.html Profina Debt Consolidation Company]]\n" +
      "Credit Bill Debt Reduction Calculator [[http://fatalhome.myfreewebs.net/credit/card-debt-reduction-calculator.html Credit Card Debt Reduction Reckoner]]\n" +
      "32 Scale Vw Golf Slot Cars [[http://clapos.sexushost.com/132/scale-vw-golf-slot-cars.html\n" +
      "32 Scale Vw Golf Slot Cars]]\n" +
      "Massachusetts State Lottery Daily Turn Thursday 11305 [[http://fatalhome.webyar.com/massachusetts-state-lottery-daily-number-thursday-11305.html Massachusetts State Lottery Daily Number Thursday 11305]]\n" +
      "Secure Recognition Card Processing [[http://umapers.xadulthosting.com/secure/credit-card-processing.html Procure Credit Menu Processing]]\n" +
      "Alaska Express Job Bank [[http://umapers.free-websites.com/alaska-state-job-bank.html Alaska Posit Occupation Bank]]\n" +
      "Exoctic Car Rental Denver [[http://clapos.free-websites.com/exoctic/car-rental-denver.html Exoctic Car Lease Denver]]\n" +
      "200 Calorie Diabetic Diet [[http://umapers.webyar.com/1200-calorie-diabetic-diet.html\n" +
      "200 Calorie Diabetic Diet]]\n" +
      "Pharmacist Jobs Nyc [[http://umapers.free-websites.com/pharmacist-jobs-nyc.html Pharmacist Jobs Nyc]]\n" +
      "Coconut Embrocate Diet [[http://umapers.webyar.com/coconut-oil-diet.html Coconut Anele Diet]]\n" +
      "Caribean Cruise [[http://clapos.sexushost.com/caribean-cruise.html Caribean Cruise]]\n" +
      "Bribe Diet Pills That Hold Ephedra [[http://umapers.webyar.com/buy-diet-pills-that-contain-ephedra.html Buy Diet Pills That Hold Ephedra]]\n" +
      "Alternative Book Medicine Seller [[http://umapers.erosfreehost.com/alternative-book-medicine-seller.html Alternative Hold Medicine Seller]]\n" +
      "Solutions For Theater Repossessions From Debt Angels [[http://fatalhome.myfreewebs.net/solutions/for-house-repossessions-from-debt-angels.html Solutions For House Repossessions From Debt Angels]]\n" +
      "Credit Card Turn Generator Downloads [[http://clapos.sexushost.com/credit-card-number-generator-downloads.html Credit Card Number Generator Downloads]]\n" +
      "Hotels Motels Gift Income We Buy [[http://fatalhome.myopenweb.com/hotels-motels-invest-income-we-buy.html Hotels Motels Vest Income We Buy]]\n" +
      "Initiative Meter Purchaser Mortgage [[http://fatalhome.myopenweb.com/first-time-buyer-mortgage.html Kickoff Clock Vendee Mortgage]]\n" +
      "Empire Poker Rakeback [[http://umapers.sexushost.com/empire/poker-rakeback.html Empire Poker Rakeback]]\n" +
      "His brutal oral rape continued until I felt him come in my mouthOpenSpace co-chair with AnnAnderson";

  public static void main(String[] args) {
    OmContentFilter filter = new OmContentFilter();
    filter.gatherStatsFor(anotherSample);

    System.out.println("link count: " + filter.spamLinkCount);
    System.out.println("porn count: " + filter.pornCount);
    System.out.println("profanity count: " + filter.profanityCount);
    System.out.println("drug count: " + filter.drugCount);
    System.out.println("misc count: " + filter.miscCount);
    System.out.println("offense count: " + filter.offenseCount);
    System.out.println("link ratio: " + filter.spamLinkRatio);
    System.out.println("porn ratio: " + filter.pornRatio);
    System.out.println("profanity ratio: " + filter.profanityRatio);
    System.out.println("drug ratio: " + filter.drugRatio);
    System.out.println("misc ratio: " + filter.miscRatio);
    System.out.println("offense ratio: " + filter.offenseRatio);
  }
}
