package utils;

import play.api.libs.json.Json
import play.api.libs.json.Writes
import com.knoldus.model.Trends
import play.api.libs.json.OFormat

object JsonParserUtility {
  implicit def JsonParser: OFormat[Trends] = Json.format[Trends]
  implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
}
