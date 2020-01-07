package com.mongoscala


//import org.mongodb.scala._ ;

/* document type */
import doctype._
import seleniumTool._;


object App {

  def main(args: Array[String]) {


     var url = "https://sandy.bandcamp.com/album/race";

     var creeper : BandCampCrawler = new BandCampCrawler( url );

     creeper.printBuyers();

     var albumDeetMap = creeper.getAlbumDeets()

     println( albumDeetMap )
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

}





