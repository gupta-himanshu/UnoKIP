name := """tweetTrendsTest"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(				
						ws,
  						"org.reactivemongo" 	%% 	"reactivemongo" % "0.10.5.0.akka23",
						"org.webjars" 			%% 	"webjars-play" 		% "2.3.0-2",
  						"org.webjars" 			%	"bootstrap" 		% "3.1.1-2",
						"com.knoldus"          %%   "spark-services"  %   "1.0",
						     "joda-time" % "joda-time" % "2.7"		
)

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;controllers.ref.*;controllers.javascript.*;"
