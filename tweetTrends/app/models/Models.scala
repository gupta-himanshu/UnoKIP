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

  