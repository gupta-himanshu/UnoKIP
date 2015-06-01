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
      contentAsString(result) must contain("Sessions")
    }
    "respond to the /test route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/test?topicId=1"))
      status(result) must equalTo(OK)
      charset(result) must beSome("utf-8")
    }
    "respond to the /testHashtag route" in new WithApplication(new FakeApplication) {
      val Some(result) = route(FakeRequest(GET, "/testHashtag?topicId=1"))
      status(result) must equalTo(OK)
      charset(result) must beSome("utf-8")
    }
  }
}