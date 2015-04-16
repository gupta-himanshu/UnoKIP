package com.knoldus.crud

import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import com.knoldus.dbconnection.People
import com.knoldus.dbconnection.DBCrud
import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.dbconnection.People
import com.knoldus.dbconnection.Connector

class CrudTest extends FlatSpec with Connector with BeforeAndAfter {

  private val objectId = BSONObjectID.generate

  val db = connector("localhost", "rmongo", "rmongo", "pass")
  val dbcrud=new DBCrud(db,"table1")
  val coll=db("table1")
  before {   
    coll.drop()
    val res = dbcrud.insert(People(objectId, "iii"))
    Await.result(res, 1 second)
  }
  after {
    coll.drop()
  }
  
  "insert data" should "true" in {
    val res = dbcrud.insert(People(BSONObjectID.generate, "name"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }

  "update data" should "true" in {
    val res = dbcrud.update(People(objectId, "xyz"))
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }

  "remove data" should "true" in {
    val res = dbcrud.delete(People(BSONObjectID.generate, "xyz"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
}