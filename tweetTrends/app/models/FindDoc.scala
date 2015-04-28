package models

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.api.Cursor
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Producer.nameValue2Producer
import com.knoldus.tweetstreaming.SparkStreaming
import com.knoldus.tweetstreaming.Tweet

trait FindDoc extends Connector {
  val db = connector("localhost", "rmongo", "username", "Password")
  val coll=db("table1")
  
   def findWholeDoc()(implicit reader: BSONDocumentReader[Tweet], writer:BSONDocumentWriter[Tweet]):Cursor[Tweet]= {
    val filter = BSONDocument()
    coll.find(BSONDocument(), filter).cursor[Tweet]    
  }

  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}

object FindDoc extends FindDoc
