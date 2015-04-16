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


object CRUDSample extends App with DBCrud{
  
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
  implicit val coll1=datab("table1")
  val num =find(People(BSONObjectID.generate,"pp"))
  val future = Await.result(num, 1 seconds)
  println(future)
  /**
   * insert collection
   */
  val isInserted:Future[Boolean] = insert(People(BSONObjectID.generate,"xyz"))
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)
  
  /**
   * Update collection
   */
  val isUpdated = update(People(BSONObjectID.generate,"First Name"))
  val isUpdatedDone = Await.result(isInserted, 1 seconds)
  println(isUpdatedDone)
  
  /**
   * Update collection
   */
  val isDeleted = delete(People(BSONObjectID.generate,"First Name"))
  val isDeletedDone = Await.result(isInserted, 1 seconds)
  println(isDeletedDone)
}