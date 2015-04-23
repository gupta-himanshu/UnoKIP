package com.knoldus.twitterstreaming

import com.knoldus.tweetstreaming.TweetTest
import com.knoldus.tweetstreaming.SparkStreaming
import org.scalatest.FunSuite

/**
 * @author knoldus
 */
class StreamingTest extends FunSuite {
  test("twitter stream test"){
    val twitter=SparkStreaming.startStream("ss", "local[2]")
    
    assert(twitter.getClass()==="class twitter4j.TwitterImpl")
  }
}