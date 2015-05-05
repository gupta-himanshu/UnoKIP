package controllers

import java.util.concurrent.TimeoutException
import scala.concurrent.Future
import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.utils.ConstantUtil
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import utils.JsonParserUtility.tuple2

object Application extends Application {
  val dbService = DBServices
  val dbTrendService = DBTrendServices
  val birdTweet = BirdTweet
}

/**
 * @author knoldus
 *
 */
trait Application extends Controller {
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
 * @return This is to render showData page.
 */
def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())
  }
}
