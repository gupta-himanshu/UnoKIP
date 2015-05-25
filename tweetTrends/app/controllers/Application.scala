package controllers

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
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
import utils.JsonParserUtility.JsonParser
import utils.JsonParserUtility.tuple2
import play.api.libs.ws.WS
import play.api.Logger
import sprayutility.RoutesFunction
import services.DBTrendServices
import play.api.libs.json.Writes
import services.Trend

/**
 * @author knoldus
 *
 */

object Application extends Application{
  val dbTrendServices=DBTrendServices
  
}

trait Application extends Controller {

  val dbTrendServices:DBTrendServices
  def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())

  }
  //WebSocket With future
  def socket(start: String): WebSocket[String, JsValue] = WebSocket.acceptWithActor[String, JsValue] { request =>
    out =>
      val date = new DateTime()
      val formatter = DateTimeFormat.forPattern("dd/MM/yyyy kk:mm:ss");
      val endDate = formatter.print(date)
      val startDate = formatter.parseDateTime(start)
      val end = formatter.parseDateTime(endDate)
     
      val trend = dbTrendServices.getTrends
      val res=trend.map { x => x.map { x => (x.hashtag,x.trends) }.sortBy(x=>x._2).reverse }
        val res1=res.map { x => Json.toJson(x) }
      val jsonData: JsValue = Await.result(res1, 1 second)
      MyWebSocketActor.props(out, jsonData)
  }

  def datepicker: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }

  def datepick: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }

  def startstream: Action[AnyContent] = Action {
    val homePage = WS.url("http://192.168.1.14:8001/startstream").get();
    Ok("start stream")
  }

  def testTrend() = Action.async {
    val res = dbTrendServices.getTrends
    implicit val trendWrite = new Writes[Trend] {
      def writes(trend: Trend) = Json.obj(
        "hashtag" -> trend.hashtag,
        "trend" -> trend.trends)
    }
    res.map { x => Ok(Json.toJson(x).toString())}.recover{case s=>Ok("not")}
      }
  def insTrend() = Action {
    dbTrendServices.insTrend
    Ok("done")
  }
}
