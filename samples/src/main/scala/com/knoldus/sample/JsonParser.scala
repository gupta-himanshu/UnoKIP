package com.knoldus.sample

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s._
import org.json4s.JsonDSL._
import com.knoldus.dbconnection.People
import com.knoldus.converter.JsonConverter
import reactivemongo.bson.BSONObjectID

object JsonParserObj extends App with JsonConverter {
  val json = """{"name":"Pushpendu"}"""
  val jsonToCaseClass = toCaseClass(json)
  println(jsonToCaseClass)
  val people = People(BSONObjectID.generate,"name")
  val caseClassToJson = toJson(people)
  println(caseClassToJson)
  
}