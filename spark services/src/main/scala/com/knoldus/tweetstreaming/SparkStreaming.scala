
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils

import com.knoldus.tweetstreaming.TwitterClient

case class Tweet(tweet: String)

/**
 * @author knoldus
 */

object SparkStreaming extends App {

  val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")
  val sc: SparkContext = new SparkContext(sparkConf)
  val ssc: StreamingContext = new StreamingContext(sc, Seconds(2))
  val twitterauth = new TwitterClient().tweetCredantials()
  val filter = Array("en")
  val tweetDstream = TwitterUtils.createStream(ssc, Option(twitterauth.getAuthorization), filter)
  val tweets = tweetDstream.map { x => Tweet(x.getText) }
  tweets.saveAsTextFiles("tweets/tweet")
  ssc.start()
 
}