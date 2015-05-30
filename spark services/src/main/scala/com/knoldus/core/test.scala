package com.knoldus.core

import java.util.Date
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.knoldus.model.Tweet
import com.knoldus.core.Global
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import com.knoldus.twittertrends._
import scala.collection.JavaConversions._
import com.knoldus.model.WordList

/**
 * @author knoldus
 */

object TestSentiment extends App {

  def readSentiment() = {
    val reader = new CSVReader(new FileReader("/home/knoldus/sentiment project/tweet_collect/twitter_sentiment_list.csv"), ',')
    val csv = reader.readAll()
    csv.map { x => Senti(x(0), x(1).toDouble, x(2).toDouble) }.toList

  }

  def readCSV() = {
    val reader = new CSVReader(new FileReader("/home/knoldus/sentiment project/cluster_design/UnoKIP/spark services/affin.csv"), '\t')
    val csv = reader.readAll()
    csv.map { x => WordList(x(0), x(1).toDouble) }.toList

  }

  val s = readSentiment()
  val words = readCSV()
  val tweets = List(Tweet(591216111431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
    "feeling happy @odersky #hope #hope #excited #excited #excited #excited #scaladays", false, "Somen||48", "prem", "http://jkt48.com", 2880640850L, "en",
    new Date, Some("new york"), Some("new york"), Some(23554221), Some(43545423)))

  val tweetRDD = Global.sc.parallelize(tweets)

  AffinSentiments.sentimentAnalysis(tweetRDD, words)

  //  SentiementCSV.sentimentAnalysis(tweetRDD, s)
  //SentimentAnalysis.sentimentAnalysis(tweetRDD)

}