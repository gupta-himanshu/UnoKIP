//package com.knoldus.tweetstreaming
//
//import com.knoldus.db.Connector
//import com.knoldus.db.DBCrud
//import org.apache.spark.streaming.twitter.TwitterUtils
//
//object TweetTest extends App with Connector{
//  val db = connector("localhost", "rmongo", "rmongo", "pass")
//  val dbcrud =DBCrud
//  val s: SparkStreaming = SparkStreaming
//  val a = s.startStream("app name", "local[2]")
//
//}