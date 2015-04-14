package com.knoldus.dbconnection

import akka.actor.ActorLogging
import reactivemongo.api._
import reactivemongo.bson.BSONDocument
import reactivemongo.bson._
import scala.concurrent.Future
import play.api.libs.iteratee.Iteratee
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.impl.Future
import reactivemongo.api.collections.default._
import org.json4s.JsonInput
import com.knoldus.converter.JsonConverter

case class People(_id: BSONObjectID, name: String)

trait convertor {
  implicit val reader: BSONDocumentReader[People] = Macros.reader[People]
  implicit val writer: BSONDocumentWriter[People] = Macros.writer[People]

}
trait DBCrud extends Connector with convertor with JsonConverter {
  import scala.collection.mutable.ListBuffer
  var l: ListBuffer[String] = new ListBuffer

  def find(person:People)(implicit coll: BSONCollection) = {
    val filter = BSONDocument(
      "name" -> 1)
    val cursor = coll.find(query(person._id.stringify), filter).cursor[People]
    cursor.collect[List]().map { x =>
      x
    }
  }

  def insert(person: People)(implicit coll: BSONCollection): Future[Boolean] = {
    coll.insert(person).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }

  def update(person: People)(implicit coll: BSONCollection): Future[Boolean] = {
    coll.update(query(person._id.stringify), person).map { lastError =>
      lastError.updatedExisting
    }
  }

  def delete(person: People)(implicit coll: BSONCollection): Future[Boolean] = {
    coll.remove(query(person._id.stringify)).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }

  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
