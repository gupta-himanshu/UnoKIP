package models

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala
import play.api.libs.json.JsValue

object MyWebSocketActor {
  def props(out: ActorRef,jsonData:JsValue): Props = Props(new MyWebSocketActor(out,jsonData))
}
class MyWebSocketActor(out: ActorRef,jsonData : JsValue) extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case msg: String =>
      out ! jsonData
  }
}
