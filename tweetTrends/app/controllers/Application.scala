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
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.WebSocket
import services.DBApi
import utils.JsonParserUtility.tuple2
import play.api.libs.ws.WS
import play.api.Logger
import scala.util.Failure
import scala.util.Success
import sprayutility.RoutesFunction
import play.api.libs.ws.WS
import play.api.Logger
import utils.SentimentAnalysisUtility
import models.Sentiment
import models.TweetDetails

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

  def datepick: Action[AnyContent] = Action {
    Ok(views.html.datepicker())
  }

  def sessions: Action[AnyContent] = Action {
    val json = "{'positiveCount':0,'negativeCount':0,'neutralCount':0}"
    Ok(views.html.sessions(Json.toJson(json)))
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

  /*case class Sentiment(tweetId: Long, positiveCount: Option[Int], negativeCount: Option[Int],
                       neutralCount: Option[Int], session: String, hastags: Array[String], content: String)*/

  private val DEFAULT_SENTIMENT = Sentiment("session", None, None, None)

  def testAnalysis(topicId: String) = Action.async {
    val futureofHandlers = dbApi.findHandler(topicId)
    val sentiments =
      for {
        handlers <- futureofHandlers
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

    displayData.map { x =>
      implicit val sentimentWrite = new Writes[Sentiment] {
        def writes(sentiment: Sentiment) = Json.obj(
          "positiveCount" -> sentiment.positiveCount,
          "negativeCount" -> sentiment.negativeCount,
          "neutralCount" -> sentiment.neutralCount)
      }
      Ok(Json.toJson(x))
    }.recover { case s => Ok("not") }
  }

  private def getPostiveCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).positiveCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  def getTweetsDetails(topicId: String) = Action.async {
    val futureofHandlers = dbApi.findHandler(topicId)
    val tweetDetails = for {
      handlers <- futureofHandlers
      res = handlers match {
        case Some(data) => data.handler.map { handler =>
          val s = dbApi.findTweetDetails(handler)
          s
        }
        case None => Nil
      }

    } yield (res)
    val futureTweets = tweetDetails flatMap { detail => Future.sequence(detail) }
    val displayData = futureTweets.map { list => 
      list.flatten
    }
    val content = displayData map { list => list.map { tweetDetail => tweetDetail.content } }
    val username = displayData map { list => list.map { tweetDetail => tweetDetail.username } }
    println(username.map { x => x })
    displayData map { content => Ok(content.toString()) }
  }
}
