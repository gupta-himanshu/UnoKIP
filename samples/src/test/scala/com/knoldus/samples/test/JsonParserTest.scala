package com.knoldus.samples.test

import org.scalatest._
import com.knoldus.sample.JsonParserObj
import com.knoldus.converter.JsonConverter
import com.knoldus.dbconnection.People
import reactivemongo.bson.BSONObjectID

class JsonParserTest extends FlatSpec with JsonConverter {
  "json converter will word" should "case class" in {
    val json = """{"name":"Pushpendu"}"""
    val res = toCaseClass(json)
    val expectedres = People(BSONObjectID.generate,"pushpendu")
    assert(res === expectedres)
  }
}