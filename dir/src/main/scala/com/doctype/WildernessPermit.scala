package doctype

import java.util.Date;
import java.time.LocalDate


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

}

case class WildernessPermit( datePulled : LocalDate
               , datePermit : LocalDate
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int
               , reserveType : String ) { } 
