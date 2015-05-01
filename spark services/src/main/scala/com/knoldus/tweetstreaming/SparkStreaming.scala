package com.knoldus.tweetstreaming

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import com.knoldus.core.Global.sc
import com.knoldus.db.DBServices
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.streamInterval

/**
 * @author knoldus
 */

private object TweetCollect extends App {
  val ssc: StreamingContext = new StreamingContext(sc, Seconds(streamInterval))
  val client = new TwitterClient()
  val twitterauth = new TwitterClient().tweetCredantials()
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization), Seq("scala"))
  val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x =>
    Tweet(x.getId, x.getSource, x.getText, x.isRetweet(), x.getUser.getName,
      x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang)
  }
  tweets.foreachRDD { x => x.foreach { x => DBServices.insert(x) } }
  ssc.start()
}
