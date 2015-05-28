package services

import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.MongoDriver
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros
import models.Trend._
import models.Trend
import scala.concurrent.Future
import models.Handlers._
import reactivemongo.bson.BSON
import com.knoldus.model.Sentiment
import models.Handlers
import play.api.Logger
import scala.util.Failure
import scala.util.Success

/**
 * @author knoldus
 */

trait DBTrendServices {

  val config = ConfigFactory.load()
  val host: String = config.getString("db.hostName")
  val dbName: String = config.getString("db.dbName")
  val username: String = config.getString("db.username")
  val pass: String = config.getString("db.password")

  val driver = new MongoDriver
  val connection = driver.connection(List(host))
  val db = connection(dbName)

  val trendColl = db("trends")

  def getTrends = {
    trendColl.find(BSONDocument()).cursor[Trend].collect[List]()
  }

  val collHandler = db("handles")

  def findHandler(topicId: String): Future[Option[Handlers]] = {
      println("finding Handler......")
    collHandler.find(BSONDocument({ "topicId" -> topicId })).one[Handlers]
  }
  
    
val sentColl=db("sentiment")
    def sentimentQuery(handler:String): Future[Option[Sentiment]]={
      sentColl.find(BSONDocument("session"->handler)).one[Sentiment]
    }
}

object DBTrendServices extends DBTrendServices