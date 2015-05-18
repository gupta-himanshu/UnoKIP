name := "spark services"

version := "1.0"

scalaVersion := "2.11.4"

organization := "com.knoldus"

libraryDependencies ++= Seq(
                      "org.apache.spark" 	%% 	"spark-core"	        % 	"1.3.1" % "provided",
                      "org.apache.spark" 	%% 	"spark-streaming"       % 	"1.3.1" ,
                      "org.apache.spark" 	%% 	"spark-streaming-twitter" % 	"1.3.1" ,
                       "com.typesafe.akka"     %% "akka-actor"         % "2.3.2",
                      "org.scalatest"	 	%% 	"scalatest" % "2.2.4" 	%       "test",
                      "org.reactivemongo" 	%% 	"reactivemongo" 	% 	"0.10.5.0.akka23",
		      "org.mockito" 		% 	"mockito-all" 		% 	"1.8.4",
		      "joda-time"		 % 	"joda-time" 		%	 "2.7",
		      "org.twitter4j"		 % 	"twitter4j-core"	 % 	"3.0.3",
		      "com.typesafe.akka" 	%% 	"akka-actor" 		% 	"2.3.4",
		      "log4j" 			% 	"log4j" 		% 	"1.2.17",
		      "io.spray"            	%% 	"spray-can"        	%  	"1.3.1",
	              "io.spray"           	 %%	 "spray-routing"    	% 	"1.3.1",
			"io.spray"          	 %% 	"spray-json"       	% 	"1.3.1",
			"com.typesafe.akka"  	%% 	"akka-actor"       	%	 "2.3.7",
			"com.typesafe.play"	 %% 	"play-json"	 	%	 "2.3.9"
		       )

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

test in assembly := {}

assemblyJarName in assembly := "spark_services-1.0-SNAPSHOT.jar"

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
 cp filter {x => x.data.getName.matches("unused-1.0.0.jar")}
}
