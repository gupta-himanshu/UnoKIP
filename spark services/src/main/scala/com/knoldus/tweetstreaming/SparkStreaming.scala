package com.knoldus.tweetstreaming

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils

import com.knoldus.core.Global.sc
import com.knoldus.db.DBIngestion
import com.knoldus.model.Tweet
import com.knoldus.twittertrends.TopTrends
import com.knoldus.utils.ConstantUtil.streamInterval
import com.typesafe.config.ConfigFactory

//This is main object which is collect tweets from twitter stream and perist in mongoDB
trait TweetCollect {
  val ssc: StreamingContext = new StreamingContext(sc, Seconds(streamInterval))
  val client = new TwitterClient()
  val config = ConfigFactory.load()
  val filter = config.getString("twitter.handles").split(" ")
  val twitterauth = new TwitterClient().tweetCredantials()
  val dbIngestion:DBIngestion
  val topTrends:TopTrends
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization),filter)
  val tweets = tweetDstream.filter { status => status.getUser.getLang == "en" }.map { status =>
    Tweet(status.getId, status.getSource, status.getText, status.isRetweet(), status.getUser.getName, status.getUser.getScreenName, status.getUser.getURL,
      status.getUser.getId, status.getUser.getLang, status.getCreatedAt,
      Option(status.getPlace) match {
        case Some(place) => Some(place.getCountry)
        case None        => None
      },
      Option(status.getPlace) match {
        case Some(name) => Some(name.getName)
        case None       => None
      },
      Option(status.getGeoLocation) match {
        case Some(long) => Some(long.getLongitude)
        case None       => None
      },
      Option(status.getGeoLocation) match {
        case Some(lat) => Some(lat.getLatitude)
        case None      => None
      })
  }
  tweets.foreachRDD(saveTweets(_))
  //tweets.foreachRDD(analyzeAndSaveTrends(_))
  //tweets.foreachRDD(sentimentAnalysis(_))
  def start(): Unit = ssc.start()
  def stop(): Unit = ssc.stop()
  private def saveTweets(tweetRDD: RDD[Tweet]) = {
    val collectedTweets = tweetRDD.collect
    collectedTweets.foreach(dbIngestion.insert(_))
  }
  //private def sentimentAnalysis(tweetRDD: RDD[Tweet]) = SentimentAnalysis.sentimentAnalysis(tweetRDD)
  private def analyzeAndSaveTrends(tweetRDD: RDD[Tweet]) = topTrends.trending1(tweetRDD)
}

object TweetCollect extends TweetCollect{
  val dbIngestion=DBIngestion
  val topTrends=TopTrends
}
