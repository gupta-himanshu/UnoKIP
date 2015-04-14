import sbt._
import Keys._
import Dependencies._
import TestDependencies._
import org.scalastyle.sbt.ScalastylePlugin

object BuildSettings {
 
 lazy val commonSetting: Seq[Setting[_]] =
   Defaults.defaultSettings  ++
     Seq(
       	version := "1.0",
       	scalaVersion in ThisBuild := "2.11.5",
	organization := "knoldus",
       	parallelExecution in ThisBuild := false
	)
 
}
object SbtMultiBuild extends Build {

    import BuildSettings._


	lazy val UnoKIP = Project(id = "UnoKIP",
				base = file(".")) aggregate(dbs,example,samples)

	lazy val dbs =(project in file("dbs")).settings(
				commonSetting,
				libraryDependencies ++=  Seq(scalaTest,reactiveMongo,json4sNative))

    	lazy val example =(project in file("example")).settings(
				commonSetting,
				libraryDependencies ++= Seq(scalaTest,reactiveMongo)
				)
	lazy val samples =(project in file("samples")).settings(
				commonSetting,
				libraryDependencies ++= Seq(scalaTest,reactiveMongo,json4sNative)
				).dependsOn(dbs)
}
