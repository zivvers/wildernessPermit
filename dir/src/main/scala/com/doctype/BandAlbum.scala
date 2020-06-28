package doctype
//import doctype.BandCampPage; -> already in scope
import org.mongodb.scala.bson.ObjectId;
//import org.mongodb.scala.bson.ISODate;
import java.util.Date;



object BandAlbum {
   def apply( artist: String
   				, album: String
   				, url: String
   				, rawLocation: String
   				, parsedLocation: String
   				, parent: String): BandAlbum =
      new BandAlbum( url, artist, album, url, rawLocation, parsedLocation, parent, new Date() )   



}
/* NOTE: use url as ID! */
case class BandAlbum(_id: String
						, artist: String
						, album: String
						, url: String
						, rawLocation: String
						, parsedLocation: String
						, parent: String
					    , dateAccessed : Date) extends BandCampPage(_id, url, rawLocation, parsedLocation)
{

	override def print() : Unit =
    {
    	printf("artist: " + artist + ", album: " + album + ", unparsed location: " + rawLocation);
    }
}


