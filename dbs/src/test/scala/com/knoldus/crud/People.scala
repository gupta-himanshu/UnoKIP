package com.knoldus.crud

import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.Macros

case class People(_id: BSONObjectID, name: String)

object People
{
  implicit val read = Macros.reader[People]
  implicit val write = Macros.writer[People]
  }