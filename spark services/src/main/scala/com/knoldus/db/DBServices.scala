package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.api.QueryOpts

trait DBServices extends DBConnector {
  val filter = BSONDocument()
  val query=BSONDocument()
  val collTweet = db("table1")
  def insert(tweet: Tweet): Future[Boolean] = {
    collTweet.insert(tweet).map { lastError =>
      lastError.ok
    }
  }


  def filterQuery(pageNumber: Int, pageSize: Int): Future[List[Tweet]] = {
    collTweet.find(query).options(QueryOpts((pageNumber - 1) * pageSize, pageSize)).cursor[Tweet].collect[List](pageSize)
  }

}

object DBServices extends DBServices
