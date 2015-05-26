package controllers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws.WS
import com.knoldus.model.Sentiment
import models.MyWebSocketActor
import models.Trend
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.ws.WS
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.Logger
import play.api.mvc.WebSocket
import services.DBTrendServices
import utils.JsonParserUtility.JsonParser
import utils.JsonParserUtility.tuple2
import scala.concurrent.Future
import play.api.libs.json._
import net.liftweb.json._

/**
 * @author knoldus
 *
 */

object Application extends Application {
  val dbTrendServices = DBTrendServices

}

trait Application extends Controller {

  val dbTrendServices: DBTrendServices
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
      val res = trend.map { x => x.map { x => (x.hashtag, x.trends) }.sortBy(x => x._2).reverse }
      val res1 = res.map { x => Json.toJson(x) }
      val jsonData: JsValue = Await.result(res1, 1 second)
      MyWebSocketActor.props(out, jsonData)
  }

  def datepick: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }
  
  def sessions:Action[AnyContent] = Action{
    Ok(views.html.sessions())
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
    res.map { x => Ok(Json.toJson(x).toString()) }.recover { case s => Ok("not") }
  }

  /*case class Sentiment(tweetId: Long, positiveCount: Option[Int], negativeCount: Option[Int],
                       neutralCount: Option[Int], session: String, hastags: Array[String], content: String)*/

  private val DEFAULT_SENTIMENT = Sentiment(0L, None, None, None, "", Array(""), "")

  def testAnalysis(topidId: String) = Action.async {
    val handler = dbTrendServices.findHandler(topidId)

    val sentiments =
      for {
        x <- handler
        res = x match {
          case Some(data) => data.handler.map { handler =>
            Logger.info(">>>>>>>>>>>>>>>>>>>>>>>" + handler)
            dbTrendServices.sentimentQuery(handler)
          }
          case None => {
            Logger.info(">>>>>>>>>>>>>>>>>>>>>>> Got None")
            dbTrendServices.sentimentQuery("1")
           List(Future(None)) 
          }
          
        }
      } yield (res)
    val listofSentiment = sentiments flatMap (sentiment => Future.sequence(sentiment))
    listofSentiment.map { x => println(x) }
    val displayData = listofSentiment.map { sentiments =>
      val totalPositiveCount = getPostiveCount(sentiments)
      val totalNegativeCount = getNegativeCount(sentiments)
      val totalNeutralCount = getNeutralCount(sentiments)
      DEFAULT_SENTIMENT.copy(positiveCount = totalPositiveCount, negativeCount = totalNegativeCount,
        neutralCount = totalNeutralCount)
    }

    //   val sentiment=handler.map { handler =>  
    //     handler match {
    //       case Some(handler)=>dbTrendServices.sentimentQuery(handler.handler)
    //       case None=>
    //     }}
    displayData.map { x=>
      Ok(x.toString())
    }.recover { case s => Ok("not") }

  }

  private def getPostiveCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).positiveCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  private def getNegativeCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).negativeCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  private def getNeutralCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).neutralCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }
  
  def dummy(data:String):Action[AnyContent] = Action{
    Ok(views.html.dummyGraph(data))
  } 

}
