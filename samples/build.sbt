name := """samples"""

version := "1.0"


scalaVersion := "2.11.5"

libraryDependencies ++= Seq(

	   	      	"org.apache.spark" %% "spark-core" % "1.2.1",
                      	"org.apache.spark" %% "spark-streaming" % "1.2.1",
			"org.json4s" 		%% 	"json4s-native" 	% 	"3.2.10",
			"org.reactivemongo" 	%% 	"reactivemongo" 	% 	"0.10.5.0.akka23",
			"com.knoldus" 		%%	"dbs"			%	"1.0",
			"com.knoldus"		%%	"streaming"		%	"1.0"
)
