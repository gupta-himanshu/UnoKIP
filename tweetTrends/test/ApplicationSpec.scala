import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.specs2.mock.Mockito
import scala.concurrent.Future
import com.knoldus.db.DBServices
import com.knoldus.model.Models.Tweet
import com.knoldus.twittertrends.BirdTweet
import play.api.mvc.Results
import controllers.Application
import play.api.test.FakeRequest
import play.api.test.FakeApplication
import play.api.test.PlaySpecification
import play.api.test.WithApplication
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito {

  val mockDbService: DBServices = mock[DBServices]
  val mockBirdTweet:BirdTweet=mock[BirdTweet]
  
  object TestObj extends Application {
    val dbService: DBServices = mockDbService
    val birdTweet:BirdTweet=mockBirdTweet
  }

  "Application" should {
    "trending" in new WithApplication(new FakeApplication) {
     val tweets= mockDbService.findWholeDoc() returns Future.successful(List(Tweet(1223, "ss", "ss", true, "ss", "ss", "ss", 1234, "ss")))
      val trend = mockBirdTweet.trending(tweets) returns List(("#worldcup", 100))
       //val result = await(TestObj.trending.apply(FakeRequest(GET, "/")))
        val home = route(FakeRequest(GET, "/")).get
      home must equalTo(OK)
    }
  }
}
