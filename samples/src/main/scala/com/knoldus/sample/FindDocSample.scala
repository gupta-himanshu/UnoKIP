package com.knoldus.sample

import reactivemongo.bson.BSONObjectID
import com.knoldus.dbconnection.Connector
import com.knoldus.dbcrud.FindDoc
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import reactivemongo.bson.Macros
import com.knoldus.dbconnection.DBCrud
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter


object FindDocSample extends App with Connector{
  val p =People(BSONObjectID.generate,"sand")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val findDoc=new FindDoc(datab,"table1")
   implicit val write=Macros.reader[People]
    implicit val read=Macros.writer[People]
  val db=new DBCrud[People](datab,"table1")
  val objId=BSONObjectID.generate
  val isInserted=db.insert(People(objId,"ss"))
   val isInsertedDone = Await.result(isInserted, 1 seconds)
  val num =findDoc.find(objId.stringify)
  val future = Await.result(num, 1 seconds)
  println(future)
}
