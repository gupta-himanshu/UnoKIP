import sbt._
import Keys._


object Version {

  val scalaTestVer = "2.2.4"

}
object TestDependencies{
	import Version._
	val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVer % "test"	
}
object Dependencies{

	val reactiveMongo = "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
	val DBSjar = "knoldus" 	%%	"dbs"	%	"1.0"
	val json4sNative = "org.json4s" %% "json4s-native" % "3.2.10"
}







