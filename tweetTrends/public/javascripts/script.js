/**
 * 
 */
console.log("Welcome to angularJs file");

var testM = angular.module("MyApp", []);

testM.directive('myChart', function() {
	return {
		restrict : 'E',
		replace : true,
		scope : {
			chartid : '='
		},
		template : '<div id="pieContainer{{chartid}}"></div>',
		link : function($scope) {
			$scope.$watch($scope.chartid,
					function() {
						var highchartsOptions = Highcharts.setOptions(Highcharts.theme);     
						$("#pieContainer" + $scope.chartid).highcharts({
							colors: [ '#92CD00','#CC0000', '#FF9900'],
						    
							   /* CHART TITLE */
							   chart: {
								   style: {
									   font: 'bold 16px "Roboto Condensed", sans-serif'
							       },
								   backgroundColor: 'transparent',
								   plotBorderWidth: 0,
								   spacingBottom: 0,
							       spacingTop: 0,
							       spacingLeft: 0,
							       spacingRight: 0,
							       width: null,
							       height: 200,
							       renderTo: '#pieContainer'+1
							   },
							   title: {
							       text: ''
							   },
							   plotOptions: {
							       pie: {
							          cursor: 'pointer',
							          shadow: false,
							          dataLabels: {
							              enabled: true,
							              format: '{point.percentage:.1f} %'
							          }
							       }
							   },
							series : [ {
								type : 'pie',
								name : 'Number Of Tweets',
								innerSize: '60%',
								data : j,
								showInLegend : false
							} ]
						});

						var myElement = document.querySelector("#record" + $scope.chartid);
						console.log(myElement);
						var max = function(positive, negative, neutral){
							if (positive >= negative) {
								if (positive >= neutral) {
									console.log("positive")
									myElement.style.backgroundColor = "#93DB70";
								} else {
									console.log("neutral")
									myElement.style.backgroundColor = "rgb(255,201,102)";
								}
							} else {
								if (negative >= neutral) {
									console.log("negative")
									myElement.style.backgroundColor = "#FF6666"; 
								} else {
									console.log("neutral")
									myElement.style.backgroundColor ="rgb(255,201,102)";
								}
							}
						}
						max(positive, negative, neutral);
					});

		}
	}
});

testM.directive('scaladaySessionData', function() {
	return {
		restrict : 'E',
		replace : true,
		scope : {
			sessiondata : "="
		},
		templateUrl : 'userDetails.htm',
		controller : function($scope) {
			$scope.userinfos = [];
			console.log(">>>")
			var sessions = $scope.sessiondata.Session;
			for (var i = 0; i < sessions.length; i++) {
				$scope.userinfos.push({
					"image" : sessions[i].Image,
					"name" : sessions[i].Name.split("~"),
					"twitterId" : sessions[i].TwitterID.split("~"),
					"room" : sessions[i].Room,
					"topic": sessions[i].Topic,
					"topicId": sessions[i].TopicId
				})
			}
			$scope.userDetails = $scope.userinfos
		}
	}
	})

