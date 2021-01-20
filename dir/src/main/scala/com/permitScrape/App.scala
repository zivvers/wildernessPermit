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
import cats.data.NonEmptyList
//import java.sql.Instant

import lib.selenium.RecreationCrawler
import lib.db.PostgresTool
import doctype._

//import doobie.implicits.legacy.localdate._

object App {


   def main(args: Array[String]) {

      /*
      sql"select * from information_schema.tables"
        .query[(Option[String], Option[String], Option[String], Option[String], Option[String])] // Query0[String]
        .stream        // Stream[ConnectionIO, String]
        //.take(100)       // Stream[ConnectionIO, String]
        .quick         // IO[Unit]
        .unsafeRunSync
      */
      if ( args.size < 2 )
      {
         
         println("Need to provide schema and table name as argument")
         println("args size: " + args.size)
         return
      }
      
      
      val schemaName : String = args(0)
      
      val tableName : String = args(1)
      
      //print(s"schema: ${schemaName}")
      //print(s"table: ${tableName}")

      /*
      val crawler : RecreationCrawler = new RecreationCrawler( "https://www.recreation.gov/permits/233262/registration/detailed-availability?type=overnight-permit&date=2021-05-01" )      

      // wilderness permit season starts May 1st
      val data : List[WildernessPermit] = crawler.scrapePermitAvailability()

      //val dataNonEmpty : Option[ NonEmptyList[WildernessPermit] ] = NonEmptyList.fromList( data )
      //val drop = sql"drop table if exists ${tableName}".update.run
      //println(drop)
      //val dropString = s"DROP SCHEMA IF EXISTS $schemaName CASCADE;"
      //println(dropString)
      */
      val postgres = new PostgresTool(schemaName, tableName)

      //postgres.deleteSchema()

      //postgres.createSchemaTable()

      postgres.querySchema()

      postgres.insertPermitData( List( WildernessPermit(LocalDate.now()
                                                                                          ,  LocalDate.now()
                                                                                          , "test"
                                                                                          , "test"
                                                                                          , "test"
                                                                                          , 10
                                                                                          , 10
                                                                                          , "test"
                                                                                          )) )

      /*
      val dropSchema = (fr"DROP SCHEMA IF EXISTS" ++ Fragment.const0(schemaName) ++ fr"CASCADE").update.run

      //val dropSchema = sql"DROP SCHEMA IF EXISTS taco CASCADE;".update.run
      val createSchema = (fr"CREATE SCHEMA IF NOT EXISTS" ++ Fragment.const0(schemaName)).update.run
      //val createSchema = sql"CREATE SCHEMA IF NOT EXISTS $schemaName;".update.run

      val createTable = ( fr"CREATE TABLE IF NOT EXISTS" ++  Fragment.const0(schemaName) ++ fr"." ++ Fragment.const(tableName) ++
            WildernessPermit.getPostgresTableFragment() ).update.run
      
      for {
      
      _ <- dropSchema.transact(xa)
      _ <- createSchema.transact(xa)
      _ <- createTable.transact(xa)

      } yield ()

      println("dropping schema")
      dropSchema.transact(xa).unsafeRunSync

      println("creating schema")
      createSchema.transact(xa).unsafeRunSync

      println("creating table")
      createTable.transact(xa).unsafeRunSync
      */

      //create.transact(xa).unsafeRunSync

      // ok!
      /*
      val drop = sql"drop table if exists example_ts".update.run
      val create =
      sql"create table if not exists example_ts (data TEXT NOT NULL, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP)".update.run
      */
      //val insert = Update[WildernessPermit](s"insert into $tableName (datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType) values (?, ?, ?, ?, ?, ?, ?, ?)")
      //   .updateMany( data )
      //(drop, create).mapN(_ + _).transact(xa).unsafeRunSync

   //(drop, create).mapN(_ + _).transact(xa).unsafeRunSync
   //drop.transact(xa).unsafeRunSync 
   //println("dropped succeeded")
   //create.transact(xa).unsafeRunSync

    /*
    for {
      
      _ <- drop.transact(xa)
      _ <- create.transact(xa)
      //_ <- insert.transact(xa)
    } yield ()
    */

      //val insertSQL : String = WildernessPermit.getPostgresInsertStr( tableName )

      //dataNonEmpty.map( dat => Update[WildernessPermit](insertSQL).updateMany( dat ).transact(xa).unsafeRunSync )

      // data.map( row => row.toDoobieInsertion(tableName).run ).map( elem => elem.transact(xa).unsafeRunSync )

      /* build insertion statements */
      //val updateList : List[Update0] = data.map( item => { println(item.toDoobieInsertion( tableName )); item.toDoobieInsertion( tableName ) })


      //val batch = updateList.traverse( item => item.run  )

      //batch.transact(xa)
      //     .unsafeRunSync()

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
