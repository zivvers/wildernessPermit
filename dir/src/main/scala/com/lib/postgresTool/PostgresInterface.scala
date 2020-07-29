package lib.postgresTool


import doobie.util.ExecutionContexts
import doobie.util.fragment.Fragment
import doobie._
import doobie.implicits._
import doobie.implicits.legacy.localdate._
import cats._
import cats.effect._
import cats.implicits._

import doctype._

object PostgresInterface {


   private implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

   private val xa = Transactor.fromDriverManager[IO](
          "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/postgres", sys.env("POSTGRES_USER"), sys.env("POSTGRES_PASSWORD")
    )



   def insertPermitData( data : List[WildernessPermit], tableName : String )
   {
      // make sure table exists
      val create = sql"""CREATE TABLE IF NOT EXISTS $tableName ${WildernessPermit.getSQLStr()}""".update.run
      create.transact(xa).unsafeRunSync

      /*val batch = data traverse { item => sql"""insert into $tableName ${WildernessPermit.getSQLStr()} values ( $item.datePermit
                                                                                                      , $item.trailHeadID
                                                                                                      , $item.trailHead
                                                                                                      , $item.area
                                                                                                      , $item.numAvail
                                                                                                      , $item.quota
                                                                                                      , $item.reserveType )""".update.run }
   
      */
      val item = data(0)
      
      val batch = sql"""insert into $tableName ${WildernessPermit.getSQLStr()} values ( ${item.datePermit}
                                                                                                      , ${item.trailHeadID}
                                                                                                      , ${item.trailHead}
                                                                                                      , ${item.area}
                                                                                                      , ${item.numAvail}
                                                                                                      , ${item.quota}
                                                                                                      , ${item.reserveType} )""".update.run 
  

      batch.transact(xa).unsafeRunSync  
   }


}
