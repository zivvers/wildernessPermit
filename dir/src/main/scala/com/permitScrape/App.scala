package com.permitScrape


import java.io._
import java.time.LocalDate


import doobie.util.ExecutionContexts
import doobie.util.fragment.Fragment
import doobie._
import doobie.implicits._
import doobie.implicits.legacy.localdate._
import cats._
import cats.effect._
import cats.implicits._


import lib.seleniumTool._
import lib.postgresTool._
import doctype._

object App {

   
   def main(args: Array[String]) {



      if ( args.size < 1 )
      {
         
         println("Need to provide table name as argument")
         println("args size: " + args.size)
         return
      }
      
      val tableName : String = args(0)

      val crawler : RecreationCrawler = new RecreationCrawler()      

      val data : List[WildernessPermit] = crawler.scrapePermitAvailability()
      /*
      val pw = new PrintWriter(new File(s"/usr/src/app/PermitScrape/random/permitData_${LocalDate.now().toString()}.tsv"  ) )
      
      pw.write(data(0).productElementNames.toList.mkString("\t"))
      

      for (item <- data)
      {
         pw.write( item.toTSV() )
      }

      pw.close()
      */

      /*
      val item = data(0)

      val cmd = sql"INSERT INTO $tableName ${WildernessPermit.getSQLStr()} values ( ${item.datePulled}, ${item.datePermit}, ${item.trailHeadID}, ${item.trailHead}, ${item.area}, ${item.numAvail}, ${item.quota}, ${item.reserveType} )"
      */
      //println( cmd )
   
   } 



}
