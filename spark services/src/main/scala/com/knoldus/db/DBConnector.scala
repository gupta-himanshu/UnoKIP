package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.utils.ConstantUtil.numOfChannels
import reactivemongo.api.DefaultDB
import reactivemongo.api.MongoDriver
import reactivemongo.core.nodeset.Authenticate

trait DBConnector {

  val db = connector("localhost", "rmongo", "username", "Password")

  def connector(host: String, dbName: String, userName: String, password: String): DefaultDB = {
    val driver = new MongoDriver
    val credentials = Seq(Authenticate(dbName, userName, password))
    val connection = driver.connection(List(host), nbChannelsPerNode = numOfChannels,
      authentications = credentials)
    connection(dbName)
  }
}
