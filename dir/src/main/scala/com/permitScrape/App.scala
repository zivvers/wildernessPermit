package com.permitScrape


//import scalaz._, Scalaz._
//import doobie.imports._
import doobie._
import doobie.util.ExecutionContexts
import cats.effect._ // e.g. IO

import seleniumTool._

object App {

   def main(args: Array[String]) {

      val crawler : RecreationCrawler = new RecreationCrawler()      

      crawler.scrapePermitAvailability()
      //crawler.waitGetScreenshot()
      implicit val cs = IO.contextShift(ExecutionContexts.synchronous)


   } 



}
