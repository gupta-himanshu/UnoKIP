package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.knoldus.model.Trends
import reactivemongo.bson.BSONDocument

trait DBTrendServices extends DBConnector {

  val collTrends = db("trend")
  val query = BSONDocument()
  val filter = BSONDocument()
  
  /**
 * Method to insert Trends object into mongoDB  
 * @param trends
 * @return Future[Boolean]
 */
def insertTrends(trends: Trends): Future[Boolean] = {
    collTrends.insert(trends).map { lastError => lastError.ok }
  }

  /**
 * Fetch the list of Trends object from mongoDB  
 * @return List[Trends]
 */
def findTrends(): Future[List[Trends]] = {
    collTrends.find(query, filter).cursor[Trends].collect[List]()
  }

  /**
 * Remove all documents from MongoDB 
 * @return
 */
def removeTrends(): Future[Boolean] = {
    collTrends.remove(query).map { lastError => lastError.ok }
  }
}

object DBTrendServices extends DBTrendServices
