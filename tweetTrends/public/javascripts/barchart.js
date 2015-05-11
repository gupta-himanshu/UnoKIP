$(document).ready(function() {
	ajaxCallBar();
	var socket = new WebSocket('ws://localhost:9000/websocket');
	socket.onopen = function(event) {
		alert("opened");
		socket.send("ss");
	};
})

var ajaxCallBar = (function() {
	$(document).ready(
			function() {
				$(".LoadingImage").show();
				$('#table-body').hide();
				$('#container').hide();
				var start = $('#datetimepicker1').data('datetimepicker').getDate();
				alert(start)
				$.ajax({
					url : "/ajaxcall",
					type : "GET",
					data:{
						start:start
					},
					success : function(jsonData) {
						top_data = jsonData;
						barChart(top_data);
						$('#table-body tr').remove();
						$('#table-body').append("<tr><th>HashTags</th><th>Number</th></tr>");
						for (i in top_data) {
							$('#table-body').append(
									"<tr>" + "<td>" + top_data[i][0] + "</td>"
											+ "<td>" + top_data[i][1] + "</td>"
											+ "</tr>");
						};
					},
					dataType : "json",
					complete : function() {
						$(".LoadingImage").hide();
						$('#table-body').show();
						$('#container').show();
					}
				});
			});
});
setInterval(ajaxCallBar, 5000);

var barChart =	function (top_data) {
	    $('#container').highcharts({
	        chart: {
	            type: 'column',
	            margin: 100,
	            options3d: {
	               enabled: true,
	               alpha: 10,
	               beta: 25,
	               depth: 50
	            }
	        },
	        title: {
	            text: 'Top 10 Trends in Twitter'
	        },
	        subtitle: {
	            text: 'Source:Twitter'
	        },
	        xAxis: {
	            type: 'category',
	            title: {
	                text: 'HashTags'
	            },
	            labels: {
	                rotation: -45,
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif'
	                }
	            }
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: 'frequency'
	            }
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            pointFormat: 'Frequency: <b>{point.y:.0f}</b>'
	        },
	        plotOptions: {
	        	column: {
	                depth: 20
	            },
	            series: {
	            	allowPointSelect: true,
	                color: '#CCFF99',
	                dataLabels: {
	                    enabled: true,
	                    borderRadius: 5,
	                    backgroundColor: '#222',
	                    borderWidth: 1,
	                    borderColor: '#999',
	                    y: -6
	                    
	                }
	            }
	        },
	        series: [{
	            name: 'HashTags',
	            data: top_data,
	            dataLabels: {
	                enabled: true,
	                rotation: -90,
	                color: '#FFFFFF',
	                align: 'right',
	                format: '{point.y:.0f}', // Zero decimal
	                y: 5, // 10 pixels down from the top
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif'
	                }
	            }
	        }]
	    });
	}

