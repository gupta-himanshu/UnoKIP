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
import utils.JsonParserUtility.otherAnalysisWrite
import utils.JsonParserUtility.sentimentWrite
import utils.SentimentAnalysisUtility
import models.Sentiment
import models.TweetDetails
import models.OtherAnalysis

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
  
  private val DEFAULT_SENTIMENT = Sentiment("session", None, None, None)
  val dbApi: DBApi
  val sentimentUtility: SentimentAnalysisUtility
  val routes: RoutesFunction
  
  def trending: Action[AnyContent] = Action {
    Ok(views.html.showData())

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

  

  def testAnalysis(topicId: String) = Action.async {
    val futureofHandlers = dbApi.findHandler(topicId)
    val sentiments =
      for {
        handlers <- futureofHandlers
        res = handlers match {
          case Some(data) => data.handler.map { handler =>
            dbApi.sentimentQuery(handler)
          }
          case None => Nil
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
      Ok(Json.toJson(x))
    }.recover { case s => Ok("not") }
  }

  private def getPostiveCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).positiveCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  def otherAnalysis(topicId: String) = Action.async {
    val futureofHandlers = dbApi.findHandler(topicId)
    futureofHandlers.map { x => println(x) }
    val tweetDetails =
      for {
        handlers <- futureofHandlers
        res = handlers match {
          case Some(data) => data.handler.map(handler => dbApi.findTweetDetails(handler))
          case None       => Nil
        }
      } yield (res)

    val listOfTweets = tweetDetails.flatMap(detail => Future.sequence(detail)).map(listOfTweetDetails => listOfTweetDetails.flatten)
    val contributors = listOfTweets.map { listOfTweet => listOfTweet.map { tweetDetails => tweetDetails.username } }
    val contributorPair = contributors.map { x => x.map { x => (x, 1) } }
    val contributorSum = contributorPair.map(x => x.groupBy(_._1).map(data => data._1 -> data._2.map(_._2).sum))
    val contributorList = contributorSum.map(x => x.toList)
    val top5contributor = contributorList.map(x => x.sortBy(_._2).reverse.take(5).map(x => x._1))
    val hashtags = listOfTweets.map { listOfTweet => listOfTweet.flatMap { tweetDetails => tweetDetails.hashtags } }
    val hashtagspair = hashtags.map { hashtags => hashtags.map { hashtag => (hashtag, 1) } }
    val hashtagsSum = hashtagspair.map(hashtags => hashtags.groupBy(_._1).map(data => data._1 -> data._2.map(_._2).sum))
    val hashtagsList = hashtagsSum.map(hashtagMap => hashtagMap.toList)
    val top5hashtags=hashtagsList.map(hashtag=>hashtag.sortBy(_._2).reverse.take(5).map(x=>x._1))
    val tweets = listOfTweets.map { x => x.map { x => x.content } }
    val otherAnalysis=for{
      tophashtags<-top5hashtags
      topcontributor<-top5contributor
      tweets<-tweets
      otherAnalysis=OtherAnalysis(tweets,tophashtags,topcontributor)
    }yield(otherAnalysis)
    otherAnalysis.map {x=>Ok(Json.toJson(x))}.recover { case s => Ok("not") }
  }
}
