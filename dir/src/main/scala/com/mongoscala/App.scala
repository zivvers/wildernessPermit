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
    
    creeper.testBuyerScrape()
    /*
    println("Website is: " + websiteURL);
    
    creeper.testUpload();

    Thread.sleep(5000);

    creeper.printFirst();

    println("..ok done");
    */
     //
     // var albumDeetMap = creeper.getAlbumDeets()
     //
    /* */
     //println( albumDeetMap )
     /*
     creeper.expandBuyers();

     var buyerURLs : List[String] = creeper.getBuyerURLs()

     println( s"buyer URL list size: ${ buyerURLs.size }" )
     */
    


/*     var driver = new RemoteWebDriver( url , DesiredCapabilities.firefox());

     driver.get("https://sandy.bandcamp.com/album/race")

     val waitForLoad = new WebDriverWait(driver, 12)

     waitForLoad.until(
        ExpectedConditions.elementToBeClickable( By.className("playbutton") )
     )


     println("TIME FOR STUFF")

     while ( driver.findElements(By.cssSelector("a.more-thumbs")).size() > 0 )

     {
     	println("loopin")

     	driver.findElement(By.cssSelector("a.more-thumbs")).click()



     }


     //val band1 : Band = Band("123", "garbanzo", "http://bandcamp.garbanzo", "philly")

     //println( band1.location)

*/
  }

};





