package com.knoldus.sample

import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import com.knoldus.dbcrud.People
import com.knoldus.dbconnection.Connector
import com.knoldus.dbcrud.People
import com.knoldus.dbcrud.FindDoc
import reactivemongo.bson.Macros


object FindDocSample extends App with Connector{
  
  implicit val reader: BSONDocumentReader[Knoldus] = Macros.reader[Knoldus]
  implicit val writer: BSONDocumentWriter[Knoldus] = Macros.writer[Knoldus]
  
  //val p =People(BSONObjectID.generate,"sand")
  val knoldus = Knoldus(1046,"pushpendu","Jaipur")
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val findDoc=new FindDoc(datab,"table1")
  val num =findDoc.find(knoldus)
  val future = Await.result(num, 1 seconds)
  println(future)
}