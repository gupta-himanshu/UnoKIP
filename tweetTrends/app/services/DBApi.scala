package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.typesafe.config.ConfigFactory
import models.Handlers
import models.Sentiment
import models.TweetDetails
import reactivemongo.api.MongoDriver
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.Producer.nameValue2Producer

/**
 * @author knoldus
 */

trait DBApi {

  val config = ConfigFactory.load()
  val host: String = config.getString("db.hostName")
  val dbName: String = config.getString("db.dbName")
  val username: String = config.getString("db.username")
  val pass: String = config.getString("db.password")

  val driver = new MongoDriver
  val connection = driver.connection(List(host))
  val db = connection(dbName)
  val trendColl = db("trends")
  val collHandler = db("handles")
  val sentColl = db("sentiment")
  val collTweet = db("tweets")


  def findHandler(topicId: String): Future[Option[Handlers]] = {
    collHandler.find(BSONDocument("topicId" -> topicId)).one[Handlers]
  }

  def sentimentQuery(handler: String): Future[Option[Sentiment]] = {
    sentColl.find(BSONDocument("session" -> handler)).one[Sentiment]
  }

  def findTweetDetails(session: String): Future[List[TweetDetails]] = {
    collTweet.find(BSONDocument("session" -> session)).cursor[TweetDetails].collect[List]()
  }
}

object DBApi extends DBApi
