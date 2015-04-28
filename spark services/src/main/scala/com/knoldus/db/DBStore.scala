package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.tweetstreaming.Tweet
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import com.knoldus.tweetstreaming.Tweet

trait DBCrud extends Connector {
  
  val db = connector("localhost", "rmongo", "username", "Password")
  val coll = db("table1")

  def insert(person: Tweet): Future[Boolean] = {
    implicit val read = Macros.reader[Tweet]
    implicit val write = Macros.writer[Tweet]
    coll.insert(person).map { lastError =>
      lastError.ok
    }
  }
}

object DBCrud extends DBCrud 