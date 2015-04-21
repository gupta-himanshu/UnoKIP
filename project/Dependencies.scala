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

	val reactiveMongo = 	"org.reactivemongo" 	%% 	"reactivemongo" 	% 	"0.10.5.0.akka23"
	val DBSjar = 		"com.knoldus" 		%%	"dbs"			%	"1.0"
	val Streamingjar=	"com.knoldus"		%%	"streaming"		%	"1.0"
	val json4sNative = 	"org.json4s" 		%% 	"json4s-native" 	% 	"3.2.10"
	val sparkSQL = 		"org.apache.spark" 	%% 	"spark-sql" 		% 	"1.3.0"
	val sparkCore = 	"org.apache.spark" 	%% 	"spark-core" 		% 	"1.3.0"
	val sparkStream=        "org.apache.spark" %% "spark-streaming" % "1.2.1"
        val sparkTweet=         "org.apache.spark" %% "spark-streaming-twitter" % "1.2.1"
}







