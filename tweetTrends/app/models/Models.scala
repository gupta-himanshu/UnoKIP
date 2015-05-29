package models

import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

/**
 * @author knoldus
 */

case class Trend(hashtag: String, trends: Int)

object Trend {
  implicit val reader: BSONDocumentReader[Trend] = Macros.reader[Trend]
  implicit val writer: BSONDocumentWriter[Trend] = Macros.writer[Trend]
}

case class Handlers(topicId: String, handler: List[String])

object Handlers {
  implicit val reader: BSONDocumentReader[Handlers] = Macros.reader[Handlers]
  implicit val writer: BSONDocumentWriter[Handlers] = Macros.writer[Handlers]
}

case class Sentiment(session:String, positiveCount: Option[Int], negativeCount: Option[Int],
                     neutralCount: Option[Int])

object Sentiment {
  implicit val reader: BSONDocumentReader[Sentiment] = Macros.reader[Sentiment]
  implicit val writer: BSONDocumentWriter[Sentiment] = Macros.writer[Sentiment]
}

case class OtherAnalysis(session: String, hashtag: Array[String], contributor: String)

object OtherAnalysis {
  implicit val reader: BSONDocumentReader[OtherAnalysis] = Macros.reader[OtherAnalysis]
  implicit val writer: BSONDocumentWriter[OtherAnalysis] = Macros.writer[OtherAnalysis]
}

case class TweetDetails(tweetId:Long,content:String,hashtags:Array[String],username:String,session:String,sentiment:Double)

object TweetDetails {
  implicit val reader: BSONDocumentReader[TweetDetails] = Macros.reader[TweetDetails]
  implicit val writer: BSONDocumentWriter[TweetDetails] = Macros.writer[TweetDetails]
}