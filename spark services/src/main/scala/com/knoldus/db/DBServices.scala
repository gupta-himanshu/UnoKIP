package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Models.Tweet
import reactivemongo.bson.BSONDocument
import reactivemongo.api.Cursor

trait DBServices extends DBConnector {

  val db = connector("localhost", "rmongo", "username", "Password")
  val coll = db("table1")
  def insert(tweet: Tweet): Future[Boolean] = {
    coll.insert(tweet).map { lastError =>
      lastError.ok
    }
  }
  def findWholeDoc(): Future[List[Tweet]] = {
    val filter = BSONDocument()
    coll.find(BSONDocument(), filter).cursor[Tweet].collect[List]()
  }
}

object DBServices extends DBServices
