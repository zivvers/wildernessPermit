package com.mongoscala


//import org.mongodb.scala._ ;

/* document type */

import seleniumTool._;

import java.time._


object App {

  def main(args: Array[String]) {

    var websiteURL : String = "";
    //var creeper : BandCampCrawler = _;
    if (args.length == 0) {
        //throw new IllegalArgumentException("need argument for website ;-)");

        websiteURL = "https://sandy.bandcamp.com/album/race";
        

        //creeper.expandBuyers()
    }
    else {

        websiteURL = args(0);
        
        //creeper.waitGetScreenshot();
    }

    var creeper : BandCampCrawler = new BandCampCrawler( websiteURL );
    
    var testURL : String = "https://bandcamp.com/sometimesgreg";

    var purchaseURLs : List[String] = creeper.scrapeBuyerPurchases( testURL )
    println("size of buyers curr process: " + purchaseURLs)
    creeper.testPull()
    /*
    println("Website is: " + websiteURL);
    
    creeper.testUpload();

    Thread.sleep(5000);

    creeper.printFirst();

    println("..ok done");
    */
  }

};





