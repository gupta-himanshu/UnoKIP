import sbt._
import Keys._


object Version {

  val scalaTestVer = "2.2.4"

}

object Dependencies{
import Version._
	val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVer % "test"
	val reactiveMongo = "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
}







