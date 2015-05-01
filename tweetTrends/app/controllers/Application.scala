package controllers

import java.util.concurrent.TimeoutException
import scala.concurrent.Future
import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import com.knoldus.utils.ConstantUtil

object Application extends Application {
  val dbService = DBServices
  val birdTweet = BirdTweet
}

trait Application extends Controller {
  this: Controller =>

  val dbService: DBServices
  val birdTweet: BirdTweet

  def trending: Action[AnyContent] = Action.async {
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
    res.map { r => Ok(views.html.showData(r)) }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
}
