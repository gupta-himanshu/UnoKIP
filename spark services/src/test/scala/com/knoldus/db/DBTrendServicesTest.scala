package com.knoldus.db

import scala.concurrent.Await
import com.knoldus.utils.ConstantUtil
import org.scalatest.BeforeAndAfter
import com.knoldus.model.Tweet
import org.scalatest.FunSuite
import scala.concurrent.duration.DurationInt
import com.knoldus.model.Trends

class DBTrendServicesTest extends FunSuite with DBConnector with BeforeAndAfter {

  val dbtrendservice = DBTrendServices

  test("insertion of trends") {
    val res = dbtrendservice.insertTrends(Trends("#source content", 5, 1))
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }
  test("remove trends from databse") {
    val res = dbtrendservice.removeTrends()
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
    
  }

  test("fetch list of trends") {
    val res = dbtrendservice.findTrends()
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }
}