package com.knoldus.sample

import DBConnection.Connector
//home/knoldus/workspaceTest/UnoKIP/dbs/target/scala-2.11/classes
object SampleObject extends App with Connector {
  val res=connector.name
  println( res)

}