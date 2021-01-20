package lib.selenium

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import doobie.util.ExecutionContexts
import doobie._
import doobie.implicits._
//import doobie.postgres.pgisimplicits._
import cats._
import cats.effect._
import cats.implicits._


import scala.collection.JavaConverters._
import scala.util.control.Breaks._ //need for scala break statement
import scala.concurrent.Await
//import scala.concurrent.duration.TimeUnit
import scala.concurrent.duration.Duration

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.By;
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.remote.Augmenter // experimental class!

import java.net.URL
import java.io._
import java.util.concurrent.TimeUnit
import java.util.Calendar
import java.time.LocalDate
//import java.time.DateTimeFormatter
import java.time._
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale
import java.time.temporal.TemporalAccessor


import org.apache.commons.io.FileUtils

/* for error:  Cannot find an implicit ExecutionContext */
import scala.concurrent.ExecutionContext.Implicits.global

import doctype._;


class RecreationCrawler(baseURL : String = "https://www.recreation.gov/permits/233262/registration/detailed-availability?type=overnight-permit") extends Crawler() {

   /* optional URL will get us to permits for today + 5 days after */
   driver.get( baseURL );
   
   println("navigating to website ... ")

   // parses the date information for a given table 
   private val dateFormat : DateTimeFormatter = new DateTimeFormatterBuilder()
                                       .parseCaseInsensitive()
                                       .appendPattern("yyyy MMM EEE d")
                                       .toFormatter()

   
   private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

   private val xa = Transactor.fromDriverManager[IO](
          "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", sys.env("POSTGRES_USER"), sys.env("POSTGRES_PASSWORD")
    )
  


