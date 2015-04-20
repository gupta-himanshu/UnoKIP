package com.knoldus.sample

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import reactivemongo.api.MongoDriver
import scala.util.Failure
import scala.util.Success
import reactivemongo.bson._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import com.knoldus.dbconnection.Connector

case class People(_id: BSONObjectID, name: String)
object CRUDSample extends App with Connector {

  implicit val read = Macros.reader[People]
  implicit val write = Macros.writer[People]
  /**
   * finding collection
   */

  val p = People(BSONObjectID.generate, "sand")
  val datab = connector("localhost", "rmongo", "rmongo", "pass")
  val dbcrud = new DBCrud[People](datab, "table1")
  private val objectId = BSONObjectID.generate
  /**
   * insert collection
   */
  val isInserted: Future[Boolean] = dbcrud.insert(People(objectId, "xyz"))
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)

  /**
   * Update collection
   */
  val isUpdated = dbcrud.update(objectId.stringify, People(objectId, "xyz"))
  val isUpdatedDone = Await.result(isUpdated, 1 seconds)
  println(isUpdatedDone)

  /**
   * delete document
   */
  val isDeleted = dbcrud.delete(objectId.stringify)
  val isDeletedDone = Await.result(isDeleted, 1 seconds)
  println(isDeletedDone)
}