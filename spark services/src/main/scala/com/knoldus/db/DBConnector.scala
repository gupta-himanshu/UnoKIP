package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.utils.ConstantUtil.numOfChannels
import reactivemongo.api.DefaultDB
import reactivemongo.api.MongoDriver
import reactivemongo.core.nodeset.Authenticate
import org.slf4j.LoggerFactory
import org.slf4j.Logger
//import org.apache.log4j.Level
//import org.apache.log4j.Logger

trait DBConnector {
//  Logger.getLogger("org").setLevel(Level.OFF)
  val logger = LoggerFactory.getLogger(this.getClass.getName)
  def connector(host: String, dbName: String, userName: String, password: String): DefaultDB = {
    val driver = new MongoDriver
    val credentials = Seq(Authenticate(dbName, userName, password))
    val connection = driver.connection(List(host), nbChannelsPerNode = numOfChannels, authentications = credentials)
    connection(dbName)
  }
}