   def scrapePermitAvailability(  ) : List[WildernessPermit] = {

      /* button for whether commercially guided */

      val guideRadioButtons : List[WebElement] = driver.findElements( By.cssSelector("label.rec-label-radio") ).asScala.toList

      println("# of radios " + guideRadioButtons.size) 
      
      waitForLoad.until(ExpectedConditions.elementToBeClickable( guideRadioButtons(1) ))

      guideRadioButtons(1).click()

      println("clicked!")
	
      // val groupSizeIn : WebElement = driver.findElement( By.cssSelector("input#number-inpute") )

      // groupSizeIn.sendKeys("1", Keys.Enter)

      
      //var availTabl : WebElement = driver.findElement( By.cssSelector("table#availability-table") )
      println("wait table load")

      // val firstRow : WebElement = driver.findElement( By.cssSelector("table#availability-table tbody tr") )

      waitForLoad.until( ExpectedConditions.visibilityOfElementLocated( By.cssSelector("table#availability-table tbody tr") ) )
      
      // click to give a "Group Size" of 1
      driver.findElements( By.cssSelector("div.sarsa-number-field button") ).asScala.toList(1).click()

      
      val data : List[WildernessPermit]  = Iterator.continually {
         

         //
         // TODO: need a way to proceed only when data has changed!
         //

         try
         {
            // this wait is for lagging load of trailhead availability 
            // ( sometimes showing as "unavailable" for a moment )
            waitForLoad.until(
               ExpectedConditions.elementToBeClickable( By.cssSelector("td.available") ) 
            )
         
         }
         catch // the wait could fail erroneously if no permits available for "next" 5 days
         {

            case err: TimeoutException => println(s"available trailheads timed out")

         }     
         
         val tableHTML : String = driver.executeScript( "return arguments[0].outerHTML;"
         											, driver.findElement( By.cssSelector("div.per-availability-content") ) ).toString() 
  
         val doc : Document = Jsoup.parse( tableHTML );

         val date : String = driver.findElement( By.cssSelector("div.per-availability-content span.date") ).getText()
         println(s"DATE: ${date}")
         //click "Next 5 days" button

         

         val nextButton = driver.findElements( By.cssSelector("div.navigator-buttons button") ).asScala.toList(1)
         println("button enabled? : " + nextButton.isEnabled() )  
         
         if (nextButton.isEnabled())
         {
            nextButton.click()

            // wait for table to change before 
            waitForLoad.until(ExpectedConditions.not(
                     
                     ExpectedConditions.textToBePresentInElement(
                                          driver.findElement( By.cssSelector("div.per-availability-content span.date") )
                                                               , date)
                     )
                  );


            ( Some(doc), Some(pullStartDate( doc.select("div.per-availability-content").first() )) )

      } else {
         (None, None)
      }
      }.takeWhile( tup => tup._1 != None )
      .flatMap{ case ( doc : Some[Document], startDate : Some[LocalDate] )  =>  doc.get.select("tbody tr").asScala.toList map (( _ , startDate ))  }
      //.takeWhile( tup => { /* println(s"month: ${tup._2.getMonth}, eq to August? ${tup._2.getMonth == Month.AUGUST}"); */  tup._2.getMonth != Month.AUGUST })
      .flatMap{ case (row : Element, startDate : Some[LocalDate]) => (0 until 5).map( i => {
                                                                        
                                                                     val (numAvail, quota, reserveType) = pullAvailQuota( row.select(s"td:eq(${3+i}").first() )
                                                                     //println(s"date ${startDate}")
                                                                     val permitObj : WildernessPermit = WildernessPermit( LocalDate.now()
                                                                                          , startDate.get.plusDays(i) 
                                                                                          , row.select("td:eq(0)").text().trim()
                                                                                          , row.select("td:eq(1)").text().trim()
                                                                                          , row.select("td:eq(2)").text().trim()
                                                                                          , numAvail
                                                                                          , quota
                                                                                          , reserveType
                                                                                          )
                                                                     permitObj
                                                             } ) }
      .toList

      data

   }

   
   def pullAvailQuota( cellElem : Element /* JSoup Document */ ) : (Int, Int, String) =
   {

      if (cellElem.attr("class") == "unavailable")
      {
         return (0, 0, "reserve ahead")
      }
      else if (cellElem.attr("class") == "walk-up")
      {

         return (0, 0, "walk-up")
      }   
      else
      {
         println(cellElem.select("div.rec-availability-hint").attr("aria-label"))
         val availStr : String = cellElem.select("div.rec-availability-hint").attr("aria-label").split(":")(1).trim()
         val intFind = "(\\d+)(\\D+)(\\d+)".r
         
         availStr match 
         {
            case intFind(availNum,_, quota) => return (availNum.toInt, quota.toInt, "reserve ahead")  
         }
      }

   }

   def pullStartDate( parsedHTML : Element /* JSoup Document */ ) : LocalDate = 
   {
      println("in pull start")
      val monthYearTitl : String = parsedHTML.select( "div.navigator-title" ).text().split("/")(0).trim()
      print("try 2nd line")
      val firstDateElem : Element = parsedHTML.select( "thead th:not(.sortable-column-header)" ).first()
      print( "weekday day str: " + firstDateElem.text() )
      val firstDateStr : String = monthYearTitl + " " + firstDateElem.select(".weekday").text().trim() + " " + firstDateElem.select(".date").text().trim()
 
      println("FIRST DATE: " + firstDateStr)

      val firstDate : LocalDate = LocalDate.from( dateFormat.parse( firstDateStr ) ) 
 
      firstDate
   }



   def writeHTML( name : String, html : String ) 
   {
      val pw = new PrintWriter(new File("/usr/src/app/PermitScrape/screenshots/" + name ) )
      pw.write( html )
      pw.close()

   }


   def getScreenshot(indx : Int)
   {

       //Thread.sleep(15000); // sleepy boi 
       val filed: File = driver.getScreenshotAs(OutputType.FILE);

       val conciseNameArr : Array[String] = baseURL.split("\\.", 3);

       val fileName : String = "/usr/src/app/PermitScrape/screenshots/"+ conciseNameArr(1) + s"_$indx.jpg";
       println("Ok saving " + fileName);


       FileUtils.copyFile(filed, new File(fileName))

   }


}
