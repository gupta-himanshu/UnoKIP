package com.knoldus.tweetstreaming

import org.apache.spark.streaming.Seconds
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import com.knoldus.core.Global.sc
import com.knoldus.db.DBServices
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.streamInterval
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import org.apache.spark.rdd.RDD
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.twittertrends.TopTrends
import com.knoldus.db.DBTrendServices
import scala.util.Success
import scala.util.Failure
import com.knoldus.twittertrends.SentimentAnalysis

//This is main object which is collect tweets from twitter stream and perist in mongoDB
object TweetCollect {

  val ssc: StreamingContext = new StreamingContext(sc, Seconds(streamInterval))
  val client = new TwitterClient()
  val config = ConfigFactory.load()
  val filter = config.getString("twitter.handles").split(" ")
  val twitterauth = new TwitterClient().tweetCredantials()
  val dbService = DBServices
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization), filter)
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
  tweets.foreachRDD(sentimentAnalysis(_))
  def start(): Unit = ssc.start()
  def stop(): Unit = ssc.stop()
  private def saveTweets(tweetRDD: RDD[Tweet]) = {
    val collectedTweets = tweetRDD.collect
    println("No of Actual tweets :::::::::::::::::: " + collectedTweets.size)
    collectedTweets.foreach(dbService.insert(_))
  }
  private def sentimentAnalysis(tweetRDD: RDD[Tweet]) = SentimentAnalysis.sentimentAnalysis(tweetRDD)
  //private def analyzeAndSaveTrends(tweetRDD: RDD[Tweet]) = TopTrends.trending1(tweetRDD)

}
