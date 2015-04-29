package com.knoldus.twittertrends

import com.knoldus.core.Global
import com.knoldus.model.Models.Tweet
import com.knoldus.utils.ConstantUtil.topTrending

trait BirdTweet {
  def trending(tweets: List[Tweet]): Map[String, Int] = {
    val createRDD = Global.sc parallelize (tweets)
    val hashtags = createRDD flatMap { tweet => tweet.content split (" ") } filter { word => word.startsWith("#") }
    val pair = hashtags.map { hashtag => (hashtag, 1) } reduceByKey (_ + _)
    val trends = pair sortBy ({ case (key, value) => value }, false)
    val topTenTrends = trends take (topTrending)
    topTenTrends toMap
  }
}

object BirdTweet extends BirdTweet
