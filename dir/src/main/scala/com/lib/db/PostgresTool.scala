package lib.db


import doobie.util.ExecutionContexts
import doobie.util.fragment.Fragment
import doobie._
import doobie.implicits._
import doobie.implicits.legacy.localdate._
import cats._
import cats.effect._
import cats.implicits._

import doctype._



case class PostgresTool(schemaName : String, tableName : String) {


   private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

   private val xa = Transactor.fromDriverManager[IO](
          "org.postgresql.Driver", s"jdbc:postgresql://${sys.env("POSTGRES_HOST_NAME")}:5432/${sys.env("POSTGRES_DB")}", sys.env("POSTGRES_USER"), sys.env("POSTGRES_PASSWORD")
    )

   /*
    * temp yolo mode
    *
    */

   val y = xa.yolo // a stable reference is required
   import y._

   //val tableName : String;
   //val schemaName : String;

   def deleteSchema() = 
   {

      val dropSchema = (fr"DROP SCHEMA IF EXISTS" ++ Fragment.const0(schemaName) ++ fr"CASCADE").update.run
      dropSchema.transact(xa).unsafeRunSync

   }


   def createSchemaTable() =
   {

      val createSchema = (fr"CREATE SCHEMA IF NOT EXISTS" ++ Fragment.const0(schemaName)).update.run

      val createTable = ( fr"CREATE TABLE IF NOT EXISTS" ++  Fragment.const0(schemaName) ++ fr"." ++ Fragment.const(tableName) ++
            WildernessPermit.getPostgresTableFragment ).update.run

      (createSchema, createTable).mapN(_ + _).transact(xa).unsafeRunSync


   }

   def querySchema() =
   {
      println("pulling from info schema")
      sql"select * from information_schema.tables where table_schema = 'permits' or table_name = 'inyo'"
              .query[(Option[String], Option[String], Option[String], Option[String], Option[String])] // Query0[String]
              .stream        // Stream[ConnectionIO, String]
              .take(100)       // Stream[ConnectionIO, String]
              .quick         // IO[Unit]
              .unsafeRunSync

      //println( res )


   }

   def insertPermitData( data : List[WildernessPermit] )
   {
   
      val insert = Update[WildernessPermit](s"insert into ${schemaName}.${tableName} (datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType) values (?, ?, ?, ?, ?, ?, ?, ?)")
                     .updateMany( data )


      insert.transact(xa).unsafeRunSync
      /*
      val item = data(0)
      
      val batch = sql"""insert into $tableName ${WildernessPermit.getSQLStr()} values ( ${item.datePermit}
                                                                                                      , ${item.trailHeadID}
                                                                                                      , ${item.trailHead}
                                                                                                      , ${item.area}
                                                                                                      , ${item.numAvail}
                                                                                                      , ${item.quota}
                                                                                                      , ${item.reserveType} )""".update.run 
  

      batch.transact(xa).unsafeRunSync 
      */ 
   }


}

object PostgresTool {

   def apply(schema : String , table : String) : PostgresTool 
      = new PostgresTool( schema, table )


}
