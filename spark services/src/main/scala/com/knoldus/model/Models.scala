package com.knoldus.model

import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import reactivemongo.bson.BSONDocument
import java.util.Date
import reactivemongo.bson.BSONDateTime
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONHandler
import reactivemongo.bson.BSONBinary
import reactivemongo.bson.Subtype
import reactivemongo.bson.BSONDateTime

/**
 * @author knoldus
 */

// Domain for Tweets
case class Tweet(id: Long, source: String, content: String, retweet: Boolean, authName: String,
                 username: String, url: String, authId: Long, language: String, create: Date, country: Option[String],
                 locationName: Option[String], long: Option[Double], lat: Option[Double])

object Tweet {
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def write(time: DateTime): BSONDateTime = BSONDateTime(time.getMillis)
    def read(bsonDateTime: BSONDateTime): DateTime = new DateTime(bsonDateTime.value)
  }

  implicit object BSONDateTimeHandler1 extends BSONHandler[BSONDateTime, Date] {
    def write(time: Date): BSONDateTime = BSONDateTime(time.getTime)
    def read(bsonDateTime: BSONDateTime): Date = new Date(bsonDateTime.value)
  }
  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]
}

// Domain for Top Trends
case class Trend(hashtag: String, trends: Int)

object Trend {
  implicit val reader: BSONDocumentReader[Trend] = Macros.reader[Trend]
  implicit val writer: BSONDocumentWriter[Trend] = Macros.writer[Trend]
}

case class Sentiment(  session: String,positiveCount: Option[Int], negativeCount: Option[Int],
                     neutralCount: Option[Int])

object Sentiment {
  implicit val reader: BSONDocumentReader[Sentiment] = Macros.reader[Sentiment]
  implicit val writer: BSONDocumentWriter[Sentiment] = Macros.writer[Sentiment]
}

case class OtherAnalysis(  session: String,hashtag:Array[String],contributor:String)

object OtherAnalysis {
  implicit val reader: BSONDocumentReader[OtherAnalysis] = Macros.reader[OtherAnalysis]
  implicit val writer: BSONDocumentWriter[OtherAnalysis] = Macros.writer[OtherAnalysis]
}


case class TweetDetails(tweetId:Long,content:String,hashtags:Array[String],username:String,session:String,sentiment:Double)

object TweetDetails {
  implicit val reader: BSONDocumentReader[TweetDetails] = Macros.reader[TweetDetails]
  implicit val writer: BSONDocumentWriter[TweetDetails] = Macros.writer[TweetDetails]
}

