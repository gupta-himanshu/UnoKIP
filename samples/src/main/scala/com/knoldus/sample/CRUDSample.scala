package com.knoldus.sample

import com.knoldus.dbconnection._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import reactivemongo.api.MongoDriver
import scala.util.Failure
import scala.util.Success
import reactivemongo.bson._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

case class Knoldus(empId: Int,empName: String, Address : String)  
  
object CRUDSample extends App with Connector{
  
  /*implicit val readerKnol: BSONDocumentReader[Knoldus] = Macros.reader[Knoldus]
  implicit val writerKnol: BSONDocumentWriter[Knoldus] = Macros.writer[Knoldus]
  val knoldus = Knoldus(1046,"pushpendu","Jaipur")
  val isInsertedKnol:Future[Boolean] = insert(knoldus)
  val isInsertedDoneKnol = Await.result(isInsertedKnol, 1 seconds)
  println(isInsertedDoneKnol)*/

  
  /**
   * finding collection
   */
  val p =People(BSONObjectID.generate,"sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val dbcrud=new DBCrud(datab,"table1")
  /**
   * insert collection
   */
  val isInserted:Future[Boolean] = dbcrud.insert(People(BSONObjectID.generate,"xyz"))
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)
  
  /**
   * Update collection
   */
  val isUpdated = dbcrud.update(People(BSONObjectID.generate,"First Name"))
  val isUpdatedDone = Await.result(isInserted, 1 seconds)
  println(isUpdatedDone)
  
  /**
   * Update collection
   */
  val isDeleted = dbcrud.delete(People(BSONObjectID.generate,"First Name"))
  val isDeletedDone = Await.result(isInserted, 1 seconds)
  println(isDeletedDone)
}