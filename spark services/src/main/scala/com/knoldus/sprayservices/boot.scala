package com.knoldus.sprayservices

/**
 * @author knoldus
 */

import akka.actor.{ ActorSystem, Props }
import akka.event.Logging
import akka.io.IO
import spray.can.Http

object Boot extends App {
  try {
    // we need an ActorSystem to host our application in
    implicit val actorSystem = ActorSystem("spray-api-service")

    // create and start our service actor
    val service = actorSystem.actorOf(Props[MyServiceActor], "spray-service")

    // start a new HTTP server on port 8080 with our service actor as the handler
    IO(Http) ! Http.Bind(service, interface = "localhost", port = 8001)
  } catch {
    case ex: Throwable => ex.printStackTrace()
  }
}