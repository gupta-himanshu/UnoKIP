package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import com.knoldus.model.Trend
import com.knoldus.model.Handlers
import com.knoldus.model.Sentiment

trait DBTrendServices extends DBConnector {

  val collTrends = db("trends")
  val query = BSONDocument()
  val filter = BSONDocument()

  /**
   * Method to insert Trends object into mongoDB
   * @param trends
   * @return Future[Boolean]
   */
  def insertTrends(trends: Trend): Future[Boolean] = {
    collTrends.insert(trends).map { lastError => lastError.ok }
  }

  /**
   * Fetch the list of Trends object from mongoDB
   * @return List[Trends]
   */
  def findTrends(): Future[List[Trend]] = {
    collTrends.find(query, filter).cursor[Trend].collect[List]()
  }

  /**
   * Remove all documents from MongoDB
   * @return
   */
  def removeTrends(): Future[Boolean] = {
    collTrends.remove(query).map { lastError => lastError.ok }
  }

  val collHandler = db("searchandlers")

  def findHandler(handler: String): Future[List[Handlers]] = {
    collHandler.find(BSONDocument({ "handler" -> handler })).cursor[Handlers].collect[List]()
  }

  val collSent = db("sentiment")
  def insertSentiment(sentiment: Sentiment): Future[Boolean] = {
    collSent.insert(sentiment).map { lastError => lastError.ok }
  }

  def findSentiment(session: String): Future[Option[Sentiment]] = {
    collSent.find(BSONDocument({ "session" -> session })).one[Sentiment]
  }

  def updateSentiment(sentimentDoc: Sentiment): Future[Boolean] = {
    collSent.update(BSONDocument("tweetId" -> sentimentDoc.tweetId), BSONDocument("$set" -> sentimentDoc), multi = false)
      .map(res => res.updatedExisting)
  }

}

object DBTrendServices extends DBTrendServices
