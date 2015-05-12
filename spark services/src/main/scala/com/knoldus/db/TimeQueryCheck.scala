package com.knoldus.db

import scala.concurrent.ExecutionContext.Implicits.global
import java.text.SimpleDateFormat
import org.joda.time.DateTime
import java.util.Date
import com.sun.jmx.snmp.Timestamp
import org.joda.time.format.DateTimeFormat
import java.util.TimSort

/**
 * @author knoldus
 */
object TimeQueryCheck extends App {
  val dbse = DBServices
  val date = new DateTime();
  val formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
  val dt1=date.toString("MM/dd/yyyy HH:mm")
  println(dt1)
  
  val start = formatter.parseDateTime("11/05/2013 11:46");
  val end = formatter.parseDateTime(dt1)
  //println(formatter.parseDateTime(date.toString()))
  val res = DBServices.getTimeOfTweet(start.getMillis, end.getMillis)
  res.map { x => println(x) }
}