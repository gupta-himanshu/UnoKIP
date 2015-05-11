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

trait BirdTweet {
  
  val dbTrendService: DBTrendServices

  /**
   * Function to calculate to 10 trends
   * @param tweets
   * @param trend
   * @param pageNum
   * @return List[(String,Int)]
   */
  def trending(tweets: List[Tweet], trend: List[Trends], pageNum: Int): List[(String, Int)] = {
    val createRDDTweet = Global.sc parallelize (tweets)
    val trendsList = trend.map { trends => (trends.hashtag, trends.trend) }
    val trendsRDD = Global.sc.parallelize(trendsList)
    val hashtags = createRDDTweet.flatMap { tweet => tweet.content split (" ") }.filter { word => word.startsWith("#") }
    val pair = hashtags.map((_, 1))
    val aggPair = pair.union(trendsRDD)
    val trends = aggPair reduceByKey (_ + _) sortBy ({ case (_, value) => value }, false)
    val topTenTrends = trends take (topTrending) toList

    dbTrendService.removeTrends() onComplete {
      case Success(result) => topTenTrends.map({ case (hashtag, trend) => Trends(hashtag, trend, pageNum) }).
        map { trends => dbTrendService.insertTrends(trends) }
      case Failure(e) =>
    }
    topTenTrends
  }
}

object BirdTweet extends BirdTweet {
  val dbTrendService = DBTrendServices
}
