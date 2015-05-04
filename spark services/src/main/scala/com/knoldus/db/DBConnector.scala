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
  val dbName:String=config.getString("db.dbName")
  val username:String=config.getString("db.username")
  val pass:String=config.getString("db.password")
  val db = connector(host, dbName, username, pass)

  def connector(host: String, dbName: String, userName: String, password: String): DefaultDB = {
    val driver = new MongoDriver
    val credentials = Seq(Authenticate(dbName, userName, password))
    val connection = driver.connection(List(host), nbChannelsPerNode = numOfChannels,
      authentications = credentials)
    connection(dbName)
  }
}
