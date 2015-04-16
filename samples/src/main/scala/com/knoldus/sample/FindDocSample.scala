package com.knoldus.sample

import com.knoldus.dbconnection.People
import reactivemongo.bson.BSONObjectID
import com.knoldus.dbconnection.Connector
import com.knoldus.dbcrud.FindDoc
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object FindDocSample extends App with Connector{
  val p =People(BSONObjectID.generate,"sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val findDoc=new FindDoc(datab,"table1")
  val num =findDoc.find(People(BSONObjectID.generate,"pp"))
  val future = Await.result(num, 1 seconds)
  println(future)

}