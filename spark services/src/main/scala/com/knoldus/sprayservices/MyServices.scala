package com.knoldus.sprayservices

import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.knoldus.model.Tweet
import com.knoldus.tweetstreaming.TweetCollect
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
import com.knoldus.model.Trend
import com.knoldus.twittertrends.TopTrends

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

      }
}

class MyServiceActor extends Actor with MyService {
  def actorRefFactory: ActorContext = context
  def receive: Actor.Receive = runRoute(myRoute)
}
