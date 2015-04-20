package com.knoldus.dbcrud

import com.knoldus.dbconnection.Connector
import com.knoldus.converter.JsonConverter
import com.knoldus.dbconnection.People
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONValue._
import reactivemongo.api.collections.default._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID
import org.jboss.netty.util.DefaultObjectSizeEstimator
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

class FindDoc(db:DefaultDB,collection:String) extends Connector with JsonConverter {
  val coll=db(collection)
  def find[T](person: T)(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T])= {
    
    val filter = BSONDocument()
    val cursor = coll.find(query("s"), filter).cursor[T]
    cursor.collect[List]().map { x =>
      x
    }
  }
  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
