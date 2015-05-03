package com.knoldus.db

import scala.concurrent.Future
import com.knoldus.model.Trends
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONDocument

trait DBTrendServices extends DBConnector {

  val collTrends = db("trend")
  val query = BSONDocument()
  val filter = BSONDocument()
  
  def insertTrends(trends: Trends): Future[Boolean] = {
    collTrends.insert(trends).map { lastError => lastError.ok }
  }

  def findTrends(): Future[List[Trends]] = {
    collTrends.find(query, filter).cursor[Trends].collect[List]()
  }

  def removeTrends(): Future[Boolean] = {
    collTrends.remove(query).map { lastError => lastError.ok }
  }

}

object DBTrendServices extends DBTrendServices