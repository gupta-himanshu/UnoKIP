package com.knoldus.dbcrud

import com.knoldus.dbconnection.Connector
import com.knoldus.dbconnection.Convertor
import com.knoldus.converter.JsonConverter
import com.knoldus.dbconnection.People
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONValue._
import reactivemongo.api.collections.default._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID
import org.jboss.netty.util.DefaultObjectSizeEstimator
import reactivemongo.api.DefaultDB

class FindDoc(db:DefaultDB,collection:String) extends Connector with Convertor with JsonConverter {
  val coll=db(collection)
  def find(person: People)= {
    val filter = BSONDocument(
      "name" -> 1)
    val cursor = coll.find(query(person._id.stringify), filter).cursor[People]
    cursor.collect[List]().map { x =>
      x
    }
  }
  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
