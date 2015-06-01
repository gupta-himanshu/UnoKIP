package controllers;

import scala.concurrent.Future
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import play.api.test.FakeApplication
import play.api.test.FakeRequest
import play.api.test.PlaySpecification
import play.api.test.WithApplication
import org.specs2.runner.JUnitRunner
import play.api.mvc.Controller
import org.specs2.execute.Results
import sprayutility.RoutesFunction
import services.DBApi
import utils.SentimentAnalysisUtility

/**
 *
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito with Results {

  val mockdbApi: DBApi = mock[DBApi]
  val mocksentimentUtility: SentimentAnalysisUtility = mock[SentimentAnalysisUtility]
  val mockroutes: RoutesFunction = mock[RoutesFunction]

  object TestObj extends Application with Controller {
    val dbApi: services.DBApi = mockdbApi
    val sentimentUtility: utils.SentimentAnalysisUtility = mocksentimentUtility
    val routes: sprayutility.RoutesFunction = mockroutes
  }

  "Application" should {
    "sessions should be valid" in new WithApplication {
      val result = TestObj.sessions()(FakeRequest())
      status(result) must equalTo(200)
      contentType(result) must beSome("text/html")
    }
  }
}
