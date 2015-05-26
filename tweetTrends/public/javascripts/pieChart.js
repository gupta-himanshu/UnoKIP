var chart;
$(document).ready(function() {
/* PIE CHART THEME */
Highcharts.theme = {
   /* LINE/BAR/COLUMN/SLICE COLORS - only used for slices for Plex, if we add multiple data sets in future releases, these colors will work with the rendering of other sets */
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
   }    
}; 

var highchartsOptions = Highcharts.setOptions(Highcharts.theme);     
/*var callPieChart = function(i, data){
	// Apply the theme
    
	$('#pieContainer'+i).highcharts({
	        series: [{
	            type: 'pie',
	            innerSize: '60%',
	            name: 'number of tweets',
	            data: data,
	            showInLegend:false
	        }]
        });
}*/
/*for(i=1;i<=55;i++){
	callPieChart(i,j);
	var idOfDiv = "#aas"+i;
	console.log(idOfDiv)
	var myElement = document.querySelector(idOfDiv);
	var pos=5
	var neg = 1
	var neu =3
	if(positive>=negative){
		if(positive >= neutral){
			console.log("positive")
			myElement.style.backgroundColor = "#93DB70";	
		}
		else{
			console.log("neutral")
			myElement.style.backgroundColor = "#FFC966";
		}
	}
	else{
		if(negative >=neutral){
			console.log("negative")
			myElement.style.backgroundColor = "#FF4040";
		}
		else{
			console.log("neutral")
			myElement.style.backgroundColor = "#FFC966";
		}
	}
}*/
});