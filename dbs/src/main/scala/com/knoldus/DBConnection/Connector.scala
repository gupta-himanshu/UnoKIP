//DB connection

package com.knoldus.DBConnection

import reactivemongo.api.{ collections, MongoDriver }
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory 
import reactivemongo.api.DefaultDB

trait Connector {
  /**
   * This function take the database configuration from application.conf and make connection to collection table1 of rmongo database
   * @return BSONCollection type of object
   */
  def connector(host: String, dbName: String): DefaultDB = {
    val driver = new MongoDriver
    val connection = driver.connection(List(host))
    // Gets a reference to the database
    val db = connection(dbName)
    // Gets a reference to the collection
    db
  }
}
