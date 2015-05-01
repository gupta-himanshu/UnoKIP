package controllers

import java.util.concurrent.TimeoutException
import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.libs.json.Json
import play.api.libs.json.Writes
import scala.concurrent.Future
import com.knoldus.model.Models.Tweet

object Application extends Application {
  val dbService = DBServices
  val birdTweet = BirdTweet

}

trait Application extends Controller {
  case class User(name: String, age: Int)
  val dbService: DBServices
  val birdTweet: BirdTweet

 /**
 * This is for ajax call
 */
  def ajaxCall: Action[AnyContent] = Action.async {
    implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
    val tweets = dbService.findWholeDoc()
    val res = tweets.map(x => birdTweet.trending(x))
    res.map { r =>
      Ok(play.api.libs.json.Json.toJson(r))
    }
  }
/**
 * This is to render page.
 */
  def trending: Action[AnyContent] = Action {
      Ok(views.html.showData())
  }
}
