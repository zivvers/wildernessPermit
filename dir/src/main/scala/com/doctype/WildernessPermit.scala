package doctype

import java.util.Date;
import java.time.LocalDate

import doobie._
import doobie.implicits._
import doobie.implicits.legacy.localdate._

object WildernessPermit {

   def apply( datePulled : LocalDate
               , datePermit : LocalDate
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int
               , reserveType /* walk-up or reserve ahead */: String) : WildernessPermit = 
       new WildernessPermit(datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota, reserveType) 

   /*
    * "static" method to get fields / type for postgres table creation / insertion
    * NOTE: text postgresql type is unlimited character  
    */
   def getSQLStr() : String =
   {

      return """( datePulled : date
               , datePermit : date
               , trailHeadID : text
               , trailHead : text
               , area : text
               , numAvail : int
               , quota : int
               , reserveType : text ) """
     
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
