import sbt._
import Keys._
object Dependencies{

	lazy val DBSDependency = libraryDependencies ++= Seq(
                       "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
		        "org.scalatest" %% "scalatest" % "2.2.4" % "test"
                    )
}







