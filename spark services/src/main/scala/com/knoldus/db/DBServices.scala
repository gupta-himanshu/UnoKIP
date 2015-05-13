package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Tweet
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONDocument
import java.util.Date
import org.joda.time.format.ISODateTimeFormat
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDateTime

/**
 * @author knoldus
 *
 */
trait DBServices extends DBConnector {

  val filter = BSONDocument()
  val query = BSONDocument()
  val collTweet = db("twitterHandles")

  /**
   * Function to insert Tweet object into mongoDB collection
 * @param tweet
 * @return lastErro
 */
def insert(tweet: Tweet): Future[Boolean] = {
    collTweet.insert(tweet).map { lastError =>
      lastError.ok
    }
  }

  /**
   * Function to fetch chunck of data from mongoDB using pagination
 * @param pageNumber
 * @param pageSize
 * @return
 */
def getChunckOfTweet(pageNumber: Int, pageSize: Int): Future[List[Tweet]] = {
    collTweet.find(query).options(QueryOpts((pageNumber - 1) * pageSize, pageSize)).cursor[Tweet].collect[List](pageSize)
  }

def getTimeOfTweet(startTime:Long,end:Long): Future[List[Tweet]] = {
  
  val query= BSONDocument( "create" -> BSONDocument("$gte" -> BSONDateTime(startTime),
                                                   "$lt" -> BSONDateTime(end))
                )
   collTweet.find(query).cursor[Tweet].collect[List]()
  }
}
object DBServices extends DBServices
