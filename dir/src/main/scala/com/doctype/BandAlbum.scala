package doctype

import org.mongodb.scala.bson.ObjectId;

case class BandAlbum(_id: ObjectId, artist: String, album: String, url: String, location: String)

object BandAlbum {
   def apply( artist: String, album: String, url: String, location: String): Band =
      new BandAlbum( new ObjectId(), artist, album, url, location )   
}


