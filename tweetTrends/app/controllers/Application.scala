package controllers

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.knoldus.model.Sentiment
import models.MyWebSocketActor
import models.Trend
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import services.DBTrendServices
import utils.JsonParserUtility.JsonParser
import utils.JsonParserUtility.tuple2
import play.api.libs.ws.WS
import play.api.Logger
import scala.util.Failure
import scala.util.Success

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

  def sessions: Action[AnyContent] = Action {

    Ok(views.html.sessions(List()))
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

  def testAnalysis(topidId: String) = Action {
    val futureofHandlers = dbTrendServices.findHandler(topidId)
    //Logger.info(futureofHandlers.toString())

    val sentiments =
      for {
        handlers <- futureofHandlers
        handlerList = handlers.map { handlerObj => handlerObj.handler.map { handler => handler } }.flatten
        sentiments <- Future(handlerList map { handler => dbTrendServices.sentimentQuery(handler) })
      } yield (sentiments)
    //Logger.info(" >>>>>>>>>>>>>>>>>>>>" + sentiments.toString())

    
    val topicIds =
      for {
        handlers <- futureofHandlers
        //handlerList = handlers.map { handlerObj => handlerObj.topicId.map { handler => handler } }
      } yield (handlers)
    topicIds.map { x => println(x) }
    val listofSentiment = (sentiments flatMap (sentiment => Future.sequence(sentiment)))

    //implicit val senJsonParser = Json.format[Sentiment]
    val sentimentFuture = listofSentiment.map { valueList => valueList.flatten
    }
    val json = sentimentFuture.map { sentimentList =>
      sentimentList.map { sentimentList =>
        //Logger.info("negative " + sentimentList.negativeCount.toString())
        //Logger.info("positive " + sentimentList.positiveCount.toString())
        //Logger.info("neutral " + sentimentList.neutralCount.toString())
        implicit val sentimentWrite = new Writes[Sentiment] {
          def writes(sentiment: Sentiment) = Json.obj(
            //"tweetId" -> sentiment.tweetId,
            "positiveCount" -> sentiment.positiveCount,
            "negativeCount" -> sentiment.negativeCount,
            "neutralCount" -> sentiment.neutralCount,
            "session" -> sentiment.session,
            "hastags" -> sentiment.hastags,
            "content" -> sentiment.content)
        }
        Json.toJson(sentimentList)
      }
    }

    val jsonData: List[JsValue] = Await.result(json, 1 second)
    Ok(views.html.sessions(jsonData))
    /*val listofSentiment = sentiments flatMap (sentiment => Future.sequence(sentiment))
    listofSentiment.map { x => println(x) }
    val displayData = listofSentiment.map { sentiments =>
      val totalPositiveCount = getPostiveCount(sentiments)
      val totalNegativeCount = getNegativeCount(sentiments)
      val totalNeutralCount = getNeutralCount(sentiments)
      DEFAULT_SENTIMENT.copy(positiveCount = totalPositiveCount, negativeCount = totalNegativeCount,
        neutralCount = totalNeutralCount)
    }*/

    //   val sentiment=handler.map { handler =>  
    //     handler match {
    //       case Some(handler)=>dbTrendServices.sentimentQuery(handler.handler)
    //       case None=>
    //     }}
    /*    displayData.map { x=>
      implicit val sentimentWrite = new Writes[Sentiment] {
      def writes(sentiment:Sentiment) = Json.obj(
        "tweetId" -> sentiment.tweetId,
        "positiveCount" -> sentiment.positiveCount,
        "negativeCount" -> sentiment.negativeCount,
        "neutralCount" -> sentiment.neutralCount,
        "session" -> sentiment.session,
        "hastags" -> sentiment.hastags,
        "content" -> sentiment.content
        )
    }
      Ok(Json.toJson(x))
    }.recover { case s => Ok("not") }*/
    //Ok("Done")
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

  def dummy(data: play.api.libs.json.JsValue): Action[AnyContent] = Action {
    Ok(views.html.dummyGraph(data))
  }

}
