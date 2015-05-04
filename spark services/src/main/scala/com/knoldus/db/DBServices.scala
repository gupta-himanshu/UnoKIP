package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.api.QueryOpts

trait DBServices extends DBConnector {

  //  Logger.getLogger("org").setLevel(Level.OFF)

  val collTweet = db("tweets")
  def insert(tweet: Tweet): Future[Boolean] = {
    collTweet.insert(tweet).map { lastError =>
      lastError.ok
    }
  }

  /**
   * @return 
   */
  def findWholeDoc(): Future[List[Tweet]] = {
    val filter = BSONDocument()
    collTweet.find(BSONDocument(), filter).cursor[Tweet].collect[List]()
  }

  val collTrends = db("trend")
  
  def findTrends(): Future[List[Trends]] = {
    val filter = BSONDocument()
    collTrends.find(BSONDocument(), filter).cursor[Trends].collect[List]()
  }

  def insertTrends(trends: Trends): Future[Boolean] = {
    collTrends.insert(trends).map { lastError => lastError.ok }
  }

  def removeTrends() =collTrends.remove(BSONDocument())

  def filterQuery(pageNumber: Int, pageSize: Int): Future[List[Tweet]] = {
    collTweet.find(BSONDocument()).options(QueryOpts((pageNumber - 1) * pageSize, pageSize)).cursor[Tweet].collect[List](pageSize)
  }
}

object DBServices extends DBServices
