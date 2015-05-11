package models

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala

case class WebOut(name: String)
object MyWebSocketActor {
  def props(out: ActorRef): Props = Props(new MyWebSocketActor(out))
}
class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case msg: String =>
      out ! WebOut(msg)
  }
}
