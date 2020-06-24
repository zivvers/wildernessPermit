package doctype

import org.mongodb.scala.bson.ObjectId;
//import org.mongodb.scala.bson.ISODate;
import java.util.Date;



object BandAlbum {
   def apply( artist: String
   				, album: String
   				, url: String
   				, location: String
   				, parent: String): BandAlbum =
      new BandAlbum( new ObjectId(), artist, album, url, location, parent, new Date() )   



}

case class BandAlbum(_id: ObjectId
						, artist: String
						, album: String
						, url: String
						, location: String
						, parent: String
					    , date : Date)
{

	def print()
    {
    	printf("Artist: " + artist + ", Album: " + album + ", Location: " + location);
    }
}


