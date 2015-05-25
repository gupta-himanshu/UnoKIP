package services

import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.MongoDriver
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.Macros

/**
 * @author knoldus
 */

case class Trend(hashtag: String, trends: Int)

trait DBTrendServices {

  val config = ConfigFactory.load()
  val host: String = config.getString("db.hostName")
  val dbName: String = config.getString("db.dbName")
  val username: String = config.getString("db.username")
  val pass: String = config.getString("db.password")

  def insTrend{
    val driver = new MongoDriver
    val connection = driver.connection(List(host))
    val db = connection(dbName)
    val trendColl = db("trends")
    implicit val reader: BSONDocumentReader[Trend] = Macros.reader[Trend]
    implicit val writer: BSONDocumentWriter[Trend] = Macros.writer[Trend]
    val list=trendColl.insert(Trend("ss",4))
  }
  
  def getTrends = {
    val driver = new MongoDriver
    val connection = driver.connection(List(host))
    val db = connection(dbName)
    val trendColl = db("trends")
    implicit val reader: BSONDocumentReader[Trend] = Macros.reader[Trend]
    implicit val writer: BSONDocumentWriter[Trend] = Macros.writer[Trend]
    trendColl.find(BSONDocument()).cursor[Trend].collect[List]()
  }
}

object DBTrendServices extends DBTrendServices