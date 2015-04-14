package com.knoldus.crud

import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import com.knoldus.dbconnection.People
import com.knoldus.dbconnection.DBCrud
import scala.concurrent.ExecutionContext.Implicits.global

class CrudTest extends FlatSpec with DBCrud with BeforeAndAfter {

  val db = connector("localhost", "rmongo", "rmongo", "pass")
  implicit val coll = db("table1")
  before {
    val res = insert(People("iii"))
    Await.result(res, 1 second)
  }
  after {
    coll.drop()
  }
  
  "fetch data with find" should "1" in {
    val res = find()
    val finalRes = Await.result(res, 1 second)
    val expectedres = List(People("iii"))
    assert(finalRes === expectedres)
  }
  "insert data" should "true" in {
    val res = insert(People("name"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }

  "update data" should "true" in {
    val res = update(People("xyz"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }

  "remove data" should "true" in {
    val res = delete(People("xyz"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
}