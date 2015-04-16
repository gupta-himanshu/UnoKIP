package com.knoldus.db

import com.knoldus.dbconnection.Connector
import org.scalatest._
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSON

class ConnectorTest extends FlatSpec with Connector{
  
    "DB connection connected databae" should "rmongo" in{    
    val res=connector("localhost","rmongo","rmongo","pass").name
    val expectedres= "rmongo" 
    assert(res===expectedres)
  }
}