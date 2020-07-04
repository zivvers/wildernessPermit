package doctype

import java.util.Date;


object WildernessPermit {

   def apply( datePulled : Date
               , datePermit : Date
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int) : 
       new WildernessPermit(datePulled, datePermit, trailHeadID, trailHead, area, numAvail, quota) 

}

case class WildernessPermit( datePulled : Date
               , datePermit : Date
               , trailHeadID : String
               , trailHead : String
               , area : String
               , numAvail : Int
               , quota : Int ) { } 
