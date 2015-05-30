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
  val dbTrend: AnalysisDBServices
  def sentimentAnalysis(tweetRDD: RDD[Tweet]):Unit = {
    val positive = Array("happy", "amazing", "good")
    val negative = Array("sad", "bad", "angry")

    val sentiment = tweetRDD.flatMap { tweet =>

      val words = tweet.content.split(" ")
      val listForPositives = words.flatMap { word => positive.map { positiveWord => word.contains(positiveWord) } }
      val listOfPositives = listForPositives.filter(isPositive => isPositive)
      val noOfPositives = listOfPositives.size
      val listForNegatives = words.flatMap { word => negative.map { negativeWord => word.contains(negativeWord) } }
      val listOfNegative = listForNegatives.filter(isNegative => isNegative)
      val noOfNegative = listOfNegative.size
      val hashtags = words.filter(_.startsWith("#"))
      val session = words.filter(_.startsWith("@"))

      if (noOfPositives > noOfNegative) {session.map(handler => Sentiment( handler, Some(1), None, None))}
      else if (noOfPositives < noOfNegative) {session.map(handler => Sentiment( handler, None, Some(1), None))}
      else {session.map(handler => Sentiment( handler, None, None, Some(1)))}
    }.collect

    sentiment.foreach { doc =>
      val oldSentiment = dbTrend.findSentiment(doc.session)
      oldSentiment map { result =>
        result match {
          case Some(sentiment) =>
            if (doc.positiveCount.isDefined)
              {dbTrend.updateSentiment(sentiment.copy(positiveCount = Some(sentiment.positiveCount.getOrElse(0) + 1)))}
            if (doc.negativeCount.isDefined)
              {dbTrend.updateSentiment(sentiment.copy(negativeCount = Some(sentiment.negativeCount.getOrElse(0) + 1)))}
            if (doc.neutralCount.isDefined)
              {dbTrend.updateSentiment(sentiment.copy(neutralCount = Some(sentiment.neutralCount.getOrElse(0) + 1)))}
          case None => dbTrend.insertSentiment(doc)
        }
      }
    }
  }
}
object SentimentAnalysis extends SentimentAnalysis {
  val dbTrend: AnalysisDBServices = AnalysisDBServices
}
