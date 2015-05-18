package com.knoldus.tweetstreaming

import com.typesafe.config.ConfigFactory
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext
import com.knoldus.db.DBServices
import com.knoldus.core.Global.sc
import com.knoldus.utils.ConstantUtil.streamInterval
import org.apache.spark.streaming.Seconds
import com.knoldus.model.Tweet

/**
 * @author knoldus
 */
object Trends  {

  val ssc: StreamingContext = new StreamingContext(sc, Seconds(streamInterval))
  val client = new TwitterClient()
  val config=ConfigFactory.load()
  //val filter=config.getString("twitter.handles").split(" ")
  val twitterauth = new TwitterClient().tweetCredantials()
  val dbService = DBServices
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization))
  val tweets = tweetDstream.map{trends=> trends.isFavorited()}
  
  def start()=ssc.start()
  def stop()=ssc.stop()
}
