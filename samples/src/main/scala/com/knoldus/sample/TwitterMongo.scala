package com.knoldus.sample

import com.knoldus.streaming.SparkStore

object TwitterMongo extends App with Serializable {
  val sparkStore = new SparkStore
  
  
  sparkStore.start
  sparkStore.stop
  

}