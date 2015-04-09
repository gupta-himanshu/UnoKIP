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

case class People(name:String)

trait convertor {
    implicit val reader: BSONDocumentReader[People] = Macros.reader[People]
    implicit val writer: BSONDocumentWriter[People] = Macros.writer[People]
  
}
trait DBCrud extends Connector with convertor{  
  
  import scala.collection.mutable.ListBuffer
  var l: ListBuffer[String] = new ListBuffer
  
  def find (name:String)(implicit coll:BSONCollection)= {
    val query = BSONDocument("name"->name)
    // select only the fields 'lastName' and '_id'
    val filter = BSONDocument(
      "name" -> 1,
      "_id" -> 1)

    val cursor = coll.find(query, filter).cursor[People]
    val stream = cursor.collect[List]()
      stream.map { x =>
       x.size
        }      
  }    
  
  def insert(p:People): Future[Boolean] = {
    val future = coll.insert(p)
    future.map { lastError =>
      lastError.errMsg match {
        case Some(msg) => false
        case None      => true
      }
    }

  }
  def update(name:String) = {
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
  def delete(name:String) = {
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