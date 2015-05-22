package com.knoldus.sprayservices

/**
 * @author knoldus
 */

import akka.actor.{ ActorSystem, Props }
import akka.event.Logging
import akka.io.IO
import spray.can.Http
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration.DurationInt

object Boot extends App {
  try {
    val config=ConfigFactory.load()
    val spray_interface_ip=config.getString("spray.interface-ip")
    val spray_port=config.getString("spray.port").toInt
    // we need an ActorSystem to host our application in
    implicit val actorSystem = ActorSystem("spray-api-service")
    implicit val timeout = 30 seconds

    // create and start our service actor
    val service = actorSystem.actorOf(Props[MyServiceActor], "spray-service")

    // start a new HTTP server on port 8080 with our service actor as the handler
    IO(Http) ! Http.Bind(service, interface = spray_interface_ip, port = spray_port)
  } catch {
    case ex: Throwable => ex.printStackTrace()
  }
}
