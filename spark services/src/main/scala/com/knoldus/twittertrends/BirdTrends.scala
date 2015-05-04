package com.knoldus.twittertrends

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.core.Global
import com.knoldus.db.DBServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.topTrending
import com.knoldus.db.DBTrendServices

trait BirdTweet {
  def trending(tweets: List[Tweet], trend: List[Trends], pageNum: Int): List[(String, Int)] = {
    val dbservices = DBServices
    val dbTrendService = DBTrendServices
    val createRDDTweet = Global.sc parallelize (tweets)
    val trendsList = trend.map { x => (x.hashtag, x.trend) }
    val trendsRDD = Global.sc.parallelize(trendsList)
    val hashtags = createRDDTweet flatMap { tweet => tweet.content split (" ") } filter { word => word.startsWith("#") }
    val pair = hashtags.map((_, 1))
    val aggPair = pair.union(trendsRDD)
    val trends = aggPair reduceByKey (_ + _) sortBy ({ case (_, value) => value }, false)
    val topTenTrends = trends take (topTrending) toList;

    dbTrendService.removeTrends() onComplete {
      case Success(result) => topTenTrends.map({ case (hashtag, trend) => Trends(hashtag, trend, pageNum) }) map { x => dbTrendService.insertTrends(x) }
      case Failure(e)      =>
    }
    topTenTrends
  }
}

object BirdTweet extends BirdTweet
