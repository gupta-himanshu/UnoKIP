package com.knoldus.twittertrends

import com.knoldus.db.DBConnector
import org.scalatest.FunSuite
import scala.concurrent.duration.DurationInt
import com.knoldus.db.DBServices
import scala.concurrent.Await
import com.knoldus.utils.ConstantUtil
import com.knoldus.db.DBTrendServices


class BirdTrendTest extends FunSuite with DBConnector {
  
  test("Test for top trends") {
    val lisOfTweets = DBServices.filterQuery(2, ConstantUtil.pageSize)
    val listOfTrends=DBTrendServices.findTrends()
    val tweets=Await.result(lisOfTweets, 1 second)
    val trends=Await.result(listOfTrends, 1 second)
    val newTrend=BirdTweet.trending(tweets,trends,2)
    assert(newTrend.size===10)
  }
}