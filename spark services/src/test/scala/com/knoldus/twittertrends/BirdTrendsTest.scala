package com.knoldus.twittertrends

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.knoldus.db.DBConnector
import com.knoldus.db.DBTrendServices
import com.knoldus.model.Trends
import com.knoldus.model.Tweet
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.FunSuite
import scala.concurrent.Await
import scala.concurrent.Future

class BirdTrendTest extends FunSuite with MockitoSugar {

  val mockDbTrendService: DBTrendServices = mock[DBTrendServices]

  object mockObj extends BirdTweet {
    val dbTrendService: DBTrendServices = mockDbTrendService

  }

  test("Test for top trends") {
    val listOfTweets = List(Tweet(123, "source", "#content", true, "authName", "username", "url", 234, "language"))
    val listOfTrends = List(Trends("#source content", 5, 2))
    val pgNo = 2
    when(mockDbTrendService.insertTrends(Trends("#source content", 5, 2))) thenReturn (Future(true))
    when(mockDbTrendService.removeTrends()) thenReturn(Future(true))
    val newTrends = mockObj.trending(listOfTweets, listOfTrends, pgNo)
    assert(newTrends.size === 2)
  }
}