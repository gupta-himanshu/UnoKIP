import sbt._
import Keys._
import Dependencies._
import org.scalastyle.sbt.ScalastylePlugin

object BuildSettings {
 
 lazy val commonSetting =
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

    val dbsDep = libraryDependencies ++= Seq(scalaTest,reactiveMongo,lift)
    val exampleDep = libraryDependencies ++= Seq(scalaTest,reactiveMongo)
    val sampleDep =  libraryDependencies ++= Seq(scalaTest,reactiveMongo,DBSjar)
    lazy val UnoKIP = Project(id = "UnoKIP",
				base = file(".")) aggregate(dbs,example,samples)

	lazy val dbs = Project(id = "dbs",
                           	base = file("dbs"),
			   	settings = commonSetting ++ dbsDep)
    	lazy val example = Project(id = "example",
				base = file("example"),
				settings = commonSetting ++ exampleDep
				)
	lazy val samples = Project(id = "samples",
				base = file("samples"),
				settings = commonSetting ++ sampleDep
				).dependsOn(dbs)
}
