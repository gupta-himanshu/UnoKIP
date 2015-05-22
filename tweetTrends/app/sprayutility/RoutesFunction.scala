package sprayutility

import play.api.Play.current
import play.api.libs.ws.WS
/**
 * @author knoldus
 */
object RoutesFunction {
  def startStream()={
     WS.url("http://192.168.1.14:8001/startstream").get();
  }
}