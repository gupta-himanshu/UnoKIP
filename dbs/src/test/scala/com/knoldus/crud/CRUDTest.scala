package com.knoldus.crud

import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import com.knoldus.dbconnection.People
import com.knoldus.dbconnection.DBCrud

class CrudTest extends FlatSpec with DBCrud  with BeforeAndAfter{
  
val db=connector("localhost", "rmongo","rmongo","pass")
 implicit val coll=db("table1")
 before
 {
  insert(People("name"))
 }
 after
 {
   delete("name")
 }
  "fetch data with find" should "1" in {
    val res = find("name")
    val finalRes = Await.result(res, 1 second)

    val expectedres = 1
    assert(finalRes === expectedres)
  }
  "insert data" should "true" in {
    val p=People("xyz")
    val res = insert(p)
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }

  /*"update data" should "true" in {
    val res = update("xyz")
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }*/

  "remove data" should "true" in {
    val res = delete("xyz")
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
}