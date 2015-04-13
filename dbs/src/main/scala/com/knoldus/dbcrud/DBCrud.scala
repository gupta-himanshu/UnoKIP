package com.knoldus.dbconnection

import akka.actor.ActorLogging
import reactivemongo.api._
import reactivemongo.bson.BSONDocument
import reactivemongo.bson._
import scala.concurrent.Future
import play.api.libs.iteratee.Iteratee
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.concurrent.impl.Future
import reactivemongo.api.collections.default._
import org.json4s.JsonInput
import com.knoldus.converter.JsonConverter

case class People(name:String)

trait convertor {
    implicit val reader: BSONDocumentReader[People] = Macros.reader[People]
    implicit val writer: BSONDocumentWriter[People] = Macros.writer[People]
  
}
trait DBCrud extends Connector with convertor with JsonConverter{  
  import scala.collection.mutable.ListBuffer
  var l: ListBuffer[String] = new ListBuffer
  
  def find (name:String)(implicit coll:BSONCollection)= {
    val query = BSONDocument("name"->name)
    val filter = BSONDocument(
      "name" -> 1)

    val cursor = coll.find(query, filter).cursor[People]
    val stream = cursor.collect[List]()
      stream.map { x =>
       x
        }      
  }    
  
  def insert(person:JsonInput)(implicit coll:BSONCollection): Future[Boolean] = {
    val conPerson=toCaseClass(person)
    val future = coll.insert(conPerson)
    future.map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }

  }
  def update(name:String)(implicit coll:BSONCollection) :Future[Boolean]= {
    val modifier = BSONDocument(
      "name" -> "charlie")
    val selector = BSONDocument("name" -> name)
    // get a future update
    val futureUpdate = coll.update(selector, modifier)

    futureUpdate.map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }

  }
  def delete(name:String)(implicit coll:BSONCollection) :Future[Boolean] = {
    val selectorDelete = BSONDocument(
      "name" -> name)

    val futureRemove = coll.remove(selectorDelete)

    futureRemove.map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }

  }
}
