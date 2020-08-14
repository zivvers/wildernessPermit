package com.permitScrape


import java.io._
import java.time.LocalDate


import doobie.util.ExecutionContexts
import doobie._
import doobie.implicits._
//import doobie.implicits.legacy.localdate._
import cats._
import cats.effect._
import cats.implicits._


import lib.seleniumTool._
import lib.postgresTool._
import doctype._

object App {

   private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
   private val xa = Transactor.fromDriverManager[IO](
          "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", sys.env("POSTGRES_USER"), sys.env("POSTGRES_PASSWORD")
    )

   
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

      
      val create = sql"""CREATE TABLE IF NOT EXISTS $tableName ${WildernessPermit.getSQLStr()}""".update.run
      create.transact(xa).unsafeRunSync


      /* build insertion statements */
      val updateList : List[Update0] = data.map( item => item.toDoobieInsertion( tableName ) )


      val batch = updateList.traverse( item => item.run  )

      batch.transact(xa)
           .unsafeRunSync()

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
