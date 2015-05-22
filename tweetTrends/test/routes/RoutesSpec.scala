package routes

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test.PlaySpecification
import play.api.test.WithApplication
import play.api.test.FakeRequest
import play.api.test.FakeApplication

@RunWith(classOf[JUnitRunner])
class RoutesSpec extends PlaySpecification{

  "Routes" should {
    
    "respond to the / route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/"))
      status(result) must equalTo(OK)
      contentType(result) must beSome("text/html")
      charset(result) must beSome("utf-8")
      contentAsString(result) must contain("Twitter Trends")
    }
    
    "respond to the /socket route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/socket"))
      status(result) must equalTo(400)
      contentType(result) must beSome("text/html")
      charset(result) must beSome("utf-8")
    }
    
    "respond to the /start route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/start"))
      status(result) must equalTo(OK)
      contentType(result) must beSome("text/plain")
      charset(result) must beSome("utf-8")
    }
    
    "respond to the /sessions route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/sessions"))
      status(result) must equalTo(OK)
      contentType(result) must beSome("text/html")
      charset(result) must beSome("utf-8")
      contentAsString(result) must contain("Sessions")
    }
  }
}