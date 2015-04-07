//DB connection

package com.knoldus.DBConnection

import reactivemongo.api.{ collections, MongoDriver }
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory 
import reactivemongo.api.DefaultDB
import reactivemongo.core.nodeset.Authenticate

trait Connector {
  /**
   * This function take the database configuration from application.conf and make connection to collection table1 of rmongo database
   * @return BSONCollection type of object
   */
  def connector(host: String, dbName: String,userName:String,password:String): DefaultDB = {
    val driver = new MongoDriver
    // Gets a reference to the database
    val credentials = Seq(Authenticate(dbName, userName, password))
    val connection = driver.connection(List(host), nbChannelsPerNode = 5, authentications = credentials)
    val db = connection(dbName)
    db
  }
}
