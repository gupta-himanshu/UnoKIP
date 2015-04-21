package dataProcess

import com.knoldus.dbcrud.FindDoc
import com.knoldus.dbconnection.Connector
import com.knoldus.crud.People
import reactivemongo.bson.Macros
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import play.api.libs.iteratee.Iteratee
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global

object ShowData extends App with Connector {
  val datab=connector("localhost","rmongo", "rmongo", "pass")
  val findDoc = new FindDoc[People](datab, "table1")
//  implicit val write=Macros.reader[People]
//  implicit val read=Macros.writer[People]
//  val cursor =findDoc.findWholeDoc()
  val enumerator =findDoc.findWholeDoc().enumerate()
  val processDocuments: Iteratee[People, Unit] =
    Iteratee.foreach { people =>
      println(s"$people")
    }
  //enumerator.apply(processDocuments)
  
}      

