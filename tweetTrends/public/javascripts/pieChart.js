var chart;
$(document).ready(function() {
    
/* PIE CHART THEME */
Highcharts.theme = {
   /* LINE/BAR/COLUMN/SLICE COLORS - only used for slices for Plex, if we add multiple data sets in future releases, these colors will work with the rendering of other sets */
   colors: [ '#92CD00','#CC0000', '#FF9900'],
    
   /* CHART TITLE */
   title: {
      style: {
         color: '#000',
         font: 'bold 16px Roboto Condensed, sans-serif'
      }
   },
   chart: {
	   backgroundColor: '#EEEEEE',
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
       text: 'Twitter Sentiment Analysis'
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
   }    
}; 
  
var data_json = [[
       ['Positive',  50.0],
       ['Negative',  10.0],
       ['Neutral',   9.0],
   ],[
      ['Positive',  17.0],
      ['Negative',  11.0],
      ['Neutral',   12.0],
  ],[
     ['Positive',  11.0],
     ['Negative',  11.0],
     ['Neutral',   11.0],
 ],[
    ['Positive',  11.0],
    ['Negative',  11.0],
    ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
  ['Positive',  11.0],
  ['Negative',  11.0],
  ['Neutral',   11.0],
],[
 ['Positive',  11.0],
 ['Negative',  11.0],
 ['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
   ['Positive',  11.0],
   ['Negative',  11.0],
   ['Neutral',   11.0],
],[
  ['Positive',  11.0],
  ['Negative',  11.0],
  ['Neutral',   11.0],
],[
 ['Positive',  11.0],
 ['Negative',  11.0],
 ['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
],[
['Positive',  11.0],
['Negative',  11.0],
['Neutral',   11.0],
]];
var highchartsOptions = Highcharts.setOptions(Highcharts.theme);     
var callPieChart = function(i, data){
	// Apply the theme
    
	$('#pieContainer'+i).highcharts({
	        series: [{
	            type: 'pie',
	            innerSize: '0%',
	            name: 'number of tweets',
	            data: data,
	            showInLegend:false
	        }]
	    /*},function(chart){
            var middleX = chart.plotWidth / 2;
            var series = chart.series[0];
            var points = series.points;
            console.log(series.center[0],series.center[1])
            // middle image
            chart.renderer.image('http://event.scaladays.org/dl/photos/Scala%20Days%202015%20speakers/Martin_Odersky_square.png', series.center[0] - 25, series.center[1]+10, 80, 80)
            .add();
        */    
        });
}
for (i = 0; i < 56; i++) {
	callPieChart(i,data_json[i-1]);
}
});