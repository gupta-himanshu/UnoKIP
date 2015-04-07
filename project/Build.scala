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
       	parallelExecution in ThisBuild := false
	)
 
}
object SbtMultiBuild extends Build {

    import BuildSettings._

    val dbsDep = libraryDependencies ++= Seq(scalaTest,reactiveMongo)
    //val exampleDep = libraryDependencies ++= Seq(scalaTest,reactiveMongo)
    lazy val UnoKIP = Project(id = "UnoKIP",
				base = file(".")) aggregate(dbs,example)

    lazy val dbs = Project(id = "dbs",
                           base = file("dbs"),
			   settings = commonSetting ++ dbsDep)
    lazy val example = Project(id = "example",
				base = file("example"),
				settings = commonSetting ++ dbsDep
				).dependsOn(dbs)
}
