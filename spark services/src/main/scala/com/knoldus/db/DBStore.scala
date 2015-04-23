package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.tweetstreaming.Tweet
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import com.knoldus.tweetstreaming.Tweet

class DBCrud(db: DefaultDB, collection: String) extends Connector {
  
  val coll = db(collection)
  def insert(person: Tweet): Future[Boolean] = {
    implicit val read = Macros.reader[Tweet]
    implicit val write = Macros.writer[Tweet]
    coll.insert(person).map { lastError =>
      lastError.ok
    }
  }
}