package com.knoldus.tweetstreaming

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.api.HelpResources.Language
import java.util.Date
import twitter4j.URLEntity
import twitter4j.Status
import twitter4j.Place
import twitter4j.GeoLocation
import twitter4j.UserMentionEntity
import com.knoldus.db.Connector
import com.knoldus.db.DBCrud
import org.apache.spark.serializer.KryoRegistrator
import com.esotericsoftware.kryo.Kryo

case class Tweet(id: Long, source: String, content: String, retweet: Boolean, authName: String, username: String, url: String, authId: Long, language: String)

/**
 * @author knoldus
 */
trait SparkStreaming extends Connector {

  def startStream(appName: String, master: String): StreamingContext = {
    val db = connector("localhost", "rmongo", "rmongo", "pass")
    val dbcrud = new DBCrud(db, "table1")
    val sparkConf: SparkConf = new SparkConf().setAppName(appName).setMaster(master).set(" spark.driver.allowMultipleContexts", "true")
    //  .set("spark.kryo.registrator", "HelloKryoRegistrator")
    //    sparkConf.registerKryoClasses(Array(classOf[DBCrud]))
    val sc: SparkContext = new SparkContext(sparkConf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(10))
    ssc
  }
}

object SparkStreaming extends SparkStreaming

