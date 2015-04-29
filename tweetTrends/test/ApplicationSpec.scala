import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import models._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.specs2.mock
import org.specs2.mock.Mockito
import play.api.mvc.Controller
import controllers.Application
import reactivemongo.bson.BSONDocumentReader
import com.knoldus.tweetstreaming.Tweet
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import reactivemongo.api.Cursor
import org.specs2.execute.Results
import akka.util.Timeout
import scala.concurrent.duration._

/**
 *
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito {

  //override implicit def defaultAwaitTimeout: Timeout = 20.seconds

  val tweet = List(Tweet(1223, "ss", "ss", true, "ss", "ss", "ss", 1234, "ss"), Tweet(1223, "ss", "ss", true, "ss", "ss", "ss", 1234, "ss"))

  val mockfindDoc: FindDoc = mock[FindDoc]

  object TestObj extends Application {
    val findDoc: FindDoc = mockfindDoc
  }

  "Application" should {
    "find all collection" in  new WithApplication(new FakeApplication) {

      mockfindDoc.findWholeDoc()(any[BSONDocumentReader[Tweet]], any[BSONDocumentWriter[Tweet]]) returns Future.successful(tweet)

      //when(mockfindDoc.findWholeDoc()) thenReturn( Future.successful(tweet))

      val result = await(TestObj.show().apply(FakeRequest(GET, "/show")))
      result.header.status must equalTo(OK)

    }
  }
}
