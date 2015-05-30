package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.utils.ConstantUtil.numOfChannels
import reactivemongo.api.DefaultDB
import reactivemongo.api.MongoDriver
import reactivemongo.core.nodeset.Authenticate
import com.typesafe.config.ConfigFactory

trait DBConnector {

  val config = ConfigFactory.load()
  val host: String = config.getString("db.hostName")
  val dbName: String = config.getString("db.dbName")
  val username: String = config.getString("db.username")
  val pass: String = config.getString("db.password")
  val db = connector(host, dbName, username, pass)

  /**
   * This function create a connection to a mongoDB database using reactive mongo driver
   * @param host
   * @param dbName
   * @param userName
   * @param password
   * @return DefaultDB
   */
  def connector(host: String, dbName: String, userName: String, password: String): DefaultDB = {
    val driver = new MongoDriver
    val credentials = List(Authenticate(dbName, userName, password))
    val connection = driver.connection(List(host), nbChannelsPerNode = 5, authentications = credentials)
    connection(dbName)
  }
}
