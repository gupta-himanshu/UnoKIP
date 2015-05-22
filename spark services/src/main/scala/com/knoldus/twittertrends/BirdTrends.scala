package com.knoldus.twittertrends

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.core.Global
import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.topTrending
import org.slf4j.Logger
import org.apache.log4j.Logger
import scala.concurrent.Future

trait BirdTweet {

  val dbServices: DBServices

  def trending1(startTime: Long, endTime: Long): Future[List[(String,Int)]] = {
    val tweets = dbServices.getTimeOfTweet(startTime, endTime)
    val topTrends = for {
      tweet <- tweets
      val tweetRDD = Global.sc parallelize (tweet)
      val hashtags = tweetRDD.flatMap { tweet => tweet.content split (" ") }.filter { word => word.startsWith("#") }
      val pair = hashtags.map((_, 1))
      val trends = pair reduceByKey (_ + _) sortBy ({ case (_, value) => value }, false)
      val topTenTrends = trends take (topTrending) toList
    } yield (topTenTrends)
    topTrends
  }

}

object BirdTweet extends BirdTweet {
  val dbServices = DBServices
}
