name := """tweetTrends"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  						"org.reactivemongo" 	%% 	"reactivemongo" % "0.10.5.0.akka23",
						"org.scalatest" % "scalatest_2.11" % "3.0.0-SNAP4"
)
