package com.knoldus.db

import scala.concurrent.Await
import com.knoldus.utils.ConstantUtil
import org.scalatest.BeforeAndAfter
import com.knoldus.model.Tweet
import org.scalatest.FunSuite
import scala.concurrent.duration.DurationInt
import com.knoldus.model.Trends

class DBTrendServicesTest extends FunSuite with BeforeAndAfter with DBTrendServices{

  test("insertion of trends") {
    val res = insertTrends(Trends("#source content", 5, 1))
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }
  
  test("fetch list of trends") {
    val res = findTrends()
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === List(Trends("#source content", 5, 1)))
  }
  
  test("remove trends from databse") {
    val res = removeTrends()
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
    
  }

}