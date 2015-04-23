package models

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import reactivemongo.api.Cursor
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Producer.nameValue2Producer

trait FindDoc extends Connector {
  val db = connector("localhost", "rmongo", "username", "Password")
  val coll=db("table1")
  
  def find(id: String)(implicit reader: BSONDocumentReader[User], writer:BSONDocumentWriter[User]):Future[List[User]]= {
    val filter = BSONDocument()
    val cursor = coll.find(query(id), filter).cursor[User]
    cursor.collect[List]().map { x =>
      x
    }
  }
   def findWholeDoc()(implicit reader: BSONDocumentReader[User], writer:BSONDocumentWriter[User]):Cursor[User]= {
    val filter = BSONDocument()
    coll.find(BSONDocument(), filter).cursor[User]    
  }

  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
object FindDoc extends FindDoc