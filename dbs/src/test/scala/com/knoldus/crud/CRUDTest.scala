package com.knoldus.crud

import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import com.knoldus.dbconnection.DBCrud
import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.dbconnection.Connector
import reactivemongo.bson.Macros
import com.knoldus.dbconnection.DBCrud



class CrudTest extends FlatSpec with Connector with BeforeAndAfter {

  private val objectId = BSONObjectID.generate

  val db = connector("localhost", "rmongo", "rmongo", "pass")

  val dbcrud = new DBCrud[People](db, "table1")
  val coll = db("table1")
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
    val res = dbcrud.update(objectId.stringify, People(objectId, "xyz"))
    val finalRes = Await.result(res, 1 second)
    assert(finalRes === true)
  }

  "remove data" should "true" in {
    val res = dbcrud.delete(objectId.stringify)
    val finalRes = Await.result(res, 1 second)
    val expectedres = true
    assert(finalRes === expectedres)
  }
}