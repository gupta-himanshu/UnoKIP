package controllers;

import scala.concurrent.Future
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import com.knoldus.db.DBServices
import com.knoldus.db.DBTrendServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import com.knoldus.twittertrends.BirdTweet
import com.knoldus.utils.ConstantUtil
import play.api.test.FakeApplication
import play.api.test.FakeRequest
import play.api.test.PlaySpecification
import play.api.test.WithApplication
import org.specs2.runner.JUnitRunner

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
      val home = await(route(FakeRequest(GET, "/")).get)
      home.header.status must equalTo(OK)
    }

    "Ajax Call when we get chunck of tweets" in new WithApplication(new FakeApplication) {
      mockDbTrend.findTrends() returns Future(List(Trends("#source content", 5, 1)))
      mockDbService.getChunckOfTweet(2, ConstantUtil.pageSize) returns Future(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")))
      mockBirdTweet.trending(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")), List(Trends("#source content", 5, 1)), 2) returns List(("", 1))
      val result = await(TestObj.ajaxCall.apply(FakeRequest(GET, "/ajaxcall")))
      result.header.status must equalTo(200)
    }
    
   
    "Ajax Call when we get Nil tweet list" in new WithApplication(new FakeApplication) {
      mockDbTrend.findTrends() returns Future(List(Trends("#source content", 5, 1)))
      mockDbService.getChunckOfTweet(2, ConstantUtil.pageSize) returns Future(List())
      val result = await(TestObj.ajaxCall.apply(FakeRequest(GET, "/ajaxcall")))
      result.header.status must equalTo(200)
    }
    
    
    "Ajax Call when we get Nil trends list" in new WithApplication(new FakeApplication) {
      mockDbTrend.findTrends() returns Future(Nil)
      mockDbService.getChunckOfTweet(1, ConstantUtil.pageSize) returns Future(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")))
      mockBirdTweet.trending(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")), Nil, 1) returns List(("", 1))
      val result = await(TestObj.ajaxCall.apply(FakeRequest(GET, "/ajaxcall")))
      result.header.status must equalTo(200)
    }
    
      "Ajax Call when timeout happen" in new WithApplication(new FakeApplication) {
      mockDbTrend.findTrends() returns Future(Nil)
      mockDbService.getChunckOfTweet(1, ConstantUtil.pageSize) returns Future(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")))
      //mockBirdTweet.trending(List(Tweet(123, "source", "content", true, "authName", "username", "url", 234, "language")), Nil, 1) returns (throw new TimeoutException)
      val result = await(TestObj.ajaxCall.apply(FakeRequest(GET, "/ajaxcall")))
      result.header.status must equalTo(200)
      
    }
  }
}
