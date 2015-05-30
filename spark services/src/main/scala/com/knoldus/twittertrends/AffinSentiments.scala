package com.knoldus.twittertrends

import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.knoldus.model.Tweet
import com.knoldus.core.Global
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import com.knoldus.twittertrends._
import scala.collection.JavaConversions._
import org.apache.spark.rdd.RDD
import com.knoldus.model.Sentiment
import com.knoldus.db.AnalysisDBServices
import com.knoldus.model.TweetDetails
import com.knoldus.model.OtherAnalysis
import scala.concurrent.impl.Future
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import com.knoldus.model.WordList

/**
 * @author knoldus
 */

trait AffinSentiments {

  val dbTrend: AnalysisDBServices

  def sentimentAnalysis(tweetRDD: RDD[Tweet], listOfSentiments: List[WordList]) = {

    val details = tweetRDD.flatMap { tweet =>
      val sen = tweet.content.split(" ").toList.flatMap(word => listOfSentiments.map { x =>
        if (x.word.split(" ").exists(word.contains)) {
          println(word + "," + x.word + "," + x.value)
          (x.value)
        } else (0D)
      })
      val value = sen.foldRight(0D)(_ + _)
      val words = tweet.content.split(" ")
      val hashtags = words.filter(_.startsWith("#"))
      val session = words.filter(_.startsWith("@"))
      session.map(handler => TweetDetails(tweet.id, tweet.content, hashtags, tweet.username, handler, value))
    }.collect()

    details.foreach { x => dbTrend.insertTweetDetails(x) }

    val sentiment = details.map { tweetDetail =>
      if (tweetDetail.sentiment > 0) {
        println("happy")
        Sentiment(tweetDetail.session, Some(1), None, None)
      } else if (0 > tweetDetail.sentiment) {
        println("negative")
        Sentiment(tweetDetail.session, None, Some(1), None)
      } else {
        println("neutral")
        Sentiment(tweetDetail.session, None, None, Some(1))
      }
    }

    val groupedSentiments = sentiment.groupBy(_.session).map {
      case (session, data) =>
        session -> Sentiment(session, Some(data.map(_.positiveCount.getOrElse(0)).sum),
          Some(data.map(_.negativeCount.getOrElse(0)).sum),
          Some(data.map(_.neutralCount.getOrElse(0)).sum))
    }
    
    val listOfSentiment = groupedSentiments.values.toList

    listOfSentiment.foreach { doc =>
      println(doc.session)
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

object AffinSentiments extends AffinSentiments {
  val dbTrend: AnalysisDBServices = AnalysisDBServices
}