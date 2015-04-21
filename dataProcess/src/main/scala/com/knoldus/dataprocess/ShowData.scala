package com.knoldus.dataprocess


import com.knoldus.dbconnection.Connector
import com.knoldus.dbconnection.DBCrud
import reactivemongo.bson.Macros
import com.knoldus.dbcrud.FindDoc
import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument
import com.knoldus.streaming.Tweet
/**
 * @author knoldus
 */
object TweetOut extends App with Connector {
  implicit val read = Macros.reader[Tweet]
  implicit val write = Macros.writer[Tweet]
  val db = connector("localhost", "rmongo", "rmongo", "pass")
  val findDoc = new FindDoc[Tweet](db, "table1")
  val enumerator =findDoc.findWholeDoc().enumerate()
  val processDocuments: Iteratee[Tweet, Unit] =
    Iteratee.foreach { tweet =>
      println(s"$tweet")
    }
  enumerator.apply(processDocuments)
}