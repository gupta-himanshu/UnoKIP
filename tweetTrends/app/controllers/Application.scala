package controllers

import org.apache.spark.streaming.twitter.TwitterUtils

import com.knoldus.tweetstreaming.SparkStreaming
import com.knoldus.tweetstreaming.Tweet
import com.knoldus.tweetstreaming.TwitterClient
import com.knoldus.twittertrends.BirdTweet

import models.DBCrud
import models.FindDoc
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

object Application extends Application {
  val findDoc = FindDoc
}

trait Application extends Controller{
  this: Controller =>
  
  val findDoc: FindDoc
  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def streamstart = Action {
    val stream = SparkStreaming
    val a = stream.startStream("ss", "local[2]")
//    val db = connector("localhost", "rmongo", "rmongo", "pass")
    val dbcrud = DBCrud
    val twitterauth = new TwitterClient().tweetCredantials()
    val tweetDstream = TwitterUtils.createStream(a, Option(twitterauth.getAuthorization))
    val tweets = tweetDstream.filter { x => x.getUser.getLang == "en" }.map { x => Tweet(x.getId, x.getSource, x.getText, x.isRetweet(), x.getUser.getName, x.getUser.getScreenName, x.getUser.getURL, x.getUser.getId, x.getUser.getLang) }
    //  tweets.foreachRDD { x => x.foreach { x => dbcrud.insert(x) } }
    tweets.saveAsTextFiles("../spark services/tweets/tweets")
    //    val s=new BirdTweet() 
    //    s.hastag(a.sparkContext)
    a.start()
    Ok("start streaming")
  }

  def trending = Action {
    val s = new BirdTweet()
    s.trending()
    Ok("trend")

  }

  def show: Action[AnyContent] = Action.async {
    val show = findDoc.findWholeDoc()
    Logger.info("Data arriving is ::::::::::: " + show)
    show.map { x =>
      Ok(views.html.showData(x))
    }
  }

  def chart: Action[AnyContent] = Action {
    Ok("chart")
  }
}
