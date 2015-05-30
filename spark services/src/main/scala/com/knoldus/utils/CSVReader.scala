package com.knoldus.utils


import scala.collection.JavaConversions._
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import com.knoldus.model.WordList


/**
 * @author knoldus
 */

object CSVReader {
     def readCSV() = {
    val reader = new CSVReader(new FileReader("/home/knoldus/sentiment project/cluster_design/UnoKIP/spark services/affin.csv"), '\t')
    val csv = reader.readAll()
    csv.map { x => WordList(x(0), x(1).toDouble) }.toList

  }
val csv=readCSV()
}