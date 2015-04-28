package com.knoldus.twitterstreaming

import org.scalatest.FunSuite
import com.knoldus.tweetstreaming.TwitterClient

/**
 * @author knoldus
 */
class TwitterClientTest extends FunSuite{
    
  test("twitter client test"){
    val twitter=new TwitterClient().tweetCredantials()
    assert(twitter.getClass.toString()==="class twitter4j.TwitterImpl")
  }
}