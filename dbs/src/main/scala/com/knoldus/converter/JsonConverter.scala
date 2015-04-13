package com.knoldus.converter

import com.knoldus.dbconnection.People
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s._
import org.json4s.JsonDSL._

trait JsonConverter {
  def toCaseClass(json:JsonInput):People={
    implicit val formats = org.json4s.DefaultFormats
    parse(json).extract[People]
  }
  
  def toJson(people:People):String={
    val j = ("name"-> people.name)
    compact(render(j))
  }

}