package com.knoldus.twittertrends

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.core.Global
import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil.topTrending
import org.slf4j.Logger
import org.apache.log4j.Logger
import scala.concurrent.Future
import com.knoldus.model.Trend
import scala.util.Failure
import scala.util.Success
import scala.util.Failure
import org.apache.spark.rdd.RDD

trait TopTrends {
  val dbServices: DBServices
  val dbTrendServices: DBTrendServices

  def trending1(newTweets: RDD[Tweet]): Future[List[Trend]] = {

    val oldTrends = dbTrendServices.findTrends()
    val topTrends = for {
      oldTopTrends <- oldTrends
      val oldTrendsRDD = Global.sc.parallelize(oldTopTrends)
      val tweet = newTweets.map { x => x.content }
      
      val hashtags = tweet.flatMap { x => x.split(" ") } filter { x => x.startsWith("#") }
      val pair = hashtags.map((_, 1))
      val sortedOldTrends = oldTrendsRDD sortBy ({ trend => trend.trends }, false) map (trend => (trend.hashtag, trend.trends))
      val unitedTrends = pair union (sortedOldTrends) reduceByKey (_ + _) sortBy ({ case (_, value) => value }, false)
      val topTenTrends = unitedTrends take (topTrending) map (topTrend => Trend(topTrend._1, topTrend._2)) toList
     
    } yield (topTenTrends)
    oldTrends.onComplete {
      case Success(x) =>
        dbTrendServices.removeTrends()
      case Failure(err) =>
    }
    topTrends.map { x => x.map { x => dbTrendServices.insertTrends(x) } }

    topTrends
  }
}

object TopTrends extends TopTrends {
  val dbServices = DBServices
  val dbTrendServices = DBTrendServices
}
