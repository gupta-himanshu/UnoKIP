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
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import org.apache.spark.rdd.RDD

/**
 * @author knoldus
 */

//This is main object which is collect tweets from twitter stream and perist in mongoDB
object TweetCollect {

  val ssc: StreamingContext = new StreamingContext(sc, Seconds(streamInterval))
  val client = new TwitterClient()
  val config = ConfigFactory.load()
  val filter = config.getString("twitter.handles").split(" ")
  val twitterauth = new TwitterClient().tweetCredantials()
  val dbService = DBServices
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization))
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
  //  tweets.foreachRDD { x => x.foreach { x => println(x) } }
  val res = tweets.foreachRDD(saveTweets(_))
  tweets.print()
  def start() = ssc.start()
  def stop() = ssc.stop()
  private def saveTweets(tweets: RDD[Tweet]) = {
    val collectedTweets = tweets.collect()
    collectedTweets.foreach(a => dbService.insert(a))
  }
}
