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
import com.knoldus.dbconnection.DBCrud
import java.rmi.server.ObjID

case class People(_id: BSONObjectID, name: String)
object CRUDSample extends App with Connector{
  
  implicit val readerKnol: BSONDocumentReader[People] = Macros.reader[People]
  implicit val writerKnol: BSONDocumentWriter[People] = Macros.writer[People]
 
  /**
   * finding collection
   */
  val p =People(BSONObjectID.generate,"sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val dbcrud=new DBCrud[People](datab,"table1")
  val objId=BSONObjectID.generate
  /**
   * insert collection
   */
  val isInserted:Future[Boolean] = dbcrud.insert(People(objId,"xyz"))
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)
  
  /**
   * Update collection
   */
  val isUpdated = dbcrud.update(objId.stringify,People(BSONObjectID.generate,"First Name"))
  val isUpdatedDone = Await.result(isInserted, 1 seconds)
  println(isUpdatedDone)
  
  /**
   * Update collection
   */
  val isDeleted = dbcrud.delete(objId.stringify)
  val isDeletedDone = Await.result(isInserted, 1 seconds)
  println(isDeletedDone)
}