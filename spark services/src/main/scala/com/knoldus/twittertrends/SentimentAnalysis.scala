package com.knoldus.twittertrends

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.knoldus.model.Tweet
import org.apache.spark.rdd.RDD
import com.knoldus.core.Global
import com.knoldus.model.Sentiment
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import com.knoldus.db.AnalysisDBServices

trait SentimentAnalysis {
  val dbAnalysis:AnalysisDBServices
  def sentimentAnalysis(tweetRDD: RDD[Tweet]):Unit = {
    val positive = Array("happy", "amazing", "good")
    val negative = Array("sad", "bad", "angry")
    val sentiment = tweetRDD.flatMap { tweet =>
      val words = tweet.content.split(" ")
      val listOfIsPositive = words.flatMap { wordInTweet => positive.map { wordInPositive => wordInTweet.contains(wordInPositive) } }
      val listOfPositives = listOfIsPositive.filter(isPositive => isPositive)
      val noOfPositives = listOfPositives.size
      val listOfIsNegative = words.flatMap { wordInTweet => negative.map { wordInNegative => wordInTweet.contains(wordInNegative) } }
      val listOfNegatives = listOfIsNegative.filter(isNegative => isNegative)
      val noOfNegative = listOfIsNegative.size
      val hashtags = words.filter(_.startsWith("#"))
      val session = words.filter(_.startsWith("@"))

      if (noOfPositives > noOfNegative) {session.map(handler => Sentiment(tweet.id, Some(1), None, None, handler, hashtags, tweet.content))}
      else if (noOfPositives < noOfNegative) {session.map(handler => Sentiment(tweet.id, None, Some(1), None, handler, hashtags, tweet.content))}
      else {session.map(handler => Sentiment(tweet.id, None, None, Some(1), handler, hashtags, tweet.content))}
    }.collect

    sentiment.foreach { doc =>
      val oldSentiment = dbAnalysis.findSentiment(doc.session)
      oldSentiment map { result =>
        result match {
          case Some(sentiment) =>
            if (doc.positiveCount.isDefined)
              {dbAnalysis.updateSentiment(sentiment.copy(positiveCount = Some(sentiment.positiveCount.getOrElse(0) + 1)))}
            if (doc.negativeCount.isDefined)
              {dbAnalysis.updateSentiment(sentiment.copy(negativeCount = Some(sentiment.negativeCount.getOrElse(0) + 1)))}
            if (doc.neutralCount.isDefined)
              {dbAnalysis.updateSentiment(sentiment.copy(neutralCount = Some(sentiment.neutralCount.getOrElse(0) + 1)))}
          case None => dbAnalysis.insertSentiment(doc)
        }
      }
    }
  }
}
object SentimentAnalysis extends SentimentAnalysis {
  val dbAnalysis: AnalysisDBServices = AnalysisDBServices
}
