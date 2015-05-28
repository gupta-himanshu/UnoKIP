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

/**
 * @author knoldus
 */

case class WordList(word: String, value: Double)

trait AffinSentiments {

  val dbTrend: AnalysisDBServices

  def sentimentAnalysis(tweetRDD: RDD[Tweet], listOfSentiments: List[WordList]) = {

    val details = tweetRDD.flatMap { tweet =>
      val sen = tweet.content.split(" ").toList.flatMap(word => listOfSentiments.map { x =>
        if (word == x.word) {
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

    details.foreach { x => dbTrend.insertHashtag(OtherAnalysis(x.session, x.hashtags,x.username)) }

    val sentiment = details.flatMap { tweetDetail =>

      val tweetsDetails = dbTrend.findTweetDetails(tweetDetail.session)
      //      val top5hashtags = for {
      //        details <- tweetsDetails
      //        val hashtagspair = details.flatMap { x => x.hashtags.map { x => (x, 1) } }
      //        val hashpairRDD = Global.sc.parallelize(hashtagspair)
      //        val top5hastags = hashpairRDD.reduceByKey((x, y) => x + y).sortBy({ case (_, value) => value }, false) take (5)
      //      } yield (top5hastags)

      val words = tweetDetail.content.split(" ")
      val session = words.filter(_.startsWith("@"))
      if (tweetDetail.sentiment > 0) {
        println("happy")
        
        session.map { handler => Sentiment(handler, Some(1), None, None) }
      } else if (0 > tweetDetail.sentiment) {
        println("negative")
        session.map(handler => Sentiment(handler, None, Some(1), None))
      } else {
        println("neutral")
        session.map(handler => Sentiment(handler, None, None, Some(1)))
      }
    }

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

object AffinSentiments extends AffinSentiments {
  val dbTrend: AnalysisDBServices = AnalysisDBServices
}