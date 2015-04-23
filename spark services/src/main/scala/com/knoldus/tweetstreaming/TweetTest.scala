package com.knoldus.tweetstreaming

object TweetTest extends App{
  
  val s:SparkStreaming=SparkStreaming
  val a = s.startStream("app name","local[2]")
//  a.start()
}