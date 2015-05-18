package com.knoldus.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.typesafe.config.ConfigFactory

/**
 * @author knoldus
 */

//Global spark context

object Global {
  private val config = ConfigFactory.load()
  private val appName = config.getString("spark.conf.appName")
  private val master = config.getString("spark.conf.master")
  private val sparkConf: SparkConf = new SparkConf().setAppName(appName).setMaster(master).setJars(Array("/home/knoldus/Desktop/twitter4j-4.0.3.zip"))
  val sc: SparkContext = new SparkContext(sparkConf)
}
