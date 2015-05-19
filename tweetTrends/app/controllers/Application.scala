package controllers

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.twittertrends.BirdTweet
import models.MyWebSocketActor
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.libs.F.Function
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import utils.JsonParserUtility._
import utils.JsonParserUtility.tuple2
import scala.concurrent.Promise
import play.api.libs.ws.WS

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
   * @return This is to render showData page.
   */
  def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())
  }

  //WebSocket With future 
  def socket(start:String): WebSocket[String, JsValue] = WebSocket.acceptWithActor[String, JsValue] { request =>
    out =>
      val trends = dbTrendService.removeTrends()
      val date = new DateTime()
      val formatter = DateTimeFormat.forPattern("dd/MM/yyyy kk:mm:ss");
      val endDate = formatter.print(date)
      val startDate = formatter.parseDateTime(start)
      val end = formatter.parseDateTime(endDate)
      val res=WS.url("http://localhost:8001/trends?start="+startDate.getMillis+"&end="+end.getMillis).get()
     // val res1 = birdTweet.trending1(startDate.getMillis,end.getMillis) 
      val jsonData: JsValue = Await.result(res.map { r => r.json}, 5 second)
      MyWebSocketActor.props(out, jsonData)
  }

  def datepicker: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }

  def datepick = Action {
    Ok(views.html.datepicker())
  }
  
  def startstream=Action{
    val homePage = WS.url("http://localhost:8001/startstream").get();
    Ok("stream strat")
  }
}
