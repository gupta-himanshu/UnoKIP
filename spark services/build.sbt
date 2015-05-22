name := "spark test"

version := "1.0"

scalaVersion := "2.10.4"

organization := "com.knoldus"

libraryDependencies ++= Seq(
                      "org.apache.spark"	%%	"spark-core"	    %	"1.3.0"	%	"provided" exclude("io.netty", "netty-all"),
                      "org.apache.spark"	%%	"spark-streaming"	% 	"1.3.0" % "provided" exclude("io.netty", "netty-all"),
                      "org.apache.spark" 	%% 	"spark-streaming-twitter" % 	"1.3.0",
                      "org.scalatest"	 	%% 	"scalatest" % "2.2.4" 	  %       "test",
                      "org.reactivemongo" 	%% 	"reactivemongo" 	% 	"0.10.5.0.akka23",
		              "org.mockito" 		% 	"mockito-all" 		% 	"1.8.4",
		              "joda-time"		 % 	"joda-time" 		%	 "2.1",
		              "org.twitter4j"		 % 	"twitter4j-core"	 % 	"3.0.3",
		              "com.typesafe.akka" 	%% 	"akka-actor" 		% 	"2.3.4",
		              "log4j" 			% 	"log4j" 		% 	"1.2.17",
		              "io.spray"            	%% 	"spray-can"        	%  	"1.3.2",
	                  "io.spray"           	 %%	 "spray-routing"    	% 	"1.3.2",
			          "io.spray"          	 %% 	"spray-json"       	% 	"1.3.1",
			          "com.typesafe.play"	 %% 	"play-json"	 	%	 "2.3.9",
			          "io.netty" % "netty-all" % "4.0.23.Final",
			          "com.typesafe" % "config" % "1.2.1",
	          "com.typesafe.akka" %% "akka-slf4j" % "2.3.4" % "provided",
		"io.spray" %% "spray-testkit" % "1.3.1" % "test"	
		       )

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

test in assembly := {}

assemblyJarName in assembly := "spark_services-1.0-SNAPSHOT.jar"

excludedJars in assembly <<= (fullClasspath in assembly).map { _ filter { cp =>List("servlet-api", "guice-all", "junit", "uuid","jetty", "jsp-api-2.0", "antlr", "avro", "slf4j-log4j", "log4j-1.2","scala-actors", "commons-cli", "stax-api", "mockito").exists(cp.data.getName.startsWith(_))
    } }

assembleArtifact in packageScala := false  // We don't need the Scala library, Spark already includes it

mergeStrategy in assembly := {
      case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
      case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
      case "reference.conf" => MergeStrategy.concat
      case _ => MergeStrategy.first
    }
