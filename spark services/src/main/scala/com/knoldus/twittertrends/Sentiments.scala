package com.knoldus.twittertrends

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.knoldus.model.Tweet
import org.apache.spark.rdd.RDD
import com.knoldus.core.Global
import com.knoldus.db.DBTrendServices
import com.knoldus.model.Sentiment
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure

trait SentimentAnalysis {
  val dbTrend: DBTrendServices
  def sentimentAnalysis(tweetRDD: RDD[Tweet]) = {
    val positive = Array("happy", "amazing", "good")
    val negative = Array("sad", "bad", "angry")
    //  val positiveRDD = Global.sc.makeRDD(positive)
    //  val negativeRDD = Global.sc.makeRDD(negative)
    //  
    val sentiment = tweetRDD.flatMap { tweet =>
      val words = tweet.content.split(" ")
      val s = words.flatMap { x => positive.map { y => x.contains(y) } }
      val sen = s.filter(x => x)
      val pcount = sen.size
      val n = words.flatMap { x => negative.map { y => x.contains(y) } }
      val nsen = n.filter(x => x)
      val ncount = nsen.size
      val hashtags = words.filter(_.startsWith("#"))
      val session = words.filter(_.startsWith("@"))
      if (pcount > ncount) session.map(handler => Sentiment(tweet.id, Some(1), None, None, handler, hashtags, tweet.content))
      else if (pcount < ncount) session.map(handler => Sentiment(tweet.id, None, Some(1), None, handler, hashtags, tweet.content))
      else session.map(handler => Sentiment(tweet.id, None, None, Some(1), handler, hashtags, tweet.content))
    }.collect
    
    sentiment.foreach { doc =>
      val oldSentiment = dbTrend.findSentiment(doc.session)
      oldSentiment map { result =>
        result match {
          case Some(sentiment) =>
            if (doc.positiveCount.isDefined)
              dbTrend.updateSentiment(sentiment.copy(positiveCount = Some(sentiment.positiveCount.getOrElse(0) + 1)))
            if (doc.negativeCount.isDefined)
              dbTrend.updateSentiment(sentiment.copy(negativeCount = Some(sentiment.negativeCount.getOrElse(0) + 1)))
            if (doc.neutralCount.isDefined)
              dbTrend.updateSentiment(sentiment.copy(neutralCount = Some(sentiment.neutralCount.getOrElse(0) + 1)))
          case None => dbTrend.insertSentiment(doc)
        }
      }
    }
  }
}
object SentimentAnalysis extends SentimentAnalysis {
  val dbTrend: DBTrendServices = DBTrendServices
}