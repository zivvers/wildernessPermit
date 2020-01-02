package seleniumTool

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.remote.RemoteWebDriver;

import scala.collection.JavaConverters._
import scala.util.control.Breaks._ //need for scala break statement

import java.net.URL;

class BandCampCrawler(url : String)  {

   val remoteURL : URL = new URL( "http://remote-webdriver:4444/wd/hub");
   // val bandName = 
   var driver : RemoteWebDriver = new RemoteWebDriver( remoteURL , DesiredCapabilities.firefox() );

   println("IN AUX CONSTRUCTOR")
   driver.get(url);
   println("connection made...")

   val waitForLoad = new WebDriverWait(driver, 12);

   waitForLoad.until(
      ExpectedConditions.elementToBeClickable( By.className("playbutton") )
   )

   println("page loaded...")
   
   /*def this( url : String, driver : RemoteWebDriver)
   {


	   this( url )
	   this.driver = driver;
     println("IN AUX CONSTRUCTOR")
     driver.get(url);
   	 println("connection made...")

     val waitForLoad = new WebDriverWait(driver, 12);

     waitForLoad.until(
        ExpectedConditions.elementToBeClickable( By.className("playbutton") )
     )

     println("page loaded...")
   } */


   // this is a method to expand the buyers list of an album (by clicking "more...")
   def expandBuyers() 
   {

    var numBuyers : Int = driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList.size

    driver.findElement(By.cssSelector("a.more-thumbs")).click()

    //driver.findElement(By.cssSelector("a.more-thumbs"))

    breakable {
      while( true )
      {
        if ( driver.findElements(By.cssSelector("a.fan.pic")).asScala.toList.size > numBuyers )
        {

          println( s"number of buyers: ${ driver.findElements( By.cssSelector("a.fan.pic") ).asScala.toList.size }" );
          break;
        
        }

      }
    }


   }

   def printBuyers()
   {

    var thumbBuyers : List[WebElement] = driver.findElements(By.cssSelector("a.more-thumbs")).asScala.toList
    //var numBuyers = thumbBuyers.size;
    println(s"number of visible buyers: ${ thumbBuyers.size }")


   }


  /*
  def main(args: Array[String]) {

     var url:URL = new URL( "http://remote-webdriver:4444/wd/hub");

     var driver = new RemoteWebDriver( url , DesiredCapabilities.firefox());

     driver.get("https://sandy.bandcamp.com/album/race")

     val waitForLoad = new WebDriverWait(driver, 12)

     waitForLoad.until(
        ExpectedConditions.elementToBeClickable( By.className("playbutton") )
     )


     println("TIME FOR STUFF")

     while (true)

     {
     	println("loopin")

     	driver.findElement(By.cssSelector("a.more-thumbs"))

     }


     //val band1 : Band = Band("123", "garbanzo", "http://bandcamp.garbanzo", "philly")

     //println( band1.location)


  }
} */
}




