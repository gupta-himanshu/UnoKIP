package controllers

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import models.MyWebSocketActor
import models.Trend
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import services.DBApi
import utils.JsonParserUtility.tuple2
import sprayutility.RoutesFunction
import models.Sentiment
import play.api.libs.ws.WS
import play.api.Logger
import utils.SentimentAnalysisUtility

/**
 * @author knoldus
 *
 */

object Application extends Application {
  val dbApi = DBApi
  val sentimentUtility = SentimentAnalysisUtility
  val routes: RoutesFunction = RoutesFunction
}

trait Application extends Controller {

  val dbApi: DBApi
  val sentimentUtility: SentimentAnalysisUtility
  val routes: RoutesFunction
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

      val trend = dbApi.getTrends
      val res = trend.map { x => x.map { x => (x.hashtag, x.trends) }.sortBy(x => x._2).reverse }
      val res1 = res.map { x => Json.toJson(x) }
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
    val homePage = routes.startStream()
    Ok("start stream")
  }

  def testTrend() = Action.async {
    val res = dbApi.getTrends
    implicit val trendWrite = new Writes[Trend] {
      def writes(trend: Trend) = Json.obj(
        "hashtag" -> trend.hashtag,
        "trend" -> trend.trends)
    }
    res.map { x => Ok(Json.toJson(x).toString()) }.recover { case s => Ok("not") }
  }

  def testAnalysis(topidId: String) = Action.async {
    val handler = dbApi.findHandler(topidId)

    val sentiments =
      for {
        handlers <- handler
        res = handlers match {
          case Some(data) => data.handler.map { handler =>
            dbApi.sentimentQuery(handler)
          }
          case None => List(Future(None))
        }
      } yield (res)
    val listofSentiment = sentiments flatMap (sentiment => Future.sequence(sentiment))
    val displayData = listofSentiment.map { sentiments =>
      val totalPositiveCount = sentimentUtility.getPostiveCount(sentiments)
      val totalNegativeCount = sentimentUtility.getNegativeCount(sentiments)
      val totalNeutralCount = sentimentUtility.getNeutralCount(sentiments)
      sentimentUtility.DEFAULT_SENTIMENT.copy(positiveCount = totalPositiveCount, negativeCount = totalNegativeCount,
        neutralCount = totalNeutralCount)
    }

    displayData.map { x => Ok(x + "?????") }.recover { case s => Ok("not") }
  }

}



