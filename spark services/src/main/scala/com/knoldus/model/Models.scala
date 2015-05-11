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
/**
 * @author knoldus
 */

// Domain for Tweets
case class Tweet(id: Long, source: String, content: String, retweet: Boolean, authName: String,
                 username: String, url: String, authId: Long, language: String, create: Date)

object Tweet {
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def write(time: DateTime) = BSONDateTime(time.getMillis)
    def read(bsonDateTime: BSONDateTime) = new DateTime(bsonDateTime.value)
  }
  
  implicit object BSONDateTimeHandler1 extends BSONHandler[BSONDateTime, Date] {
    def write(time: Date) = BSONDateTime(time.getTime)
    def read(bsonDateTime: BSONDateTime) = new Date(bsonDateTime.value)
  }
  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]
}

// Domain for Top Trends
case class Trends(hashtag: String, trend: Int, pageNum: Int)

object Trends {
  implicit val reader: BSONDocumentReader[Trends] = Macros.reader[Trends]
  implicit val writer: BSONDocumentWriter[Trends] = Macros.writer[Trends]
}
