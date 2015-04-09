package com.knoldus.db

<<<<<<< HEAD
import com.knoldus.DBConnection.Connector
=======
import com.knoldus.dbconnection.Connector
>>>>>>> b34110962e200f366cfa3fbdbc5f88f11b874ead
import org.scalatest._
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSON

import org.scalatest._

class ConnectorTest extends FlatSpec with Connector{
  
    "DB connection connected database" should "rmongo" in{    
    val res=connector("localhost","rmongo","rmongo","pass").name
    val expectedres= "rmongo" 
    assert(res===expectedres)
  }
}