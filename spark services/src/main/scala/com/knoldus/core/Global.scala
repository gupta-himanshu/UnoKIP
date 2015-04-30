package com.knoldus.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * @author knoldus
 */
object Global {
 val sparkConf: SparkConf = new SparkConf().setAppName("tweet_collect").setMaster("local[2]")
 val sc: SparkContext = new SparkContext(sparkConf)
}
