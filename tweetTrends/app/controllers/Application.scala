package controllers

import java.util.concurrent.TimeoutException

import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller

object Application extends Application {
  val dbService = DBServices
  val birdTweet = BirdTweet
}

trait Application extends Controller{
  this: Controller =>

  val dbService: DBServices
  val birdTweet: BirdTweet

  def trending: Action[AnyContent] = Action.async {
    val tweets = dbService.findWholeDoc()
    val res = tweets.map { x => birdTweet.trending(x) }
    res.map { r => Ok(views.html.showData(r)) }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
}
