package com.knoldus.sprayservices

import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.db.DBServices
import com.knoldus.model.Tweet
import com.knoldus.sprayservices.RouteRequestHandler.execute
import com.knoldus.tweetstreaming.TweetCollect
import com.knoldus.twittertrends.BirdTweet
import akka.actor.Actor
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Writes
import spray.http.HttpEntity.apply
import spray.http.HttpResponse
import spray.http.MediaTypes.`application/json`
import spray.http.MediaTypes.`text/html`
import spray.http.StatusCodes.OK
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply
import spray.util.LoggingContext
import akka.actor.ActorContext

trait MyService extends HttpService {

  val myRoute =
    path("startstream") {
      get {
        complete {
          TweetCollect.start()
          HttpResponse(OK, "Streaming is Started")
        }
      }
    } ~
      path("stopstream") {
        get {
          complete { TweetCollect.stop(); HttpResponse(OK, "Streaming is Stopped") }
        }
      } ~
      path("trends") {
        get {
          parameters('start.as[Long], 'end.as[Long]) { (start, end) =>
            respondWithMediaType(`application/json`) {
              implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
              val tweetTrends = BirdTweet.trending1(start, end)
              complete {
                tweetTrends.map(s => HttpResponse(OK, Json.toJson(s).toString()))
              }
            }
          }
        }
      }
}

class MyServiceActor extends Actor with MyService {
  def actorRefFactory:ActorContext = context
  def receive:Actor.Receive = runRoute(myRoute)
}
