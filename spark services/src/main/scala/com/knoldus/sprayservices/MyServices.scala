package com.knoldus.sprayservices

import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

import com.knoldus.db.DBServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import com.knoldus.tweetstreaming.TweetCollect
import com.knoldus.twittertrends.BirdTweet

import akka.actor.Actor
import play.api.libs.json.Json
import play.api.libs.json.Writes
import spray.http.HttpEntity.apply
import spray.http.HttpResponse
import spray.http.MediaTypes.`application/json`
import spray.http.MediaTypes.`text/html`
import spray.http.StatusCodes.OK
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.routing.Directive.pimpApply
import spray.routing.HttpService
import spray.routing.directives.ParamDefMagnet.apply

trait MyService extends HttpService {

  val myRoute =
    path("index") {
      get {
        entity(as[String]) { json =>
          respondWithMediaType(`text/html`) {
            complete {
              // val result = DBService.insert
              HttpResponse(OK, "Hello Sandeep " + pass)
            }
          }
        }
      }
    } ~
      path("getindex") {
        get {
          parameter('pass) { password =>
            respondWithMediaType(`text/html`) {
              complete {
                val result = DBServices.insert(Tweet(591216001431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>", "#KakakFathir Suka Menghayal ? 67", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en", new Date()))
                result.map { x => HttpResponse(OK, "Hello Sandeep " + x) }

              }
            }
          }
        }
      } ~
            path("startstream") {
        get {
              complete {
                 TweetCollect.start     
                 HttpResponse(OK, "start stream" ) 

              }
            }
          } ~
           path("stopstream") {
        get {
              complete {
                 TweetCollect.stop()    
                 HttpResponse(OK, "stop stream" ) 

              }
            }
          } ~
          path("trends") {
        get {
          parameters('start.as[Long] , 'end.as[Long]) { (start, end)=>
            respondWithMediaType(`application/json`){
              complete {
                
              implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
                 val tweetTrends=BirdTweet.trending1(start, end)
                 tweetTrends.map(s =>HttpResponse(OK, play.api.libs.json.Json.toJson(tweetTrends)))    
              }
            }
          }
          }
          
      }
}

class MyServiceActor extends Actor with MyService {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

