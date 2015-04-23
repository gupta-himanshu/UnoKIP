package models

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Producer.nameValue2Producer


trait DBCrud extends Connector{
  val db = connector("localhost", "rmongo", "username", "Password")
  val coll=db("table1")
  
  def insert(person: User)(implicit reader: BSONDocumentReader[User], writer:BSONDocumentWriter[User]): Future[Boolean] = {
    coll.insert(person).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }

  def update(id: String, person: User)(implicit reader: BSONDocumentReader[User], writer:BSONDocumentWriter[User]): Future[Boolean] = {
    coll.update(query(id), person).map { lastError =>
      lastError.updatedExisting
    }
  }

  def delete(person: String): Future[Boolean] = {
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
object DBCrud extends DBCrud