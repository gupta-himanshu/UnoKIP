package com.knoldus.db

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import com.knoldus.model.Tweet
import com.knoldus.utils.ConstantUtil
import reactivemongo.bson.BSONDocument

class DBServicesTest extends FunSuite with DBConnector with BeforeAndAfter with DBServices {

  before {
    val remove = collTweet.remove(BSONDocument())
    Await.result(remove, 1 second)
    insert(Tweet(591216111431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
      "#KakakFathir Suka Menghayal ? 67", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en"))
  }

  test("insertion of tweets") {
    val res = insert(Tweet(591216001431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
      "#KakakFathir Suka Menghayal ? 67", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en"))
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }

  test("fetching tweets from mongodb") {
    val res = getChunckOfTweet(1, ConstantUtil.pageSize)
    val fres = Await.result(res, 1 second)
    assert(fres.size === 1)
  }
}