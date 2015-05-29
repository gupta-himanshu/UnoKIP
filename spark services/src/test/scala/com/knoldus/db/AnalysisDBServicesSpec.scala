package com.knoldus.db

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil
import reactivemongo.bson.BSONDocument
import java.util.Date
import org.scalatest.FunSuite
import com.knoldus.model.Trend
import com.knoldus.model.Sentiment
import com.knoldus.model.TweetDetails
import com.knoldus.model.OtherAnalysis


class AnalysisDBServicesSpec extends FunSuite with DBConnector with BeforeAndAfter with AnalysisDBServices {

  test("insert trends object in mongoDB") {
    val result = insertTrends(Trend("#scaladays", 3))
    val finalRes = Await.result(result, 1 second)
    assert(finalRes == true)
  }

  test("fetch list of trends") {
    val res = findTrends()
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === List(Trend("#scaladays", 3)))
  }

  test("remove trends from databse") {
    val res = removeTrends()
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === true)
  }
  
  
  test("insert sentiment object in mongoDB") {
    val res = insertSentiment(Sentiment("odersky",Some(1),Some(0),Some(3)))
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === true)
  }
  
  test("find sentiment object from mongoDB") {
    val res = findSentiment("odersky")
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === Some(Sentiment("odersky",Some(1),Some(0),Some(3))))
  }
  
  test("update sentiment object from mongoDB") {
    val res = updateSentiment(Sentiment("odersky",Some(1),Some(0),Some(3)))
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === true)
  }

  test("insert tweetDetails object in mongoDB") {
    val res = insertTweetDetails(TweetDetails(123,"i m so happy",Array("#scaladays"),"odersky","odersky",3))
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === true)
  }
  
  
  test("find tweetDetails object in mongoDB") {
    val res = findTweetDetails("odersky")
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === List(TweetDetails(123,"i m so happy",Array("#scaladays"),"odersky","odersky",3)))
  }
  
   test("insert hashTags object in mongoDB") {
    val res =insertHashtag(OtherAnalysis("odersky",Array("#scaladays"),"harrish"))
    val finalRes = Await.result(res, 2 second)
    assert(finalRes === true)
  }
  
}