package models

import akka.actor._
import play.api.libs.json._
import play.api.mvc.WebSocket.FrameFormatter

case class WebOut(name: String)
object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}
class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      out ! WebOut(msg)
  }
}
