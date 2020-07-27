package com.permitScrape


//import scalaz._, Scalaz._
//import doobie.imports._
//import doobie._
//import doobie.util.ExecutionContexts
//import cats.effect._ // e.g. IO

import java.io._
import java.time.LocalDate

import seleniumTool._
import doctype._
object App {

   def main(args: Array[String]) {

      val crawler : RecreationCrawler = new RecreationCrawler()      

      val data : List[WildernessPermit] = crawler.scrapePermitAvailability()
      val pw = new PrintWriter(new File(s"/usr/src/app/PermitScrape/random/permitData_${LocalDate.now().toString()}.tsv"  ) )
      
      pw.write(data(0).productElementNames.toList.mkString("\t"))
      for (item <- data)
      {
         
         pw.write( item.toTSV() )

      }

      pw.close()
      

   } 



}
