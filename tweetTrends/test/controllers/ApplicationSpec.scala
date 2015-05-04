package controllers

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.specs2.mock.Mockito
import scala.concurrent.Future
import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import play.api.mvc.Results
import play.api.test._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import play.api.test.FakeApplication
import play.api.test.PlaySpecification
import play.api.test.WithApplication
import com.knoldus.model.Trends
import com.knoldus.utils.ConstantUtil
import com.knoldus.model.Tweet
import twitter4j.Trend
import java.util.concurrent.TimeoutException
import com.knoldus.db.DBTrendServices

/**
 *
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito {

  val mockDbService: DBServices = mock[DBServices]
  val mockBirdTweet: BirdTweet = mock[BirdTweet]
  val mockDbTrend: DBTrendServices = mock[DBTrendServices]

  object TestObj extends Application {
    val dbService: DBServices = mockDbService
    val birdTweet: BirdTweet = mockBirdTweet
    val dbTrendService: DBTrendServices = mockDbTrend
  }

  "Application" should {
    "trending" in new WithApplication(new FakeApplication) {
      val result = await(route(FakeRequest()).get)
      result.header.status must equalTo(OK)
    }

    "Ajax Call" in new WithApplication(new FakeApplication) {
      mockDbTrend.findTrends() returns Future(List(Trends("#source content", 5, 1)))
      mockDbService.getChunckOfTweet(2, ConstantUtil.pageSize) returns Future(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")))
      mockBirdTweet.trending(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")), List(Trends("#source content", 5, 1)), 2) returns List(("", 1))
      
      val result = await(TestObj.ajaxCall.apply(FakeRequest()))
      result.header.status must equalTo(200)
    }
  }
}
