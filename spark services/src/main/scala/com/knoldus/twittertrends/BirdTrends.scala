package com.knoldus.twittertrends

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.core.Global
import com.knoldus.db.DBServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.topTrending

trait BirdTweet {
<<<<<<< HEAD
  def trending(tweets: List[Tweet], trend: List[Trends], pageNum: Int): List[(String, Int)] = {
    /*val pageNum=trend.headOption match{
      case None=>0
      case Some(trend)=>trend.pageNum
    }*/
    val dbservices = DBServices
    val createRDDTweet = Global.sc parallelize (tweets)
    val trendsList = trend.map { x => (x.hashtag, x.trend) }
    val trendsRDD = Global.sc.parallelize(trendsList)
    val hashtags = createRDDTweet flatMap { tweet => tweet.content split (" ") } filter { word => word.startsWith("#") }
    val pair = hashtags.map { hashtag => (hashtag, 1) }
    val aggPair = pair.union(trendsRDD)
    val trends = aggPair reduceByKey (_ + _) sortBy ({ case (key, value) => value }, false)
    val topTenTrends = trends take (topTrending) toList;
    dbservices.removeTrends() onComplete {
      case Success(result) => topTenTrends.map({ case (hashtag, trend) => Trends(hashtag, trend,pageNum)}) map { x => dbservices.insertTrends(x) }
      case Failure(e)      =>
    }
    topTenTrends
=======
  def trending(tweets: List[Tweet]): List[(String, Int)] = {
    val createRDD = Global.sc parallelize (tweets)
    val hashtags = createRDD flatMap { tweet => tweet.content split (" ") } filter { word => word.startsWith("#") }
    val pair = hashtags.map { hashtag => (hashtag, 1) } reduceByKey (_ + _)
    val trends = pair sortBy ({ case (key, value) => value }, false)
    val topTenTrends = trends take (topTrending)
    topTenTrends toList
>>>>>>> 1da243a207e1e40e64cfa05550a0e74e35f666de
  }
}

object BirdTweet extends BirdTweet
