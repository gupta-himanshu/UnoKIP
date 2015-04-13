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


object CRUDSample extends App with DBCrud{
  /**
   * finding collection
   */
  
  val p =People("sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  implicit val coll1=datab("table1")
  val num =find("xxyz")
  val future = Await.result(num, 1 seconds)
  println(future)
  /**
   * insert collection
   */
  val isInserted:Future[Boolean] = insert("""{"name":"xyz"}""")
  val isInsertedDone = Await.result(isInserted, 1 seconds)
  println(isInsertedDone)
  
  /**
   * Update collection
   */
  val isUpdated = update("First Name")
  val isUpdatedDone = Await.result(isInserted, 1 seconds)
  println(isUpdatedDone)
  
  /**
   * Update collection
   */
  val isDeleted = delete("First Name")
  val isDeletedDone = Await.result(isInserted, 1 seconds)
  println(isDeletedDone)
}