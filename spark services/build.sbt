name := "spark services"

version := "1.0"

scalaVersion := "2.11.4"

organization := "com.knoldus"

libraryDependencies ++= Seq(
                      "org.apache.spark" %% "spark-core" % "1.3.1",
                      "org.apache.spark" %% "spark-streaming" % "1.3.1",
                      "org.apache.spark" %% "spark-streaming-twitter" % "1.3.1",
                      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
                      "org.reactivemongo" 	%% 	"reactivemongo" 	% 	"0.10.5.0.akka23",
    		         "org.apache.spark" %% "spark-sql" % "1.3.1",
    			 "ch.qos.logback" % "logback-classic" % "1.1.3"
       		       )
