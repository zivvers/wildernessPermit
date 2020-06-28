package doctype
//import doctype.BandCampPage; -> already in scope
import org.mongodb.scala.bson.ObjectId;
//import org.mongodb.scala.bson.ISODate;
import java.util.Date;

/* a buyer link represents a single instance of a buyer that bought 
   the primary album and secondary album */

object BuyerLink {

   def apply( name: String
          , url: String
   				, primaryAlbumURL: String
   				, secondaryAlbumURL: String
   				, rawLocation: String
          , parsedLocation: String): BuyerLink =
      new BuyerLink( new ObjectId(), name, url, primaryAlbumURL, secondaryAlbumURL, rawLocation, parsedLocation, new Date() )   


}
/* NOTE: use url as ID! */
case class BuyerLink(_id: ObjectId
						, name: String
						, url: String
            , primaryAlbumURL: String
            , secondaryAlbumURL: String
            , rawLocation: String
            , parsedLocation: String
					  , dateAccessed : Date) extends BandCampPage(_id, url, rawLocation, parsedLocation)
{

	override def print() : Unit =
  {
    printf("buyer name: " + name + ", url: " + url + ", unparsed location: " + rawLocation);
  }
}
