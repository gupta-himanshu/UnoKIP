package com.knoldus.tweetstreaming

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import com.knoldus.db.DBStore
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import twitter4j.HashtagEntity

case class Tweet(id: Long, source: String, content: String, retweet: Boolean, authName: String, username: String, url: String, authId: Long, language: String)

/**
 * @author knoldus
 */

private object TweetCollect extends App {
  
  def startStream(appname: String, master: String) = {
    val sparkConf: SparkConf = new SparkConf().setAppName("tweet_collect").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(sparkConf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(10))
    implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
    implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]
    val client = new TwitterClient()
    val twitterauth = new TwitterClient().tweetCredantials()
    val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization))
    val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x => Tweet(x.getId, x.getSource, x.getText, x.isRetweet(), x.getUser.getName, x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang) }
    tweets.foreachRDD { x => x.foreach { x => DBStore.insert(x) } }
    ssc.start()
  }
  startStream("tweet_collect", "local[2]")
}