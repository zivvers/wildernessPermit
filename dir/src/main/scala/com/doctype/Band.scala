package doctype

import org.mongodb.scala.bson.ObjectId;

case class Band(_id: ObjectId, name: String, url: String, location: String)

object Band {
   def apply(id: String, name: String, url: String, location: String): Band =
      new Band(new ObjectId(), name, url, location)   
}


