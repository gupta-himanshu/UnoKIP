package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.knoldus.tweetstreaming.Tweet

import reactivemongo.bson.Macros

trait DBStore extends Connector {

  val db = connector("localhost", "rmongo", "username", "Password")
  val coll = db("table1")

  def insert(tweet: Tweet): Future[Boolean] = {
    implicit val read = Macros.reader[Tweet]
    implicit val write = Macros.writer[Tweet]
    coll.insert(tweet).map { lastError =>
      lastError.ok
    }
  }
}

object DBStore extends DBStore 