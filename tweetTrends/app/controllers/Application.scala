package controllers

import java.util.concurrent.TimeoutException

import scala.concurrent.Future
import scala.concurrent.Future
import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.utils.ConstantUtil
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller

object Application extends Application {
  val dbService = DBServices
  val birdTweet = BirdTweet
}

trait Application extends Controller {
  this: Controller =>

  val dbService: DBServices
  val birdTweet: BirdTweet

  def ajaxCall: Action[AnyContent] = Action.async {
    val trends = dbService.findTrends()
    val pageNum = trends.map { x =>
      x.headOption match {
        case None        => 1
        case Some(trend) => trend.pageNum + 1
      }
    }
    val tweets = for {
      pgNo <- pageNum
      tweets <- dbService.filterQuery(pgNo, ConstantUtil.pageSize)
    } yield (tweets, pgNo)

    val res = tweets.flatMap {
      case (listOfTweets, pgNo) => trends.map { y =>
        if (listOfTweets.size > 0) birdTweet.trending(listOfTweets, y, pgNo)
        else y.map(trend => (trend.hashtag, trend.trend))
      }
    }
    implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
    res.map { r =>
      Ok(play.api.libs.json.Json.toJson(r))
    }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
  
  /**
   * This is to render page.
   */
  def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())
  }
}
