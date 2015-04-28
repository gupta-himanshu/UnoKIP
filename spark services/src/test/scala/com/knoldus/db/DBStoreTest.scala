package com.knoldus.db

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfter
import org.scalatest.FlatSpec
import org.scalatest.FunSuite
import com.knoldus.tweetstreaming.Tweet

class DBStoreTest extends FunSuite with Connector with BeforeAndAfter {

  val db = connector("localhost", "rmongo", "rmongo", "pass")

  val dbcrud = new DBCrud(db, "table1")
  val coll = db("table1")
  test("insertion of tweets"){
    val res = dbcrud.insert(Tweet(591216001431142400L,"<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>","#KakakFathir Suka Menghayal ? 67",false,"Somen||48","amat_skate48","http://jkt48.com",2880640850L,"en"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
}