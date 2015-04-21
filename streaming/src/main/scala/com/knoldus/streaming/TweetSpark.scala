package com.knoldus.streaming

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import com.knoldus.dbconnection.Connector
import reactivemongo.bson.Macros
import com.knoldus.dbconnection.DBCrud

case class Tweet(tweet: String)

class SparkStore extends Connector with App{

  implicit val read = Macros.reader[Tweet]
  implicit val write = Macros.writer[Tweet]
  val db = connector("localhost", "rmongo", "rmongo", "pass")

  val dbcrud = new DBCrud[Tweet](db, "table1")
  val conf = new SparkConf().setAppName("myStream").setMaster("local[4]")
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(2))
  val client = new TwitterClient()
  val tweetauth = client.start()
  val inputDstream = TwitterUtils.createStream(ssc, Option(tweetauth.getAuthorization))
  val statuses = inputDstream.map { x => x.getText }
  val lines = statuses.flatMap { x => x.split("\n") }
  val words = lines.flatMap { x => x.split(" ") }
  val hastag = words.filter { x => x.startsWith("#") }.map { x => Tweet(x) }
  
  hastag.foreachRDD{ tweetRDD =>
    tweetRDD.foreach { x =>
      dbcrud.insert(x)
    }
  }  
  def start =  ssc.start()
  def stop =   ssc.awaitTermination()
}
