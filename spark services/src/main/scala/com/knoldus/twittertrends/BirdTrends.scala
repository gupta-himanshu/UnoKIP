package com.knoldus.twittertrends

import java.io.File
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import com.knoldus.tweetstreaming.Tweet

trait BirdTweet {
  
  val sparkConf: SparkConf = new SparkConf().setAppName("bird").setMaster("local[2]").set(" spark.driver.allowMultipleContexts", "true").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  val sc: SparkContext = new SparkContext(sparkConf)

  def trending(show:List[Tweet]):List[(String,Int)] = {
    val RDDFinal=sc.parallelize(show)
   val res=RDDFinal.map { x => x.content}.flatMap { x => x.split(" ")}.filter { x => x.startsWith("#")}
    val pair=res.map { x => (x,1) }
    val trends=pair.reduceByKey((a,b)=>a+b).sortBy(_._2,false).take(10)
    trends.toList
  }
}

object BirdTweet extends BirdTweet