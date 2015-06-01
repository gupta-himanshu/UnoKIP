package sprayutility

import play.api.Play.current
import play.api.libs.ws.WS
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import play.api.libs.ws.WSResponse

/**
 * @author knoldus
 */

trait RoutesFunction {
  val config=ConfigFactory.load()
  val startstream=config.getString("spray.routes.startstream")
  val stopstream=config.getString("spray.routes.stopstream")

  def startStream():Future[WSResponse]={
     WS.url(startstream).get();
  }

  def stopStream():Future[WSResponse]={
     WS.url(stopstream).get();
  }
}

object RoutesFunction extends RoutesFunction
