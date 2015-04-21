package com.knoldus.dbconnection

import akka.actor.ActorLogging
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONObjectID

class DBCrud[T](db: DefaultDB, collection: String)(implicit reader: BSONDocumentReader[T], writer: BSONDocumentWriter[T]) extends Connector {
  val coll = db(collection)
  def insert(person: T): Future[Boolean] = {

    coll.insert(person).map { lastError =>
      lastError.ok
      

    }
  }

  def update(id: String, person: T): Future[Boolean] = {
    coll.update(query(id), person).map { lastError =>
      lastError.updatedExisting
    }
  }

  def delete(person: String): Future[Boolean] = {
    coll.remove(query(person)).map { lastError =>
      lastError.ok
    }
  }

  private def query(id: String): BSONDocument =
    BSONDocument("_id" -> BSONObjectID(id))
}
