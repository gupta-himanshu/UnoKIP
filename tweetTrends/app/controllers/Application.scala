import com.knoldus.tweetstreaming.SparkStreaming
import com.knoldus.tweetstreaming.TwitterClient
import reactivemongo.bson.BSONDocumentReader
import com.knoldus.twittertrends.BirdTweet
import org.apache.spark.streaming.twitter.TwitterUtils
import com.knoldus.tweetstreaming.Tweet
import reactivemongo.bson.BSONDocumentWriter
import com.knoldus.db.DBCrud
import play.api.mvc.Controller
import com.knoldus.db.Connector
import models.FindDoc
import play.api.mvc.Action
import reactivemongo.bson.Macros
import play.api.mvc.AnyContent

object Application extends Controller  with Application {
  val findDoc = FindDoc
}

trait Application extends Connector {
  this: Controller =>
 
  val findDoc: FindDoc
 
  implicit val reader: BSONDocumentReader[Tweet] = Macros.reader[Tweet]
  implicit val writer: BSONDocumentWriter[Tweet] = Macros.writer[Tweet]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def trending = Action {
    val s = new BirdTweet()
    s.trending()
    Ok("trend")

  }

  def show: Action[AnyContent] = Action.async {
    val show = findDoc.findWholeDoc().collect[List]()
    show.map { x =>
      Ok(views.html.showData(x))
    }
  }

  def chart: Action[AnyContent] = Action {
    Ok(views.html.chart("chart"))
  }
}