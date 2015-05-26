package com.knoldus.sprayservices

import java.util.Date

import com.knoldus.core.Global
import com.knoldus.model.Tweet
import com.knoldus.twittertrends.SentimentAnalysis

/**
 * @author knoldus
 */
object TestSentiment extends App{
  
  val tweets=List(Tweet(591216111431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
      "i m soo happy", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en",
      new Date, Some("new york"), Some("new york"), Some(23554221), Some(43545423)))
 
  val tweetRDD=Global.sc.parallelize(tweets)
  
  SentimentAnalysis.sentimentAnalysis(tweetRDD)
}