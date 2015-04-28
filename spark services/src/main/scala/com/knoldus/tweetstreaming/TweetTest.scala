package com.knoldus.tweetstreaming

import com.knoldus.db.Connector
import com.knoldus.db.DBCrud
import org.apache.spark.streaming.twitter.TwitterUtils

object TweetTest extends App with Connector{
  val db = connector("localhost", "rmongo", "rmongo", "pass")
  val dbcrud = new DBCrud(db, "table1")
  val s: SparkStreaming = SparkStreaming
  val a = s.startStream("app name", "local[2]")
  val twitterauth = new TwitterClient().tweetCredantials()
  val tweetDstream = TwitterUtils.createStream(a, Option(twitterauth.getAuthorization))
  val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x => Tweet(x.getId, x.getSource, x.getText, x.isRetweet(), x.getUser.getName, x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang) }
 // tweets.foreachRDD { x => x.foreach { x => dbcrud.insert(x) } }
  tweets.saveAsTextFiles("tweets/tweets")
  a.start()
}