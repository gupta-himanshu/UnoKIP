import sbt._
import Keys._
import Dependencies._

object BuildSettings {
 
 lazy val commonSetting =
   Defaults.defaultSettings ++ 
     Seq(
       	version := "1.0",
       	scalaVersion in ThisBuild := "2.11.5",
       	parallelExecution in ThisBuild := false
	)
 
}
object SbtMultiBuild extends Build {

    import BuildSettings._

    lazy val parent = Project(id = "UnoKIP",
                            base = file(".")) aggregate(DBS)

    lazy val DBS = Project(id = "DBS",
                           base = file("DBS"),
			   settings = commonSetting ++ DBSDependency)

}
