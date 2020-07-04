package seleniumTool

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import doobie.util.ExecutionContexts
import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._


import scala.collection.JavaConverters._
import scala.util.control.Breaks._ //need for scala break statement
import scala.concurrent.Await
//import scala.concurrent.duration.TimeUnit
import scala.concurrent.duration.Duration

import scala.collection.mutable.ListBuffer

import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.By;
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.Augmenter // experimental class!

import java.net.URL;
// import java.io.File;
import java.io._
import java.util.concurrent.TimeUnit
import java.util.Calendar;
import org.apache.commons.io.FileUtils;


/* for error:  Cannot find an implicit ExecutionContext */
import scala.concurrent.ExecutionContext.Implicits.global

import doctype._;


class RecreationCrawler(baseURL : String = "https://www.recreation.gov/permits/233262/registration/detailed-availability?type=overnight-permit") extends Crawler() {

   /* optional URL will get us to permits for today + 5 days after */
   driver.get( baseURL );
   
   println("navigating to website ... ")
   
   implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val xa = Transactor.fromDriverManager[IO](
          "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", "postgres_usr", "oregano!"
    )
  


   def scrapePermitAvailability(  ) : Unit = {

      /* button for whether commercially guided */

      val guideRadioButtons : List[WebElement] = driver.findElements( By.cssSelector("label.rec-label-radio") ).asScala.toList

      println("# of radios " + guideRadioButtons.size) 
      
      waitForLoad.until(ExpectedConditions.elementToBeClickable( guideRadioButtons(1) ))

      guideRadioButtons(1).click()

      println("clicked!")
	
      // val groupSizeIn : WebElement = driver.findElement( By.cssSelector("input#number-inpute") )

      // groupSizeIn.sendKeys("1", Keys.Enter)

      val guestButtons : List[WebElement] = driver.findElements( By.cssSelector("div.sarsa-number-field button") ).asScala.toList

      println("num sarsa buttons: " + guestButtons.size)

      guestButtons(1).click()

      


      //var availTabl : WebElement = driver.findElement( By.cssSelector("table#availability-table") )
      println("wait table load")

      // val firstRow : WebElement = driver.findElement( By.cssSelector("table#availability-table tbody tr") )

      waitForLoad.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector("table#availability-table tbody tr") ) )

   
      var availTabl : List[WebElement] = driver.findElements( By.cssSelector("table#availability-table") ).asScala.toList

      println("# tables: " + availTabl.size )


      //val rows = availTabl(0).findElement( By.tagName("tbody") ).findElements( By.tagName( "tr") ).asScala.toList
      
      //println("# rows: " + rows.size )
      

      //val tabHTML : String = availTabl(0).getAttribute("innerHTML")
      val tabHTML : String = driver.executeScript( "return arguments[0].outerHTML;", availTabl(0) ).toString() 
      // println( tabHTML )
      //println("parse table")
      parseAvailTable( tabHTML, 0 )
   }

   def parseAvailTable( tableHTML : String, currDateOffset : Int ) : List[List[String]] =
   {
     getScreenshot()
     val doc : Document = Jsoup.parse( tableHTML );

     writeHTML( "table.html" , doc.toString() )

     var rows : Elements = doc.select("tbody").first().select("tr")//.first().getElementsByTag("tr")
      
     val cal : Calendar = Calendar.getInstance()
          
     //c.add(Calendar.DATE, 1);

     if ( currDateOffset == 0 )
     {

        val firstDate : Int  = doc.select("span.date").first().text().toInt
        val dayOfMonth : Int = cal.get(Calendar.DAY_OF_MONTH)
         
        assert( firstDate == dayOfMonth )
     
     }

  
    for (row <- rows.asScala)
    {

      var cells = row.select("td").asScala.toList

      println("ID: " + cells(0).text())
      println("SITE: " + cells(1).text())
      println("Avail: " + cells(6).text())

    }

     val l : List[List[String]] = List( List( "list" ))
     l
   }

   def writeHTML( name : String, html : String ) 
   {
      val pw = new PrintWriter(new File("/usr/src/app/PermitScrape/screenshots/" + name ) )
      pw.write( html )
      pw.close()

   }


   def getScreenshot()
   {

       //Thread.sleep(15000); // sleepy boi 
       val filed: File = driver.getScreenshotAs(OutputType.FILE);

       val conciseNameArr : Array[String] = baseURL.split("\\.", 3);

       val fileName : String = "/usr/src/app/PermitScrape/screenshots/"+ conciseNameArr(1) +".jpg";
       println("Ok saving " + fileName);


       FileUtils.copyFile(filed, new File(fileName))

   }


}
