$(document).ready(function() {
	$(".LoadingImage").hide();
	datepicker();
	change();
	DateChange();
});
//global variables
var myVar;
var output = document.getElementById("output");
var now = formatDate(new Date());
var wsUri = "ws://localhost:9000/socket?start="+ now;

// WebSocket Call at date change
var DateChange = (function() {
	$(document).ready(
			// DatePicker OnChange event
			$("#datetimepicker1").on("dp.change",change ));

});

var datepicker = (function() {
	$('#datetimepicker1').datetimepicker({
		defaultDate:new Date(),
		maxDate: new Date(),
		//format: 'DD/MM/YYYY HH:mm:ss'
	});										
});

//WebSocket call
var change = function(){
	if (myVar != undefined) {
		clearInterval(myVar);
		}
	$(".LoadingImage").show();
	$('#table-body').hide();
	$('#chartContainer').hide();
	/* clearInterval(myVar); */
	var start = new Date($('#datetimepicker1').data("DateTimePicker").date());
	var startDate = formatDate(start);
	wsUri = "ws://localhost:9000/socket?start="+ startDate;

	function webSocketCall() {

				websocket = new WebSocket(wsUri);
				websocket.onopen = function(evt) {
					websocket.send("ss");
					};
				websocket.onmessage = function(evt) {
					var json_data = JSON.parse(evt.data);
					barChart(json_data);
					$('#table-body tr').remove();
					$('#table-body').append("<tr><th>HashTags</th><th>Number</th></tr>");
					for (i in json_data) {$('#table-body')
								.append(
										"<tr>"
												+ "<td>"
												+ json_data[i][0]
												+ "</td>"
												+ "<td>"
												+ json_data[i][1]
												+ "</td>"
												+ "</tr>");
					};
					$(".LoadingImage").hide();
					$('#table-body').show();
					$('#chartContainer').show();
					websocket.close();
				};
				websocket.onerror = function(evt) {
					writeToScreen('<span style="color: red;">ERROR:</span> '+ evt.data);
				};
			}

			function writeToScreen(message) {
				var pre = document.createElement("p");
				pre.style.wordWrap = "break-word";
				pre.innerHTML = message;
				output.appendChild(pre);
			}
			//calling webSocket 
			webSocketCall();
			myVar = setInterval(webSocketCall,11000);
		}

// Date Formatting
function formatDate(d) {
	var month = d.getMonth();
	var day = d.getDate();
	var hh = d.getHours();
	var mm = d.getMinutes();
	var ss = d.getSeconds();
	month = month + 1;

	month = month + "";

	if (month.length == 1) {
		month = "0" + month;
	}

	day = day + "";

	if (day.length == 1) {
		day = "0" + day;
	}
	hh = hh + "";
	// 00 is not taken by system so using 24.
	if (hh == 0){
		hh = 24;
	}
	return day + '/' + month + '/' + d.getFullYear() + ' ' + hh + ':' + mm
			+ ':' + ss;
}

// Chart Rendering
var barChart = function(top_data) {
	$('#chartContainer').highcharts({
		chart : {
			style: {
				 fontFamily: 'Roboto Condensed'
            },
			backgroundColor: '#F7EAC8',
			type : 'column',
			margin : 100,
			options3d : {
				enabled : true,
				alpha : 10,
				beta : 25,
				depth : 50
			}
		},
		title : {
			text : 'Top 10 Trends in Twitter'
		},
		subtitle : {
			text : 'Source:Twitter'
		},
		xAxis : {
			type : 'category',
			title : {
				text : 'HashTags'
			},
			labels : {
				rotation : -45,
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : 'frequency'
			}
		},
		legend : {
			enabled : false
		},
		tooltip : {
			pointFormat : 'Frequency: <b>{point.y:.0f}</b>'
		},
		plotOptions : {
			column : {
				depth : 20
			},
			series : {
				allowPointSelect : true,
				color : '#1ee084',
				dataLabels : {
					enabled : true,
					borderRadius : 5,
					backgroundColor : '#222',
					borderWidth : 1,
					borderColor : '#999',
					y : -6

				}
			}
		},
		series : [ {
			name : 'HashTags',
			data : top_data,
			dataLabels : {
				enabled : true,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				format : '{point.y:.0f}', // Zero decimal
				y : 5, // 10 pixels down from the top
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		} ]
	});
}
