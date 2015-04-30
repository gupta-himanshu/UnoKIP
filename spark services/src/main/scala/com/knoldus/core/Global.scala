package com.knoldus.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * @author knoldus
 */

object Global {
  private val config = ConfigFactory.load()
  private val appName = config.getString("spark.conf.appName")
  private val master = config.getString("spark.conf.master")
  private val sparkConf: SparkConf = new SparkConf().setAppName(appName).setMaster(master)
  val sc: SparkContext = new SparkContext(sparkConf)
}
