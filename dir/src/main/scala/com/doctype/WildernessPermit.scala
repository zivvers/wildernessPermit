package doctype

import java.util.Date;
import java.time.LocalDate
import java.time.LocalDateTime
import java.sql.Timestamp
import java.time.Instant

import doobie._
import doobie.implicits._
//import doobie.implicits.localdate._
import doobie.implicits.javatime._

//import doobie.implicits.javasql._


object WildernessPermit {


   def apply( datePulled : LocalDate
               , datePermit : LocalDate
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int
               , /* walk-up or reserve ahead */ reserveType : String) : WildernessPermit = 
       new WildernessPermit( datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType) 


   /*
    * "static" method to get fields / type for postgres table creation / insertion
    * NOTE: text postgresql type is unlimited character  
    */
   def getPostgresTableFragment : Fragment =
   {

      fr""" ( datePulled TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                  , datePermit DATE
                  , trailHeadID TEXT NOT NULL
                  , trailHead TEXT NOT NULL
                  , area TEXT NOT NULL
                  , numAvail SMALLINT
                  , quota SMALLINT
                  , reserveType TEXT NOT NULL )"""
     
   }

   def getSQLStr() : String = {
      "( datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType )"
   }

   def getPostgresInsertStr(table : String) : String =
   {

      s"insert into $table ( datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType ) values (?, ?, ?, ?, ?, ?, ?, ?)"

   }

}

case class WildernessPermit( datePulled : LocalDate
               , datePermit : LocalDate
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int
               , reserveType : String ) 
{ 
   
   def toTSV() : String = 
   {
   
      return s"${datePulled.toString}\t${datePermit.toString}\t${trailHeadID}\t${trailHead}\t${area}\t${numAvail}\t${quota}\t${reserveType}" 
   
   }

   /* turn a WildernessPermit instance into a doobie Update0 */
   
   def toDoobieInsertion( tableName : String ) : Update0 = 
   { 
      return sql"""insert into $tableName ${WildernessPermit.getSQLStr()} 
                     values ( ${datePulled}, ${datePermit}, ${trailHeadID}, ${trailHead}, ${area}, ${numAvail}, ${quota}, ${reserveType} )""".update
   }
   
} 
