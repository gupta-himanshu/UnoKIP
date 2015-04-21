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
	organization := "com.knoldus",
       	parallelExecution in ThisBuild := false
	)
 
}
object SbtMultiBuild extends Build {

    import BuildSettings._


	lazy val UnoKIP = Project(id = "UnoKIP",
				base = file(".")) aggregate(dbs,streaming)

	lazy val dbs =(project in file("dbs")).settings(
				commonSetting,
				libraryDependencies ++=  Seq(scalaTest,reactiveMongo,json4sNative))

 	lazy val streaming = (project in file("streaming")).settings(
				commonSetting,	
				libraryDependencies ++= Seq(sparkStream,sparkTweet,reactiveMongo)
				).dependsOn(dbs)
	lazy val utilities = (project in file("utilities")).settings(
				commonSetting, 
				libraryDependencies ++= Seq(json4sNative)
				).dependsOn(dbs)
	lazy val samples = project


}
