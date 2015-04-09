package com.knoldus.sample

import com.knoldus.dbconnection.Connector
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import reactivemongo.api.MongoDriver
import scala.util.Failure
import scala.util.Success
import reactivemongo.bson.BSONString

object SampleObject extends App with Connector{
  val res = connector("localhost","rmongo","rmongo","pass")
  val coll = res("table1")
  val query = BSONDocument()
  val filter = BSONDocument(
    "firstName" -> 1,
    "lastName" -> 1,
    "_id" -> 0)
  val select = coll.find(query, filter).cursor[BSONDocument].collect[List]()
  select.map{list=>
    list.foreach{doc=>
      println(BSONDocument.pretty(doc))  
    }
  }
  println(coll)
}