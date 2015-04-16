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

class DBCrud[T](db:DefaultDB,collection:String) extends Connector  with JsonConverter {

  val coll = db(collection)
  def insert[T](person: T)(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T]): Future[Boolean] = {
    coll.insert(person).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }

  def update[T](person: T)(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T]): Future[Boolean] = {
    coll.update(query(person), person).map { lastError =>
      lastError.updatedExisting
    }
  }

  def delete[T](person: String)(implicit reader: BSONDocumentReader[T], writer:BSONDocumentWriter[T]): Future[Boolean] = {
    coll.remove(query(person)).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }

  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
