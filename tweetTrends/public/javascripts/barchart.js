$(document).ready(function() {
	ajaxCallBar();
})

var ajaxCallBar = (function() {
	$(document).ready(
			function() {
				$(".LoadingImage").show();
				$('#table-body').hide();
				$('#container').hide();
				$.ajax({
					url : "/ajaxcall",
					type : "GET",
					success : function(jsonData) {
						top_data = jsonData;
						barChart(top_data);
						$('#table-body tr').remove();
						for (i in top_data) {
							$('#table-body').append(
									"<tr>" + "<td>" + top_data[i][0] + "</td>"
											+ "<td>" + top_data[i][1] + "</td>"
											+ "</tr>");
						}
						;
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
var barChart = function(top_data) {
	$('#container').highcharts({
		chart : {
			type : 'column'
		},
		title : {
			text : 'Top 10 Trends in Twitter'
		},
		subtitle : {
			text : 'Source:Twitter'
		},
		xAxis : {
			type : 'category',
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
				text : 'HashTags'
			}
		},
		legend : {
			enabled : false
		},
		tooltip : {
			pointFormat : 'Frequency: <b>{point.y:.0f}</b>'
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
				y : 10, // 10 pixels down from the top
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		} ]
	});
}
