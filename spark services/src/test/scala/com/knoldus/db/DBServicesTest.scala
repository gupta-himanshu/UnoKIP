package com.knoldus.db

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import com.knoldus.model.Models.Tweet
import reactivemongo.api.Cursor

class DBStoreTest extends FunSuite with DBConnector with BeforeAndAfter {

  val dbcrud = DBServices
  test("insertion of tweets") {
    val res = dbcrud.insert(Tweet(591216001431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>", "#KakakFathir Suka Menghayal ? 67", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
  test("fetching tweets from mongodb"){
     val res = dbcrud.findWholeDoc()
     val fres=Await.result(res, 5 second)
     val exprectedres=12104
     assert(fres.size===exprectedres)
  }
}