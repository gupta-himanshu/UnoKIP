//DB connection

package DBConnection

import reactivemongo.api.{collections,MongoDriver}
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import reactivemongo.bson.BSON

trait Connector {
  /**
   * This function take the database configuration from application.conf and make connection to collection table1 of rmongo database
   * @return BSONCollection type of object
   */
  def connector:collections.default.BSONCollection = {
    val config = ConfigFactory.load
    val driver = new MongoDriver
    val location = config.getString("mongo.location")
    val database = config.getString("mongo.db")
    val collection = config.getString("mongo.collection")
      val connection = driver.connection(List(location))
      // Gets a reference to the database "rmongo"
      val db = connection(database)
      // Gets a reference to the collection "coll"
      val coll = db(collection)
      coll
    
      
  }
}
