package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Tweet
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONDocument
import java.util.Date
import org.joda.time.format.ISODateTimeFormat
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDateTime

/**
 * @author knoldus
 *
 */
trait DBIngestion extends DBConnector {

  val filter = BSONDocument()
  val query = BSONDocument()
  val collTweet = db("twitterHandle")

  /**
   * Function to insert Tweet object into mongoDB collection
   * @param tweet
   * @return lastErro
   */
  def insert(tweet: Tweet): Future[Boolean] = {
    collTweet.insert(tweet).map { lastError =>
      lastError.ok
    }
  }
}

object DBIngestion extends DBIngestion
