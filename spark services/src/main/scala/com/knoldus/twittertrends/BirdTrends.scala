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

class BirdTweet {
  
  val sparkConf: SparkConf = new SparkConf().setAppName("bird").setMaster("local[2]").set(" spark.driver.allowMultipleContexts", "true").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  val sc: SparkContext = new SparkContext(sparkConf)
  def getListOfSubDirectories(directoryName: String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(_.isDirectory).map(_.getName)
  }

  def reduceRDDList(list: List[RDD[String]]): RDD[String] = {
    list match {
      case Nil     => sc.parallelize(Nil)
      case y :: ys => sc.union(y, reduceRDDList(ys))
    }
  }
  def trending() = {
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
    val tree = getListOfSubDirectories("tweets")
    val RDDList = tree.toList.map(x => sc.textFile("tweets/" + x)) //getting List of RDD of collected Hashtags
    val RDDFinal = reduceRDDList(RDDList) //getting union of all RDD
    val schemaString = "tweets"
    val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true))) //creating schema for temp table
    val tweetRDD = RDDFinal.filter { x => x.startsWith("#") }.map(_.split(" ")).map(p => Row(p(0).toLowerCase())) //filtering hashtags from RDD (RDDFinal)
    val tweetDataFrame = sqlContext.createDataFrame(tweetRDD, schema)
    tweetDataFrame.registerTempTable("tweetsTable")
    val result = sqlContext.sql("SELECT tweets FROM tweetsTable")
    val top = result.map(t => (t(0), 1)).reduceByKey((a, b) => a + b).sortBy(_._2, false).take(10)
    //result.map(t => "Tweets: " + t(0)).collect().foreach(println)
    top.foreach(x => println("----------------------------------Hashtag----------------------------------------------------------------------------------------------------------------------------------------  " + x._1 + " is used " + x._2 + " times."))
  }
}

object test{
   def main(args: Array[String]): Unit = {
   val s=new BirdTweet
   s.trending()
  }
}
