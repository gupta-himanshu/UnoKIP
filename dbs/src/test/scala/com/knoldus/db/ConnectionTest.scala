package com.knoldus.db

import com.knoldus.DBConnection.Connector
import org.scalatest._
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSON

class ConnectorTest extends FlatSpec with Connector{

    "DB connection table name" should "table1" in{    
    val res=connector("localhost","rmongo","table1").name
    val expectedres="table1"
    assert(res===expectedres)
  }
}