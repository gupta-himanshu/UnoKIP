package controllers

import scala.concurrent.duration.DurationInt

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils

import com.knoldus.db.Connector
import com.knoldus.tweetstreaming.SparkStreaming
import com.knoldus.tweetstreaming.Tweet
import com.knoldus.tweetstreaming.TwitterClient
import com.knoldus.twittertrends
import com.knoldus.twittertrends.BirdTweet

import models.DBCrud
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros


object Application extends Controller with Connector {

  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  //  def save: Action[AnyContent] = Action.async {
  //    val user = User("Pushpendu", "Purkait", 23)
  // //   val insert = dbcrud.insert(user)
  //   // insert.map { x =>
  //      Ok(x.toString())
  //    }
  //  }
  //
  //  def show: Action[AnyContent] = Action.async {
  //    val show = findDoc.findWholeDoc().collect[List]()
  //    show.map { x =>
  //      Ok(x.toString())
  //    }
  //  }

  def streamstart = Action {
    val stream = SparkStreaming
    val a = stream.startStream("ss", "local[2]")
    val db = connector("localhost", "rmongo", "rmongo", "pass")
    val dbcrud = DBCrud
    val twitterauth = new TwitterClient().tweetCredantials()
    val tweetDstream = TwitterUtils.createStream(a, Option(twitterauth.getAuthorization))
    val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x => Tweet(x.getId, x.getSource, x.getText, x.isRetweet(), x.getUser.getName, x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang) }
    //  tweets.foreachRDD { x => x.foreach { x => dbcrud.insert(x) } }
    tweets.saveAsTextFiles("/home/knoldus/sentiment project/spark services/tweets/tweets")
    //    val s=new BirdTweet() 
    //    s.hastag(a.sparkContext)
    a.start()
    Ok("start streaming")
  }
 
  def trending=Action{
    val s=new BirdTweet()
    s.trending()
    Ok("trend")
  }
}
