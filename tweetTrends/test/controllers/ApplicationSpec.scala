package controllers;

import scala.concurrent.Future
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.mockito.Mockito._
import org.mockito.Matchers._
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
import play.api.mvc.Result
import models.Handlers
import scala.concurrent.Await
import scala.concurrent.duration._
import models.Sentiment

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
  val sentiment = Sentiment("",Some(1),Some(1),Some(1))

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

    "testAnalysis should be valid" in {
      running(FakeApplication()) {
        when(mockdbApi.findHandler("1")) thenReturns (Future(Option(Handlers("ss",List("s","s")))))
        when(mockdbApi.findTweetDetails("1")) thenReturns (Future(List()))
        when(mocksentimentUtility.getPostiveCount(List(Option(sentiment)))) thenReturns(Some(1))
        when(mocksentimentUtility.getNeutralCount(List(Option(sentiment)))) thenReturns(Some(1))
        when(mocksentimentUtility.getNegativeCount(List(Option(sentiment)))) thenReturns(Some(1))
        val results: Future[Result] = TestObj.testAnalysis("1")(FakeRequest())
        Await.result(results, 1 seconds)
        contentType(results) must beSome("text/plain")
        status(results) mustEqual OK
      }
    }

    "otherAnalysis should be valid" in new WithApplication {
      when(mockdbApi.findHandler("1")) thenReturns (Future(Option(Handlers("ss",List("s","s")))))
      when(mockdbApi.findTweetDetails("1")) thenReturns (Future(List()))
      val results: Future[Result] = TestObj.otherAnalysis("1")(FakeRequest())
      Await.result(results, 1 seconds)
      status(results) mustEqual OK
      contentType(results) must beSome("text/plain")
    }
  }
}
