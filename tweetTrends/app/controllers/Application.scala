package controllers

import java.util.concurrent.TimeoutException

import scala.concurrent.Future

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.twittertrends.BirdTweet

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
   * @return ajaxCall is used for fetching data as json from mongoDb collection
   * and use it to render chart and table.
   */
  def ajaxCall(start: String): Action[AnyContent] = Action.async {
    val trends = dbTrendService.removeTrends()
    val date = new DateTime()
    val formatter = DateTimeFormat.forPattern("dd/MM/yyyy kk:mm:ss");
    val endDate = formatter.print(date)
    println(endDate)
    val startDate = formatter.parseDateTime(start)
    val end = formatter.parseDateTime(endDate)
    val tweets = dbService.getTimeOfTweet(startDate.getMillis, end.getMillis)
    tweets.map { x => println(x) }
    val res = tweets.map { x => birdTweet.trending(x) }
    res.map(x => println(x))
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

  def datepick = Action {
    Ok(views.html.datepicker())
  }
}
