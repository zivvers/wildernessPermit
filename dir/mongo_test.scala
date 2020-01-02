import org.mongodb.scala._
//impo
import org.mongodb.scala.bson.ObjectId
//org.mongodb.scala.bson

val mongoClient: MongoClient = MongoClient("mongodb://db:27017/")
val database: MongoDatabase = mongoClient.getDatabase("bc_graph")

//db.createCollection("bands", 

/*
object Band {
   def apply(name: String, url: String, location: String): Band =
      Band(new ObjectId(), name, url, location)   
}

case class Band(_id: ObjectId, name: String, url: String, location: String)
*/

val doc1 = bson.Document("AL" -> bson.BsonString("Alabama"))

val collection: MongoCollection[Document] = database.getCollection("prelim_test")


collection.insertOne(doc1);//.results()

