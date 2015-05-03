package com.knoldus.model

import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

/**
 * @author knoldus
 */


//object Models {
case class Tweet(id: Long, source: String, content: String, retweet: Boolean, authName: String,
                 username: String, url: String, authId: Long, language: String)

case class Trends(hashtag:String,trend:Int,pageNum:Int)

object Tweet {
  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]
}
object Trends{
  implicit val reader: BSONDocumentReader[Trends] = Macros.reader[Trends]
  implicit val writer: BSONDocumentWriter[Trends] = Macros.writer[Trends]
}
