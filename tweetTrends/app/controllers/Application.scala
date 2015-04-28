package controllers

import java.util.concurrent.TimeoutException

import com.knoldus.db.Connector
import com.knoldus.db.FindDoc
import com.knoldus.tweetstreaming.Tweet
import com.knoldus.tweetstreaming.Tweet
import com.knoldus.tweetstreaming.TwitterClient
import com.knoldus.twittertrends
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.twittertrends.BirdTweet

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.Controller
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

object Application extends Controller with Application {
  val findDoc = FindDoc
}

trait Application extends Connector {
  this: Controller =>

  val findDoc: FindDoc

  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def chart: Action[AnyContent] = Action {
    Ok(views.html.chart("chart"))
  }

  def trending: Action[AnyContent] = Action.async {
    val show = findDoc.findWholeDoc().collect[List]()
    val res = show.map { x => BirdTweet.trending(x) }
    res.map { r => Ok(views.html.showData(r)) }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
}