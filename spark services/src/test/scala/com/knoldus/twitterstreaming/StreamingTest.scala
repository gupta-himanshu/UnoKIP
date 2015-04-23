package com.knoldus.twitterstreaming

import com.knoldus.tweetstreaming.TweetTest
import com.knoldus.tweetstreaming.SparkStreaming
import org.scalatest.FunSuite
import com.knoldus.db.Connector
import com.knoldus.db.DBCrud

/**
 * @author knoldus
 */
class StreamingTest extends FunSuite with Connector{
  test("twitter stream test"){
      val db = connector("localhost", "rmongo", "rmongo", "pass")
  val dbcrud = new DBCrud(db, "table1")
    val twitter=SparkStreaming.startStream("ss", "local[2]")
    assert(twitter.getClass().toString()==="class org.apache.spark.streaming.StreamingContext")
  }
}