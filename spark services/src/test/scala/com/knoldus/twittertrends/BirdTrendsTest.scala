package com.knoldus.twittertrends

import com.knoldus.db.DBConnector
import org.scalatest.FunSuite
import scala.concurrent.duration.DurationInt
import com.knoldus.db.DBServices
import scala.concurrent.Await

/**
 * @author knoldus
 */

class BirdTrendTest extends FunSuite with DBConnector {
  
  test("Test for top trends") {
    val show = DBServices.findWholeDoc()
    val res=Await.result(show, 1 second)
    val fres=BirdTweet.trending(res)
    assert(fres.size===10)
  }
}