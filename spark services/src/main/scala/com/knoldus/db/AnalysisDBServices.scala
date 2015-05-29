package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import com.knoldus.model.Trend
import com.knoldus.model.Sentiment
import com.knoldus.model.TweetDetails
import com.knoldus.model.OtherAnalysis


trait AnalysisDBServices extends DBConnector {

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

  val collSent = db("sentiment1")
  def insertSentiment(sentiment: Sentiment): Future[Boolean] = {
    collSent.insert(sentiment).map { lastError => lastError.ok }
  }

  def findSentiment(session: String): Future[Option[Sentiment]] = {
    collSent.find(BSONDocument({ "session" -> session })).one[Sentiment]
  }

  def updateSentiment(sentimentDoc: Sentiment): Future[Boolean] = {
    collSent.update(BSONDocument("session" -> sentimentDoc.session), BSONDocument("$set" -> sentimentDoc), multi = false)
      .map(res => res.updatedExisting)
  }

  val collTweets=db("tweets1")
  def insertTweetDetails(details: TweetDetails): Future[Boolean] = {
    collTweets.insert(details).map { lastError => lastError.ok }
  }
  
  def findTweetDetails(session: String): Future[List[TweetDetails]] = {
    collSent.find(BSONDocument({ "session" -> session })).cursor[TweetDetails].collect[List]()
  }
  
  val collHashtag=db("hashtags")
  def insertHashtag(hashtag:OtherAnalysis ): Future[Boolean] = {
    collHashtag.insert(hashtag).map { lastError => lastError.ok }
  }



}

object AnalysisDBServices extends AnalysisDBServices
