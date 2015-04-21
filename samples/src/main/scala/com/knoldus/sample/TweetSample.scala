package com.knoldus.sample

import com.knoldus.streaming.SparkStore


object TwitterMongo extends App {
  val s=new SparkStore()
  s.start()
  s.stop()
  
  
}