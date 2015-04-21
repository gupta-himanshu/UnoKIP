package com.knoldus.dbcrud

import com.knoldus.dbconnection.Connector
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONValue
import reactivemongo.api.collections.default
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID
import org.jboss.netty.util.DefaultObjectSizeEstimator
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import scala.concurrent.Future
import reactivemongo.api.Cursor

class FindDoc[T](db:DefaultDB,collection:String) extends Connector {
  val coll=db(collection)
  def find[T](id: String)(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T]):Future[List[T]]= {
    val filter = BSONDocument()
    val cursor = coll.find(query(id), filter).cursor[T]
    cursor.collect[List]().map { x =>
      x
    }
  }
  def findWholeDoc[T]()(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T]):Cursor[T]= {
    val filter = BSONDocument()
    coll.find(BSONDocument(), filter).cursor[T]    
  }
  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