testM.controller("MyController",function($scope, $log, $filter) {
					$scope.num = 10000;
					$scope.predicate = undefined;
					$scope.sort = true;
					$scope.sortBy = "name";
					$scope.sorting = function() {
						if ($scope.sort == true) {
							$scope.sort = false;
						} else {
							$scope.sort = true;
						}
					}

					$scope.details = [
					        {
								"Date" : "2015-06-08",
								"Time" : "17:00",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@odersky",
									"Name" : "Martin Odersky",
									"Topic" : "Scala - where it came from. where it's going",
									"TopicId" : "1",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Martin_Odersky_square.png"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "09:00",
								"Session" : [{
									"Room" : " ",
									"TwitterID" : "@jboner",
									"Name" : "Jonas Bonér",
									"Topic" : "Life Beyond the Illusion of Present",
									"TopicId" : "2",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Jonas%20Boner.PNG"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "10:25",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@pacoid",
									"Name" : "Paco Nathan",
									"Topic" : "GraphX: Graph analytics for insights about developer communities",
									"TopicId" : "3",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Paco%20Nathan.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@rolandkuhn",
									"Name" : "Roland Kuhn",
									"Topic" : "Project Gålbma: Actors vs. Types",
									"TopicId" : "4",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Roland%20Kuhn.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@den_sh",
									"Name" : "Denys Shabalin",
									"Topic" : "Type-safe off-heap memory for Scala",
									"TopicId" : "5",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Denys%20Shabalin.PNG"
								},
								{
									"Room" : "Veilingzaal",
									"TwitterID" : "@heejongl",
									"Name" : "Heejong Lee",
									"Topic" : "Yoyak : Static analysis framework for Scala",
									"TopicId" : "6",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Heejong%20Lee.PNG"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "11:35",
								"Session":[{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@Koze",
									"Name" : "Alexander Kops",
									"Topic" : "From Java to Scala in Less Than Three Months Daniel Nowak",
									"TopicId" : "7",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Alexander%20Kops.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@helenaedelson",
									"Name" : "Helena Edelson",
									"Topic" : "Lambda Architecture with Spark Streaming: Kafka:Cassandra: Akka and Scala",
									"TopicId" : "8",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Helena%20Edelson.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@dustinwhittle",
									"Name" : "Dustin Whittle",
									"Topic" : "Performance Testing Crash Course",
									"TopicId" : "9",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Dustin%20Whittle.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@IAnastassija ~ @afruze",
									"Name" : "Anastasia Izmaylova ~ Ali Afroozeh",
									"Topic" : "Meerkat parsers: a general parser combinator library for real programming languages",
									"TopicId" : "10",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Anastasia%20Izmaylova.PNG ~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Ali%20Afroozeh.PNG"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "13:20",
								"Session" :[{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@stuhood",
									"Name" : "Stu Hood",
									"Topic" : "From Source: Scala at Twitter",
									"TopicId" : "11",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Stu%20Hood.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@darkdimius",
									"Name" : "Dmitry Petrashko",
									"Topic" : "Making your Scala applications smaller and faster with the Dotty linkerm",
									"TopicId" : "12",
									"Image" : ""
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@caxaria",
									"Name" : "Joao Caxaria",
									"Topic" : "Scala code quality: learnings of analyzing thousands of projects",
									"TopicId" : "13",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Joao%20Caxaria.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@dlwh ~@ignaciocases",
									"Name" : "David Hall ~Ignacio Cases",
									"Topic" : "Functional Natural Language Processing with Scala",
									"TopicId" : "14",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/David_Hall.jpeg"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "14:30",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@rondoftw~@wpalmeri",
									"Name" : "Ronnie Chen ~ Will Palmeri",
									"Topic" : "Hiring & Onboarding for your Scala Team",
									"TopicId" : "15",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Ronnie%20Chen.PNG ~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Will%20Palmeri.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@xtordoir~@noootsab",
									"Name" : "Xavier Tordoir ~ Andy Petrella",
									"Topic" : "Distributed Machine Learning 101 using Apache Spark from the Browser",
									"TopicId" : "16",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Xavier%20Tordoir.PNG ~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Andy%20Petrella.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@codefinger",
									"Name" : "Joe Kutner",
									"Topic" : "The Twelve Factor App: Best Practices for Scala Deployment",
									"TopicId" : "17",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Joe%20Kutner.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@eamelink",
									"Name" : "Erik Bakker",
									"Topic" : "Options in Futures, how to unsuck them",
									"TopicId" : "18",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Erik%20Bakker.PNG"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "15:40",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@eed3si9n",
									"Name" : "Eugene Yokota",
									"Topic" : "SOAP and XML databinding using scalaxb",
									"TopicId" : "19",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Eugene%20Yokota.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@loverdos",
									"Name" : "Christos KK Loverdos",
									"Topic" : "YAB: Yet Another Build tool",
									"TopicId" : "20",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Christos%20KK.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@kennethrose82",
									"Name" : "Kenneth Rose",
									"Topic" : "WatchDog: How PagerDuty uses Scala for end-to-end functional testing",
									"TopicId" : "21",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Kenneth%20Rose.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@xeno_by",
									"Name" : "Eugene Burmako",
									"Topic" : "State of the Meta,Summer 2015",
									"TopicId" : "22",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/EugeneBurmako.png"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "16:50",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@zapletal_martin",
									"Name" : "Martin Zapletal",
									"Topic" : "Large volume data analysis on the Typesafe Reactive Platform",
									"TopicId" : "23",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Martin%20Zapletal.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@dickwall",
									"Name" : "Dick Wall",
									"Topic" : "Scala needs YOU!",
									"TopicId" : "24",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Dick_Wall.jpeg"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@ScalaFacts",
									"Name" : "Marconi Lanna",
									"Topic" : "What's new since \"Programming in Scala\"",
									"TopicId" : "25",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Marconi_Lanna.png"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@philippkhaller~@heathercmiller",
									"Name" : "Philipp Haller ~ Heather Miller",
									"Topic" : "Function-Passing Style,A New Model for Asynchronous and Distributed Programming",
									"TopicId" : "26",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Philipp%20Haller.PNG ~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Heather%20Miller.PNG"
								}]
							},{
								"Date" : "2015-06-09",
								"Time" : "18:00",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@ivantopo",
									"Name" : "Ivan Topolnjak",
									"Topic" : "Kamon: Metrics and traces for your reactive application",
									"TopicId" : "27",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Ivan%20Topolnjak.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@honzam399",
									"Name" : "Jan Machacek",
									"Topic" : "Exercise in machine learning",
									"TopicId" : "28",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Jan_Machacek.jpeg"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@muuki88",
									"Name" : "Nepomuk Seiler",
									"Topic" : "Write once,deploy everywhere",
									"TopicId" : "29",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Nepomuk%20Seiler.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@noelwelsh",
									"Name" : "Noel Welsh",
									"Topic" : "Essential Scala: Six Core Principles for Learning Scala",
									"TopicId" : "30",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Noel%20Welsh.PNG"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "09:00",
								"Session" : [{
									"Room" : "noRoom",
									"TwitterID" : "",
									"Name" : "Adam Gibson",
									"Topic" : "The Future of AI in Scala, and on the JVM",
									"TopicId" : "31",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Adam_Gibson.png"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "10:25",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@deanwampler",
									"Name" : "Dean Wampler",
									"Topic" : "Why Spark Is the Next Top (Compute) Model",
									"TopicId" : "32",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Dean%20Wampler.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "",
									"Name" : "Mikhail Limanskiy",
									"Topic" : "Embedding a language into a string interpolator",
									"TopicId" : "33",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Mike%20Limansky.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@propensive",
									"Name" : "Jon Pretty",
									"Topic" : "Delimited dependently-typed monadic checked exceptions in Scala",
									"TopicId" : "34",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Jon%20Pretty.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@pandamonial",
									"Name" : "Amanda Laucher",
									"Topic" : "Types vs Tests : An Epic Battle?",
									"TopicId" : "35",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Amanda_Laucher.png"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "11:35",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@sirthias",
									"Name" : "Mathias Doenitz",
									"Topic" : "The Reactive Streams Implementation Landscape",
									"TopicId" : "36",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Mathias%20Doenitz.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@Sandbaek ~@FlorianKirmaier",
									"Name" : "Hans-Henry Sandbaek ~ Florian Kirmaier",
									"Topic" : "SimpleFX a new Scala DSL for W.O.R.A.",
									"TopicId" : "37",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Hans-Henry%20Sandbaek.PNG ~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Florian%20Kirmaier.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@fommil~",
									"Name" : "Sam Halliday ~ Rory Graves",
									"Topic" : "Ensime - why would anybody build another Scala IDE?",
									"TopicId" : "38",
									"Image" : "https://pbs.twimg.com/profile_images/494912315976736768/x9VVsrs2_reasonably_small.jpeg~https://res.cloudinary.com/skillsmatter/image/upload/c_thumb,g_face,w_127,h_127/c_scale,w_127,h_127/v1416225068/cnrhqizvtxvbaqjhxnlg.png"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@noelmarkham",
									"Name" : "Noel Markham",
									"Topic" : "A purely functional approach to building large applications",
									"TopicId" : "39",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Noel%20Markham.PNG"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "13:20",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@StefanZeiger",
									"Name" : "Stefan Zeiger",
									"Topic" : "Reactive Slick for Database Programming",
									"TopicId" : "40",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Stefan%20Zieger.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@d6y",
									"Name" : "Richard Dallaway",
									"Topic" : "Towards Browser and Server Utopia with Scala.JS: an example using CRDTs",
									"TopicId" : "41",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Richard%20Dallaway.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "~@devmage",
									"Name" : "Alex Yakushev ~ Andrew Harris",
									"Topic" : "Almost Zen: Reflections on Four Years of Scala in Practice",
									"TopicId" : "42",
									"Image" : "~ http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Andrew%20Harris.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@avslesarenko",
									"Name" : "Alexander Slesarenko",
									"Topic" : "Program Functionally, Execute Imperatively: Peeling abstraction overhead from functional programs",
									"TopicId" : "43",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Alexander%20Slesarenko.PNG"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "14:30",
								"Session" :[{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@lutzhuehnken",
									"Name" : "Lutz Huehnken",
									"Topic" : "So how do I do a 2-phase-commit with Akka then?",
									"TopicId" : "44",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Lutz%20Huehnken.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@sjrdoeraene",
									"Name" : "Sébastien Doeraene",
									"Topic" : "Scala.js Semantics - and how they support performance and JavaScript interop",
									"TopicId" : "45",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/S%C3%A9bastien%20Doeraene.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@ItsMeijers",
									"Name" : "Thomas Meijers",
									"Topic" : "Building your first REST API in less than 30 minutes",
									"TopicId" : "46",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Thomas%20Meijers.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "",
									"Name" : "Simon Ochsenreither",
									"Topic" : "Project Valhalla: Part 2 – Value Types in the JVM",
									"TopicId" : "47",
									"Image" : ""
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "15:40",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@MichaelPNash",
									"Name" : "Michael Nash",
									"Topic" : "Easy Scalability with Akka",
									"TopicId" : "48",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Michael%20Nash.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "@jsuereth",
									"Name" : "Josh Suereth",
									"Topic" : "The road to sbt 1.0 is paved with server",
									"TopicId" : "49",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Josh%20Suereth.PNG"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@tobyjsullivan",
									"Name" : "Toby Sullivan",
									"Topic" : "Low-Friction Microservices with Scala and Play",
									"TopicId" : "50",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Toby%20Sullivan.PNG"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@davegurnell",
									"Name" : "Dave Gurnell",
									"Topic" : "Functional Data Validation (or How to Think Functionally)",
									"TopicId" : "51",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Dave%20Gurnell.PNG"
								}]
							},{
								"Date" : "2015-06-10",
								"Time" : "16:50",
								"Session" : [{
									"Room" : "Effectenbeurszaal",
									"TwitterID" : "@flaviowbrasil",
									"Name" : "Flavio Brasil",
									"Topic" : "Don't Block Yourself",
									"TopicId" : "52",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Flavio%20W.%20Brasil.PNG"
								},{
									"Room" : "Administratiezaal",
									"TwitterID" : "~",
									"Name" : "Andrew Phillips ~ Nermin Serifovic ",
									"Topic" : "Scala Puzzlers: \\In the Matrix, anything is possible!\\",
									"TopicId" : "53",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Andrew_Phillips.png ~"
								},{
									"Room" : "Berlagezaal",
									"TwitterID" : "@Safela",
									"Name" : "Alexander Podkhalyuzin",
									"Topic" : "A next generation tool for Scala code review",
									"TopicId" : "54",
									"Image" : "http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Alexander_Podkhalyuzin.jpg"
								},{
									"Room" : "Veilingzaal",
									"TwitterID" : "@polyulya",
									"Name" : "Yuriy Polyulya",
									"Topic" : "Functional programming with arrows",
									"TopicId" : "55",
									"Image" : "https://pbs.twimg.com/profile_images/481013923110346752/ACA3C4Lp.jpeg"

								}]
							}];
				})
