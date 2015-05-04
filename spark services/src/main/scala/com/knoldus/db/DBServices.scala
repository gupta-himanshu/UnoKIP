package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Tweet
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONDocument

trait DBServices extends DBConnector {

  val filter = BSONDocument()
  val query = BSONDocument()
  val collTweet = db("table1")

  def insert(tweet: Tweet): Future[Boolean] = {
    collTweet.insert(tweet).map { lastError =>
      lastError.ok
    }
  }

  def getChunckOfTweet(pageNumber: Int, pageSize: Int): Future[List[Tweet]] = {
    collTweet.find(query).options(QueryOpts((pageNumber - 1) * pageSize, pageSize)).cursor[Tweet].collect[List](pageSize)
  }
}

object DBServices extends DBServices