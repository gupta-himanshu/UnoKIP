package com.knoldus.tweetstreaming

object TweetTest extends App{
  
  val s:SparkStreaming=SparkStreaming
  s.startStream("app name","local[2]")
  
}