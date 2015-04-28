package models

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Producer.nameValue2Producer
import com.knoldus.tweetstreaming.Tweet


trait DBCrud extends Connector{
  val db = connector("localhost", "rmongo", "username", "Password")
  val coll=db("table1")
  
  def insert(person: Tweet)(implicit reader: BSONDocumentReader[Tweet], writer:BSONDocumentWriter[Tweet]): Future[Boolean] = {
    coll.insert(person).map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }
  }
}
object DBCrud extends DBCrud