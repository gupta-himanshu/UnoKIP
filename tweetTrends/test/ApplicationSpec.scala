import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import models._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.specs2.mock.Mockito
import play.api.mvc.Controller
import controllers.Application
import reactivemongo.bson.BSONDocumentReader
import com.knoldus.tweetstreaming.Tweet
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import reactivemongo.api.Cursor
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends PlaySpecification with Mockito {

  val tweet = Tweet(1223, "ss","ss",true,"ss","ss","ss",1234,"ss")
  
  def getObject = {
    val mockfindDoc: FindDoc = mock[FindDoc]
    object TestObj extends Application with Controller {
      val findDoc: FindDoc = mockfindDoc
    }
    (mockfindDoc, TestObj)
  }

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Your new application is ready.")
    }

    "find collection" in new WithApplication {
      implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
      implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]
      
      
    }
  }
}
