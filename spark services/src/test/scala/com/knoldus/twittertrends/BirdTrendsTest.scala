/*package com.knoldus.twittertrends

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
import com.knoldus.db.DBServices
import java.util.Date

class BirdTrendTest extends FunSuite with MockitoSugar {

  val mockDbServices: DBServices = mock[DBServices]

  object mockObj extends BirdTweet {
    val  dbServices: DBServices = mockDbServices

  }

  test("Test for top trends") {
    when(mockDbServices.getTimeOfTweet(0, 1)) thenReturn(Future(List(Tweet(591216111431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
      "#KakakFathir Suka Menghayal ? 67", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en",
      new Date, Some("new york"), Some("new york"), Some(23554221), Some(43545423)))))
   val trends = mockObj.trending1(0, 1)
    val fres = Await.result(trends, 3 second)
    assert(fres.size === 1)
  }
}*/