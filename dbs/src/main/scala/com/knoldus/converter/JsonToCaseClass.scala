package com.knoldus.converter

import com.knoldus.dbconnection.People
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s._
import org.json4s.JsonDSL._

trait JsonToCaseClass {
  def toCaseClass(json:JsonInput):People={
    implicit val formats = org.json4s.DefaultFormats
    parse(json).extract[People]
  }

}