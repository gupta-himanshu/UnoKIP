package utils;

import play.api.libs.json.Json
import play.api.libs.json.Writes
import models.Sentiment
import models.OtherAnalysis
import play.api.libs.json.JsObject

object JsonParserUtility {
  implicit def tuple2[A: Writes, B: Writes]: Writes[(A, B)] = Writes[(A, B)](o => play.api.libs.json.Json.arr(o._1, o._2))
  implicit val sentimentWrite = new Writes[Sentiment] {
    def writes(sentiment: Sentiment): JsObject = Json.obj(
      "positiveCount" -> sentiment.positiveCount,
      "negativeCount" -> sentiment.negativeCount,
      "neutralCount" -> sentiment.neutralCount)
  }

  implicit val otherAnalysisWrite = new Writes[OtherAnalysis] {
    def writes(otherAnalysis: OtherAnalysis): JsObject = Json.obj(
      "tweets" -> otherAnalysis.tweet,
      "hashtags" -> otherAnalysis.hashtag,
      "contributor" -> otherAnalysis.contributor)
  }

}
