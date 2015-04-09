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
  val p =People("sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  implicit val coll1=datab("table1")
  val num =find("xxyz")
  val future = Await.result(num, 1 seconds)
  println(future)
}