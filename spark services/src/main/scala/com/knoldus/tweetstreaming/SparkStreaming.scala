package com.knoldus.tweetstreaming

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.api.HelpResources.Language
import java.util.Date
import twitter4j.URLEntity
import twitter4j.Status
import twitter4j.Place
import twitter4j.GeoLocation
import twitter4j.UserMentionEntity

case class Tweet(id: Long, date: Date, source: String, content: String, retweet: Boolean, authName: String, username: String, url: String, authId: Long, language: String) //,, longitude: Option[Double],latitude:Option[Double],,,,)

/**
 * @author knoldus
 */

trait SparkStreaming {
  def startStream(appName:String,master:String) = {
    val sparkConf: SparkConf = new SparkConf().setAppName(appName).setMaster(master)
    val sc: SparkContext = new SparkContext(sparkConf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(2))
    val twitterauth = new TwitterClient().tweetCredantials()
    val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization))
    val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x => Tweet(x.getId, x.getCreatedAt, x.getSource, x.getText, x.isRetweet(), x.getUser.getName, x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang) //,Some(x.getGeoLocation.getLatitude))
    }
    tweets.saveAsTextFiles("tweets/tweet")
    ssc.start()
  }
}

object SparkStreaming extends SparkStreaming

