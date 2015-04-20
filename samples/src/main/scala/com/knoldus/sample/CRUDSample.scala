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
import com.knoldus.dbconnection.Abstract

case class Knoldus(_id: BSONObjectID, empId: Int,empName: String, Address : String)
  
object CRUDSample extends App with Connector{
  
  implicit val reader: BSONDocumentReader[Knoldus] = Macros.reader[Knoldus]
  implicit val writer: BSONDocumentWriter[Knoldus] = Macros.writer[Knoldus]
  
  /*val knoldus = Knoldus(1046,"pushpendu","Jaipur")
  val isInsertedKnol:Future[Boolean] = insert(knoldus)
  val isInsertedDoneKnol = Await.result(isInsertedKnol, 1 seconds)
  println(isInsertedDoneKnol)*/

  
  /**
   * finding collection
   */
  val p =People(BSONObjectID.generate,"sand")
  val knoldus = Knoldus(BSONObjectID.generate,1047,"pushpendu","Jaipur")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val dbcrud=new DBCrud(datab,"table1")
  
  
  
  
  /**
   * insert collection
   */
  val isInserted:Future[Boolean] = dbcrud.insert(knoldus)
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)
  
  /**
   * Update collection
   */
  val isUpdated = dbcrud.update(knoldus)
  val isUpdatedDone = Await.result(isUpdated, 1 seconds)
  println(isUpdatedDone)
  
  /**
   * Delete collection
   */
  //val objID:BSONObjectID = BSONObjectID("552f70085edccedb337af5df")
  
  val isDeleted = dbcrud.delete("knoldus")
  val isDeletedDone = Await.result(isDeleted, 1 seconds)
  println(isDeletedDone)
}
