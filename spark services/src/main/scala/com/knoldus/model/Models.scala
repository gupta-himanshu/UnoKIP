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
                 username: String, url: String, authId: Long, language: String, create: Date,country:Option[String],
                 locationName:Option[String],long:Option[Double],lat:Option[Double])

object Tweet {
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def write(time: DateTime):BSONDateTime = BSONDateTime(time.getMillis)
    def read(bsonDateTime: BSONDateTime):DateTime = new DateTime(bsonDateTime.value)
  }

  implicit object BSONDateTimeHandler1 extends BSONHandler[BSONDateTime, Date] {
    def write(time: Date):BSONDateTime = BSONDateTime(time.getTime)
    def read(bsonDateTime: BSONDateTime):Date = new Date(bsonDateTime.value)
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


case class Handlers(topicId: String, handler: String)

object Handlers {
  implicit val reader: BSONDocumentReader[Handlers] = Macros.reader[Handlers]
  implicit val writer: BSONDocumentWriter[Handlers] = Macros.writer[Handlers]
}

case class Sentiment(tweetId:Long,positiveCount:Option[Int],negativeCount:Option[Int],neutralCount:Option[Int],session:String,hastags:Array[String],content:String)

object Sentiment {
  implicit val reader: BSONDocumentReader[Sentiment] = Macros.reader[Sentiment]
  implicit val writer: BSONDocumentWriter[Sentiment] = Macros.writer[Sentiment]
}
