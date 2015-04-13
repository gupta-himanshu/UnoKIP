package com.knoldus.sample

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s._
import org.json4s.JsonDSL._
import com.knoldus.converter.JsonToCaseClass

object JsonParserObj extends App with JsonToCaseClass {
  val json = """{"name":"Pushpendu"}"""
  val jData = toCaseClass(json)
  println(jData)
}