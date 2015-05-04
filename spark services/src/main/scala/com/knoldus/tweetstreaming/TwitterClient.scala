package com.knoldus.tweetstreaming

import com.typesafe.config.ConfigFactory
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken

/**
 * @author knoldus
 */

class TwitterClient {

  val config = ConfigFactory.load()
  val CONSUMER_KEY: String = config.getString("twitter4j.oauth.consumerKey")
  val CONSUMER_KEY_SECRET: String = config.getString("twitter4j.oauth.consumerSecret")
  val ACCESS_TOKEN = config.getString("twitter4j.oauth.accessToken")
  val ACCESS_TOKEN_SECRET = config.getString("twitter4j.oauth.accessTokenSecret")

  def tweetCredantials(): Twitter = {
    val twitter: Twitter = new TwitterFactory().getInstance
    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET)
    twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET))
    twitter
  }
}
