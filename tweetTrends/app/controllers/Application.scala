package controllers

import java.util.concurrent.TimeoutException

import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.utils.ConstantUtil
import models.MyWebSocketActor
import models.WebOut
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import play.api.mvc.WebSocket.FrameFormatter
import utils.JsonParserUtility.tuple2

object Application extends Controller with Application {
  val dbService = DBServices
  val dbTrendService = DBTrendServices
  val birdTweet = BirdTweet
}

/**
 * @author knoldus
 *
 */
trait Application {
  this: Controller =>

  val dbService: DBServices
  val birdTweet: BirdTweet
  val dbTrendService: DBTrendServices

  /**
   * @return ajaxCall is used for fetching data as JSON from mongoDb collection
   * and use it to render chart and table.
   */
  def ajaxCall: Action[AnyContent] = Action.async {
    val trends = dbTrendService.findTrends()
    val pageNum = trends.map { listOfTrends =>
      listOfTrends.headOption match {
        case Some(trend) => trend.pageNum + 1
        case None        => 1
      }
    }
    val tweets = for {
      pgNo <- pageNum
      tweets <- dbService.getChunckOfTweet(pgNo, ConstantUtil.pageSize)
    } yield (tweets, pgNo)
    //if listOfTweets.size > 0 then it will aggregate the trend and tweet collection
    //otherwise it will take trends from tweets.
    val res = tweets.flatMap {
      case (listOfTweets, pgNo) => trends.map { listOfTrends =>
        if (listOfTweets.size > 0) { birdTweet.trending(listOfTweets, listOfTrends, pgNo) }
        else { listOfTrends.map(trend => (trend.hashtag, trend.trend)).sortBy({ case (hashtag, trend) => trend }).reverse }
      }
    }
    res.map { r =>
      Ok(play.api.libs.json.Json.toJson(r))
    }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
  /**
   * This is to render showData template page.
   */
  def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())
  }

  //With Iteratee
  def websocket: WebSocket[String, String] = WebSocket.using[String] { request =>
    val in = Iteratee.ignore[String]
    val out = Enumerator("Hello! Guys")
    (in, out)
  }
  implicit val outEventFormat = Json.format[WebOut]
  implicit val outEventFrameFormatter = FrameFormatter.jsonFrame[WebOut]
  //With future
  def socket: WebSocket[String, WebOut] = WebSocket.acceptWithActor[String, WebOut] { request =>
    out =>
      MyWebSocketActor.props(out)
  }

  def datepicker: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }

  def socketPage: Action[AnyContent] = Action {
    Ok(views.html.socket())
  }
}
