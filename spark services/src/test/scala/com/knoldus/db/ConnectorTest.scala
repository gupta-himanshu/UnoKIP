package com.knoldus.db

import org.scalatest.FlatSpec
import org.scalatest.FunSuite

class ConnectorTest extends FunSuite with Connector{
  
    test("Test mongo DB connector"){    
    val res=connector("localhost","rmongo","rmongo","pass").name
    val expectedres= "rmongo" 
    assert(res===expectedres)
  }
}