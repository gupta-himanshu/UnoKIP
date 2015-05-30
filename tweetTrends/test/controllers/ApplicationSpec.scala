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
import play.api.mvc.Controller
import org.specs2.execute.Results

/**
 *
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito with Results {

  val mockDbService: DBServices = mock[DBServices]
  val mockBirdTweet: BirdTweet = mock[BirdTweet]
  val mockDbTrend: DBTrendServices = mock[DBTrendServices]

  object TestObj extends Application with Controller {
    val dbService: DBServices = mockDbService
    val birdTweet: BirdTweet = mockBirdTweet
    val dbTrendService: DBTrendServices = mockDbTrend
  }

  "Application" should {    
    "trending should be valid" in new WithApplication{
          val result = TestObj.trending()(FakeRequest())        
          status(result) must equalTo(200)
          contentType(result) must beSome("text/html")
    }
    
    "sessions should be valid" in new WithApplication{
          val result = TestObj.sessions()(FakeRequest())        
          status(result) must equalTo(200)
          contentType(result) must beSome("text/html")
    }
    
    /*"startStream should be valid" in {
          val result = TestObj.startstream()(FakeRequest())        
          status(result) must equalTo(200)
    }*/
  }
}
