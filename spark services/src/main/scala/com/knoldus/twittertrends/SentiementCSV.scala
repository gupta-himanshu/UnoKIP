/*package com.knoldus.twittertrends

import java.io.StringReader
import scala.math._
import au.com.bytecode.opencsv.CSVReader
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.tools.nsc.Global
import java.io.FileReader
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import com.knoldus.model.Tweet
import org.apache.spark.rdd.RDD
import com.knoldus.model.Sentiment
import com.knoldus.db.AnalysisDBServices

*//**
 * @author knoldus
 *//*

case class Senti(word: String, positive: Double, negative: Double)

trait SentiementCSV {
  val dbTrend: AnalysisDBServices
  def sentimentAnalysis(tweetRDD: RDD[Tweet], listOfSentiments: List[Senti]) = {
    
    val sentiment = tweetRDD.flatMap { tweet =>
      val sen = tweet.content.split(" ").toList.flatMap(word => listOfSentiments.map { x =>
        if (word==x.word) {
          println(word+","+x.word+","+x.positive+","+x.negative)
          (x.positive, x.negative)
        } else (0D, 0D)
      })
      val happy_log_probs = sen.map(_._1).foldRight(0D)(_ + _)
      val sad_log_probs = sen.map(_._2).foldRight(0D)(_ + _)
      val words = tweet.content.split(" ")
      val hashtags = words.filter(_.startsWith("#"))
      val session = words.filter(_.startsWith("@"))
            println(happy_log_probs + ", " + sad_log_probs)
      val prob_happy = 1 / (exp(sad_log_probs - happy_log_probs) + 1)
      val prob_sad = 1 - prob_happy

      println(prob_happy + ", " + prob_sad)
      if (prob_happy > prob_sad) {
        println("happy")
        session.map(handler => Sentiment(tweet.id, Some(1), None, None, handler, hashtags, tweet.content))
      } else if (prob_sad > prob_happy) {
        println("negative")
        session.map(handler => Sentiment(tweet.id, None, Some(1), None, handler, hashtags, tweet.content))
      } else {
        println("neutral")
        session.map(handler => Sentiment(tweet.id, None, None, Some(1), handler, hashtags, tweet.content))
      }
    }.collect()

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

object SentiementCSV extends SentiementCSV {
  val dbTrend = AnalysisDBServices
}
*/