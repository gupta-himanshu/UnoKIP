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

  def trending: Action[AnyContent] = Action.async {
    
    implicit val userFormat = Json.format[Tweet]
    implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
    val tweets = dbService.findWholeDoc()
    val res = tweets.map(x => birdTweet.trending(x))
    res.map { r => 
      val(cat, catValue) = r.unzip
      val js = Json.obj("cat" -> cat, "value" -> catValue)
      Ok(views.html.showData(Json.obj("cat" -> cat, "value" -> catValue),r))
    }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }

  def convert: Action[AnyContent] = Action.async {
    implicit val userFormat = Json.format[Tweet]
 		implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
 
    val tweets = dbService.findWholeDoc()
    val res = tweets.map(x => birdTweet.trending(x))
    res.map { r => 
      val(cat, catValue) = r.unzip
      Ok(views.html.convert(Json.obj("cat" -> cat, "value" -> catValue)))
    }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }

  }

}
